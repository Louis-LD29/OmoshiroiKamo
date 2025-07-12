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
import com.louis.test.api.ModObject;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.api.heat.HeatStorage;
import com.louis.test.api.mte.IMTE;
import com.louis.test.api.mte.MetaTileEntity;
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
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "IC2")})
public class MTEBase implements IMTE {

    protected TEMeta host;
    protected final int meta;
    protected World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;

    public MTEBase(int meta) {
        this.meta = meta;
    }

    public void setHost(TEMeta te) {
        this.host = te;
        this.worldObj = te.getWorldObj();
        this.xCoord = te.xCoord;
        this.yCoord = te.yCoord;
        this.zCoord = te.zCoord;
    }

    public TEMeta getHost() {
        return host;
    }

    public int getMeta() {
        return meta;
    }

    public World getWorldObj() {
        return worldObj;
    }

    public ChunkCoordinates getChunkCoord() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    public void markDirty() {
        host.markDirty();
    }

    public void setForceClientUpdate(boolean forceClientUpdate) {
        host.setForceClientUpdate(forceClientUpdate);
    }

    // Render

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z, Block block) {

    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return null;
    }

    // Interact

    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
                                    float hitZ) {
        return false;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {

    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        host.redstoneStateDirty = true;
    }

    public void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemIn) {

    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {

    }

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return false;
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        return false;
    }

    // Special

    public void doUpdate() {

    }

    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {

    }

    public String getMachineName() {
        String base = ModObject.blockMeta.unlocalisedName;
        return base + "."
            + MetaTileEntity.fromMeta(meta)
            .name()
            .toLowerCase();
    }

    public boolean isActive() {
        return false;
    }

    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    public boolean isServerSide() {
        return host.isServerSide();
    }

    public int getFacing() {
        return host.getFacing();
    }

    // TE

    public void onChunkUnload() {
    }

    public void validate() {

    }

    public void invalidate() {
    }

    public void writeCommon(NBTTagCompound root) {
    }

    public void readCommon(NBTTagCompound root) {
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeCommon(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        this.readCommon(tag);
    }

    public boolean receiveClientEvent(int id, int type) {
        return false;
    }

    protected boolean canUpdate() {
        return true;
    }

    // IGuiHolder<PosGuiData>

    public void openGui(EntityPlayer player) {
        host.openGui(player);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }

    // IFluidHandlerAdv

    @Override
    public SmartTank[] getTanks() {
        return new SmartTank[0];
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    // IHeatHandler

    @Override
    public HeatStorage getHeat() {
        return null;
    }

    @Override
    public float receiveHeat(ForgeDirection side, float amount, boolean doTransfer) {
        return 0;
    }

    @Override
    public float extractHeat(ForgeDirection side, float amount, boolean doTransfer) {
        return 0;
    }

    @Override
    public boolean canReceiveHeat(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canExtractHeat(ForgeDirection side) {
        return false;
    }

    // IImmersiveConnectable

    @Override
    public boolean canConnect() {
        return false;
    }

    @Override
    public boolean isEnergyOutput() {
        return false;
    }

    @Override
    public int outputEnergy(int amount, boolean simulate, int energyType) {
        return 0;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return false;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target) {
    }

    @Override
    public WireType getCableLimiter(TargetingInfo target) {
        return null;
    }

    @Override
    public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
        return false;
    }

    @Override
    public void onEnergyPassthrough(int amount) {
    }

    @Override
    public void removeCable(ImmersiveNetHandler.Connection connection) {
    }

    @Override
    public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
        return null;
    }

    @Override
    public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
        return null;
    }

    // IEnergyHandler

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        return false;
    }

    // IEnergySink

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(ForgeDirection forgeDirection, double v, double v1) {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return false;
    }

    // IEnergySource

    @Optional.Method(modid = "IC2")
    @Override
    public double getOfferedEnergy() {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void drawEnergy(double v) {

    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSourceTier() {
        return 0;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean emitsEnergyTo(TileEntity tileEntity, ForgeDirection forgeDirection) {
        return false;
    }
}
