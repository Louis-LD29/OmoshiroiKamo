package com.louis.test.common.block.multiblock.part.energy;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.louis.test.api.MaterialRegistry;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.energy.EnergyStorageAdv;

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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }
}
