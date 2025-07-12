package com.louis.test.common.block.meta.energyConnector;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IICProxy;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import com.louis.test.api.energy.MaterialWireType;
import com.louis.test.api.material.VoltageTier;
import com.louis.test.common.block.meta.MTEBase;
import com.louis.test.common.core.helper.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Set;

public abstract class AbstractMTEConnector extends MTEBase {

    public AbstractMTEConnector(int meta) {
        super(meta);
    }

    protected WireType limitType = null;
    private boolean needsVisualUpdate = true;

    protected boolean canTakeULV() {
        return false;
    }

    protected boolean canTakeLV() {
        return false;
    }

    protected boolean canTakeMV() {
        return false;
    }

    protected boolean canTakeHV() {
        return false;
    }

    protected boolean canTakeEV() {
        return false;
    }

    protected boolean canTakeIV() {
        return false;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (worldObj != null && (!worldObj.isRemote || !Minecraft.getMinecraft()
            .isSingleplayer()))
            ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(this.host), worldObj, !worldObj.isRemote);
    }

    @Override
    public void onEnergyPassthrough(int amount) {
    }

    @Override
    public boolean allowEnergyToPass(Connection con) {
        return true;
    }

    @Override
    public boolean canConnect() {
        return true;
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
        if (cableType instanceof MaterialWireType) {
            VoltageTier tier = ((MaterialWireType) cableType).getMaterial()
                .getVoltageTier();
            if (tier == VoltageTier.ULV && !canTakeULV()) return false;
            if (tier == VoltageTier.LV && !canTakeLV()) return false;
            if (tier == VoltageTier.MV && !canTakeMV()) return false;
            if (tier == VoltageTier.HV && !canTakeHV()) return false;
            if (tier == VoltageTier.EV && !canTakeEV()) return false;
            if (tier == VoltageTier.IV && !canTakeIV()) return false;
        }
        if (cableType == WireType.STEEL && !canTakeHV()) return false;
        if (cableType == WireType.ELECTRUM && !canTakeMV()) return false;
        if (cableType == WireType.COPPER && !canTakeLV()) return false;
        if (cableType == WireType.STRUCTURE_ROPE) return false;
        if (cableType == WireType.STRUCTURE_STEEL) return false;
        return limitType == null || limitType == cableType;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target) {
        this.limitType = cableType;
        this.markDirty();
        host.redstoneStateDirty = true;
    }

    @Override
    public WireType getCableLimiter(TargetingInfo target) {
        return this.limitType;
    }

    @Override
    public void removeCable(Connection connection) {
        WireType type = connection != null ? connection.cableType : null;
        Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this.host));
        if (outputs == null || outputs.size() == 0) {
            if (type == limitType || type == null) this.limitType = null;
        }
        this.markDirty();
        host.redstoneStateDirty = true;
    }

    @Override
    public void onChunkUnload() {
        ImmersiveNetHandler.INSTANCE.setProxy(new DimensionBlockPos(this.host), new IICProxy(this.host));
        super.onChunkUnload();
    }

    @Override
    public void validate() {
        if (ImmersiveNetHandler.INSTANCE != null) {
            ImmersiveNetHandler.INSTANCE.resetCachedIndirectConnections();
        }
        super.validate();
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        if (needsVisualUpdate) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            needsVisualUpdate = false;
        }

    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCommon(nbttagcompound);
        if (worldObj != null && !worldObj.isRemote) {
            NBTTagList connectionList = new NBTTagList();
            Set<Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(worldObj, Utils.toCC(this.host));
            if (conL != null) for (Connection con : conL) connectionList.appendTag(con.writeToNBT());
            nbttagcompound.setTag("connectionList", connectionList);
        }
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();
        this.readCommon(nbt);
        needsVisualUpdate = true;
        if (worldObj != null && worldObj.isRemote
            && !Minecraft.getMinecraft()
            .isSingleplayer()) {
            NBTTagList connectionList = nbt.getTagList("connectionList", 10);
            ImmersiveNetHandler.INSTANCE.clearConnectionsOriginatingFrom(Utils.toCC(this.host), worldObj);
            for (int i = 0; i < connectionList.tagCount(); i++) {
                NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
                Connection con = Connection.readFromNBT(conTag);
                if (con != null) {
                    ImmersiveNetHandler.INSTANCE.addConnection(worldObj, Utils.toCC(this.host), con);
                } else Logger.error("CLIENT read connection as null");
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int arg) {
        if (id == -1 || id == 255) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }
        return false;
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        if (root.hasKey("needsVisualUpdate")) {
            needsVisualUpdate = root.getBoolean("needsVisualUpdate");
        }
        try {
            if (root.hasKey("limitType")) limitType = ApiUtils.getWireTypeFromNBT(root, "limitType");
        } catch (Exception e) {
            Logger.error("MTEConnector encountered MASSIVE error reading NBT. You shoudl probably report this.");
        }
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        root.setBoolean("needsVisualUpdate", needsVisualUpdate);
        try {
            if (limitType != null) root.setString("limitType", limitType.getUniqueName());

            if (this.worldObj != null) {
                root.setIntArray("prevPos", new int[]{this.worldObj.provider.dimensionId, xCoord, yCoord, zCoord});
            }
        } catch (Exception e) {
            Logger.error("MTEConnector encountered MASSIVE error writing NBT. You shoudl probably report this.");
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        super.onNeighborBlockChange(world, x, y, z, neighborBlock);
        if (world.isRemote) return; // Chỉ xử lý trên server

        ForgeDirection fd = ForgeDirection.getOrientation(host.getFacing())
            .getOpposite();
        int nx = x + fd.offsetX;
        int ny = y + fd.offsetY;
        int nz = z + fd.offsetZ;

        if (world.isAirBlock(nx, ny, nz)) {
            world.removeTileEntity(x, y, z);
            Block self = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            self.dropBlockAsItem(world, x, y, z, meta, 0);
            world.setBlockToAir(x, y, z);
        }
    }

}
