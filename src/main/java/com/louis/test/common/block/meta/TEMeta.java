package com.louis.test.common.block.meta;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.common.TileEntityEnder;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.api.heat.HeatStorage;
import com.louis.test.api.mte.IMTE;
import com.louis.test.api.mte.MetaTileEntity;
import com.louis.test.common.block.AbstractTE;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2")})
public class TEMeta extends AbstractTE implements IMTE {

    private final MTEBase mte;

    public TEMeta(int meta) {
        this.meta = meta;
        this.mte = MetaTileEntity.fromMeta(meta)
            .createInstance(meta);
    }

    public TEMeta() {
        this(0);
    }

    public MTEBase getMte() {
        return mte;
    }

    // Renderer

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z, Block block) {
        if (mte != null) mte.setBlockBoundsBasedOnState(world, x, y, z, block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (mte != null) {
            AxisAlignedBB box = mte.getRenderBoundingBox();
            if (box != null) return box;
        }
        return super.getRenderBoundingBox();
    }

    // Interact

    @Override
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
                                    float hitZ) {
        if (mte != null) return mte.onBlockActivated(world, player, side, hitX, hitY, hitZ);
        return super.onBlockActivated(world, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (mte != null) mte.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (mte != null) mte.onBlockPlacedBy(world, x, y, z, player, stack);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        if (mte != null) mte.onNeighborBlockChange(world, x, y, z, nbid);
    }

    @Override
    public void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemIn) {
        if (mte != null) mte.dropBlockAsItem(world, x, y, z, itemIn);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {
        if (mte != null) mte.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        if (mte != null) return mte.canConnectRedstone(world, x, y, z, side);
        return super.canConnectRedstone(world, x, y, z, side);
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        if (mte != null) return mte.onBlockEventReceived(world, x, y, z, eventId, eventData);
        return false;
    }

    // Special

    @Override
    protected void doUpdate() {
        if (mte != null) mte.doUpdate();
        super.doUpdate();
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
        if (mte != null) mte.processDrop(world, x, y, z, te, stack);
    }

    @Override
    public boolean isActive() {
        if (mte != null) return mte.isActive();
        return false;
    }

    @Override
    public String getMachineName() {
        if (mte != null) return mte.getMachineName();
        return "";
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        if (mte != null) return mte.processTasks(redstoneCheckPassed);
        return false;
    }

    // TE

    @Override
    public void onChunkUnload() {
        if (mte != null) mte.onChunkUnload();
        super.onChunkUnload();
    }

    @Override
    public void validate() {
        super.validate();
        if (mte != null) {
            if (mte.host == null) {
                mte.setHost(this);
            } else {
                mte.validate();
            }
        }
    }

    @Override
    public void invalidate() {
        if (mte != null) mte.invalidate();
        super.invalidate();
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        if (mte != null) mte.writeCommon(root); // Lưu cả TileEntity con
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        if (mte != null) mte.readCommon(root);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        // this.writeCommon(tag);
        writeCustomNBT(tag);

        if (mte != null) {
            Packet mtePacket = mte.getDescriptionPacket();
            if (mtePacket instanceof S35PacketUpdateTileEntity) {
                NBTTagCompound mteTag = ((S35PacketUpdateTileEntity) mtePacket).func_148857_g();
                for (Object keyObj : mteTag.func_150296_c()) {
                    String key = (String) keyObj;
                    tag.setTag(key, mteTag.getTag(key)); // Gộp từng key
                }
            }
        }

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        // this.readCommon(tag);
        readCustomNBT(tag);

        if (mte != null) {
            mte.onDataPacket(
                net,
                new S35PacketUpdateTileEntity(mte.xCoord, mte.yCoord, mte.zCoord, pkt.func_148853_f(), tag));
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (mte != null) return mte.receiveClientEvent(id, type);
        return super.receiveClientEvent(id, type);
    }

    @Override
    protected boolean shouldUpdate() {
        return mte != null && mte.canUpdate();
    }

    // IGuiHolder<PosGuiData>

    public void openGui(EntityPlayer player) {
        if (mte != null) mte.openGui(player);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        if (mte != null) return mte.buildUI(data, syncManager, settings);
        return null;
    }

    // IFluidHandlerAdv

    @Override
    public SmartTank[] getTanks() {
        if (mte != null) return mte.getTanks();
        return new SmartTank[0];
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (mte != null) return mte.fill(from, resource, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (mte != null) return mte.drain(from, resource, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (mte != null) return mte.drain(from, maxDrain, doDrain);
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if (mte != null) return mte.canFill(from, fluid);
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        if (mte != null) return mte.canDrain(from, fluid);
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (mte != null) return mte.getTankInfo(from);
        return new FluidTankInfo[0];
    }

    // IImmersiveConnectable

    @Override
    public boolean canConnect() {
        if (mte != null) return mte.canConnect();
        return false;
    }

    @Override
    public boolean isEnergyOutput() {
        if (mte != null) return mte.isEnergyOutput();
        return false;
    }

    @Override
    public int outputEnergy(int amount, boolean simulate, int energyType) {
        if (mte != null) return mte.outputEnergy(amount, simulate, energyType);
        return 0;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        if (mte != null) return mte.canConnectCable(cableType, target);
        return false;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target) {
        if (mte != null) mte.connectCable(cableType, target);
    }

    @Override
    public WireType getCableLimiter(TargetingInfo target) {
        if (mte != null) return mte.getCableLimiter(target);
        return null;
    }

    @Override
    public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
        if (mte != null) return mte.allowEnergyToPass(con);
        return false;
    }

    @Override
    public void onEnergyPassthrough(int amount) {
        if (mte != null) mte.onEnergyPassthrough(amount);
    }

    @Override
    public void removeCable(ImmersiveNetHandler.Connection connection) {
        if (mte != null) mte.removeCable(connection);

    }

    @Override
    public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
        if (mte != null) return mte.getRaytraceOffset(link);
        return null;
    }

    @Override
    public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
        if (mte != null) return mte.getConnectionOffset(con);
        return null;
    }

    // IEnergyHandler

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (mte != null) return mte.receiveEnergy(from, maxReceive, simulate);
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (mte != null) return mte.extractEnergy(from, maxExtract, simulate);
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (mte != null) return mte.getEnergyStored(from);
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (mte != null) return mte.getMaxEnergyStored(from);
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if (mte != null) return mte.canConnectEnergy(from);
        return false;
    }

    // IHeatHandler

    @Override
    public HeatStorage getHeat() {
        if (mte != null) return mte.getHeat();
        return null;
    }

    @Override
    public float receiveHeat(ForgeDirection side, float amount, boolean doTransfer) {
        if (mte != null) return mte.receiveHeat(side, amount, doTransfer);
        return 0;
    }

    @Override
    public float extractHeat(ForgeDirection side, float amount, boolean doTransfer) {
        if (mte != null) return mte.extractHeat(side, amount, doTransfer);
        return 0;
    }

    @Override
    public boolean canReceiveHeat(ForgeDirection side) {
        if (mte != null) return mte.canReceiveHeat(side);
        return false;
    }

    @Override
    public boolean canExtractHeat(ForgeDirection side) {
        if (mte != null) return mte.canExtractHeat(side);
        return false;
    }

    // IEnergySink

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        if (mte != null) return mte.getDemandedEnergy();
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        if (mte != null) return mte.getSinkTier();
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1) {
        if (mte != null) return mte.injectEnergy(forgeDirection, v, v1);
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if (mte != null) return mte.acceptsEnergyFrom(tileEntity, forgeDirection);
        return false;
    }

    // IEnergySource

    @Optional.Method(modid = "IC2")
    @Override
    public double getOfferedEnergy() {
        if (mte != null) return mte.getOfferedEnergy();
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void drawEnergy(double v) {
        if (mte != null) mte.drawEnergy(v);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSourceTier() {
        if (mte != null) return mte.getSourceTier();
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        if (mte != null) return mte.emitsEnergyTo(tileEntity, forgeDirection);
        return false;
    }
}
