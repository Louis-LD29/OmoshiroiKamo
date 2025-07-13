package com.louis.test.common.block.multiblock.part.energy;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.energy.EnergyStorageAdv;
import com.louis.test.api.energy.PowerDistributor;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.material.MaterialRegistry;

public class TEEnergyOutput extends TEEnergyInOut {

    private PowerDistributor powerDis;

    protected TEEnergyOutput(int meta) {
        this.meta = meta;
        material = MaterialRegistry.fromMeta(meta % 100);
        energyStorage = new EnergyStorageAdv(material);
    }

    public TEEnergyOutput() {
        this(100);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInOut.unlocalisedName + "." + (meta >= 100 ? "output" : "input");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return transmitEnergy();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        super.onNeighborBlockChange(world, x, y, z, nbid);
        if (powerDis != null) {
            powerDis.neighboursChanged();
        }
    }

    private boolean transmitEnergy() {
        if (powerDis == null) {
            powerDis = new PowerDistributor(new BlockCoord(this));
        }
        int canTransmit = Math.min(getEnergyStored(), getMaxEnergyExtract());
        if (canTransmit <= 0) {
            return false;
        }
        int transmitted = powerDis.transmitEnergy(worldObj, canTransmit);
        setEnergyStored(getEnergyStored() - transmitted);
        return transmitted > 0;
    }

    @Override
    public void setCanReceivePower(boolean value) {
        super.setCanReceivePower(false);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }
}
