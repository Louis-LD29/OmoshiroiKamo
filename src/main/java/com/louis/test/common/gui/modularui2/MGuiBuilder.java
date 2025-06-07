package com.louis.test.common.gui.modularui2;

import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.louis.test.api.enums.IoMode;
import com.louis.test.common.block.machine.AbstractMachineEntity;

public class MGuiBuilder {

    private final AbstractMachineEntity te;
    private final PosGuiData posGuiData;
    private final PanelSyncManager syncManager;
    private final UISettings uiSettings;

    private int width = 176;
    private int height = 166;
    private boolean doesBindPlayerInventory = true;
    private boolean doesAddConfig = true;

    public MGuiBuilder(AbstractMachineEntity te, PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        this.te = te;
        this.posGuiData = data;
        this.syncManager = syncManager;
        this.uiSettings = uiSettings;
    }

    public MGuiBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public MGuiBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public MGuiBuilder doesBindPlayerInventory(boolean doesBindPlayerInventory) {
        this.doesBindPlayerInventory = doesBindPlayerInventory;
        return this;
    }

    public MGuiBuilder doesAddConfig(boolean doesAddConfig) {
        this.doesAddConfig = doesAddConfig;
        return this;
    }

    public ModularPanel build() {
        ModularPanel panel = ModularPanel.defaultPanel(te.getMachineName())
            .size(width, height);
        if (doesBindPlayerInventory) {
            panel.bindPlayerInventory();
        }
        if (doesAddConfig) {
            panel.child(createConfig());
        }
        syncManager.addCloseListener($ -> te.markDirty());
        return panel;
    }

    private IWidget createConfig() {
        IPanelHandler panelSyncHandler = syncManager.panel("other_panel", this::configPanel, true);
        Flow column = Flow.column()
            .debugName("Settings");

        column.coverChildren()
            .leftRel(0.96f)
            .topRel(0.05f)
            .childPadding(2)
            .padding(1)
            .child(
                new ToggleButton().size(16, 16)
                    .overlay(true, MGuiTextures.BUTTON_REDSTONE_ON)
                    .overlay(false, MGuiTextures.BUTTON_REDSTONE_OFF)
                    .selectedBackground(GuiTextures.MC_BUTTON)
                    .selectedHoverBackground(GuiTextures.MC_BUTTON_HOVERED)
                    .tooltip(richTooltip -> {
                        richTooltip.showUpTimer(2);
                        richTooltip.addLine(IKey.str("Redstone Mode"));
                    }))
            .child(
                new ButtonWidget<>().size(16, 16)
                    .overlay(GuiTextures.GEAR)
                    .tooltip(richTooltip -> {
                        richTooltip.showUpTimer(2);
                        richTooltip.addLine(IKey.str("Configure"));
                    })
                    .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            te.clearAllIoModes();
                        }
                    }))
                    .onMousePressed(mouseButton -> {
                        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            if (panelSyncHandler.isPanelOpen()) {
                                panelSyncHandler.closePanel();
                            } else {
                                panelSyncHandler.openPanel();
                            }
                            return true;
                        }
                        return false;
                    }));
        return column;
    }

    public ModularPanel configPanel(PanelSyncManager syncManager, IPanelHandler syncHandler) {
        ModularPanel panel = new Dialog<>("second_window", null).setDisablePanelsBelow(false)
            .setCloseOnOutOfBoundsClick(false)
            .setDraggable(true)
            .size(64, 64);

        Flow column = new Column().debugName("Side Configs")
            .padding(5)
            .coverChildren();

        SlotGroupWidget.Builder group = SlotGroupWidget.builder()
            .matrix(" U ", "ENW", " DS");

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            char key = dir.name()
                .charAt(0); // U, D, N, S, E, W
            group.key(
                key,
                index -> new ButtonWidget<>().syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        te.setIoMode(dir, IoMode.NONE);
                    } else {
                        te.toggleIoModeForFace(dir);
                    }
                }))
                    .overlay(
                        IKey.dynamic(
                            () -> te.getIoMode(dir)
                                .getUnlocalisedName())));
        }

        column.child(group.build());
        panel.child(column);
        panel.child(ButtonWidget.panelCloseButton());
        return panel;
    }

}
