package com.louis.test.common.block.boiler.network;

import net.minecraft.world.World;

import com.louis.test.common.block.machine.AbstractMachineEntity;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.core.network.PacketHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TileBoilerNetwork extends AbstractMachineEntity {

    private int networkId = -1;
    private int idRequestTimer = 0;

    private BoilerType type;

    private IBoilerNetwork network;

    protected TileBoilerNetwork(SlotDefinition slotDefinition) {
        super(slotDefinition);
    }

    public BoilerType getType() {
        if (type == null) {
            type = BoilerType.getTypeFromMeta(getBlockMetadata());
        }
        return type;
    }

    @SideOnly(Side.CLIENT)
    public void setNetworkId(int networkId) {
        this.networkId = networkId;
        if (networkId != -1) {
            ClientNetworkManager.getInstance()
                .addToNetwork(networkId, this);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getNetworkId() {
        return networkId;
    }

    public IBoilerNetwork getNetwork() {
        return network;
    }

    public boolean setNetwork(IBoilerNetwork network) {
        this.network = network;
        return true;
    }

    public boolean canConnectTo(TileBoilerNetwork cap) {
        BoilerType t = getType();
        return t.isMultiblock() && t.getUid()
            .equals(
                cap.getType()
                    .getUid());
    }

    @Override
    public void onChunkUnload() {
        if (network != null) {
            network.destroyNetwork();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (network != null) {
            network.destroyNetwork();
        }
    }

    public void moveInventoryToNetwork() {
        if (network == null) {
            return;
        }
    }

    public void onBreakBlock() {
        // If we are holding the networks inventory when we care broken, tranfer it to another member of the network
        moveInventoryToNetwork();
    }

    @Override
    public void doUpdate() {
        if (worldObj.isRemote) {
            if (networkId == -1) {
                if (idRequestTimer <= 0) {
                    PacketHandler.INSTANCE.sendToServer(new PacketNetworkIdRequest(this));
                    idRequestTimer = 5;
                } else {
                    --idRequestTimer;
                }
            }
            return;
        }
        updateNetwork(worldObj);
        if (network == null) {
            return;
        }
    }

    private void updateNetwork(World world) {
        if (getNetwork() == null) {
            NetworkUtil.ensureValidNetwork(this);
        }
        if (getNetwork() != null) {
            getNetwork().onUpdateEntity(this);
        }
    }
}
