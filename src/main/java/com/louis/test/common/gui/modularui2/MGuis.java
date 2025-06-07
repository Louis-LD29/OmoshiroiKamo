package com.louis.test.common.gui.modularui2;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.louis.test.common.block.machine.AbstractMachineEntity;

public class MGuis {

    public static MGuiBuilder mteTemplatePanelBuilder(AbstractMachineEntity te, PosGuiData data,
        PanelSyncManager syncManager, UISettings uiSettings) {
        return new MGuiBuilder(te, data, syncManager, uiSettings);
    }

    public static ModularPanel createPopUpPanel(@NotNull String name) {
        return createPopUpPanel(name, false, false);
    }

    public static ModularPanel createPopUpPanel(@NotNull String name, boolean disablePanelsBelow,
        boolean closeOnOutOfBoundsClick) {
        return new MPopUpPanel(name, disablePanelsBelow, closeOnOutOfBoundsClick);
    }
}
