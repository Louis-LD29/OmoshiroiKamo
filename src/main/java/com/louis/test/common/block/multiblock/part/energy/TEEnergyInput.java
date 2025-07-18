package com.louis.test.common.block.multiblock.part.energy;

import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.louis.test.api.energy.EnergyStorageAdv;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.material.MaterialRegistry;

public class TEEnergyInput extends TEEnergyInOut {

    protected TEEnergyInput(int meta) {
        this.meta = meta;
        material = MaterialRegistry.fromMeta(meta % 100);
        energyStorage = new EnergyStorageAdv(material);
    }

    public TEEnergyInput() {
        this(0);
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
        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return super.receiveEnergy(from, maxReceive, simulate);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }
}
