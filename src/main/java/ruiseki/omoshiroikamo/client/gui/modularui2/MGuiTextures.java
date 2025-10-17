package ruiseki.omoshiroikamo.client.gui.modularui2;

import static ruiseki.omoshiroikamo.client.gui.modularui2.MUITexture.icon;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.drawable.ColorType;
import com.cleanroommc.modularui.drawable.UITexture;

import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public interface MGuiTextures {

    UITexture PULL = icon("pull", 0, 0);
    UITexture PUSH = icon("push", 16, 0);
    UITexture DISABLED = icon("disable", 32, 0);
    UITexture PULLNPUSH = icon("pullnpush", 48, 0);
    UITexture NONE = icon("none", 64, 0);

    UITexture BUTTON_REDSTONE_ON = UITexture.builder()
        .location(LibResources.OVERLAY_BUTTON_REDSTONE_ON)
        .imageSize(32, 32)
        .build();

    UITexture BUTTON_REDSTONE_OFF = UITexture.builder()
        .location(LibResources.OVERLAY_BUTTON_REDSTONE_OFF)
        .imageSize(32, 32)
        .build();

    UITexture WHITELIST = UITexture.builder()
        .location(LibResources.OVERLAY_WHITELIST)
        .imageSize(16, 16)
        .build();

    UITexture BLACKLIST = UITexture.builder()
        .location(LibResources.OVERLAY_BLACKLIST)
        .imageSize(16, 16)
        .build();

    UITexture FULL_HUNGER = UITexture.builder()
        .location(LibResources.OVERLAY_FULL_HUNGER)
        .imageSize(16, 16)
        .build();

    UITexture EXACT_HUNGER = UITexture.builder()
        .location(LibResources.OVERLAY_EXACT_HUNGER)
        .imageSize(16, 16)
        .build();

    UITexture PRIORITY_LEFT = UITexture.builder()
        .location(LibResources.OVERLAY_PRIORITY_LEFT)
        .imageSize(16, 16)
        .build();

    UITexture PRIORITY_DOWN = UITexture.builder()
        .location(LibResources.OVERLAY_PRIORITY_DOWN)
        .imageSize(16, 16)
        .build();

    UITexture CYCLE_IOMODE = UITexture.builder()
        .location(LibResources.CYCLE_IOMODE)
        .imageSize(16, 80)
        .build();

    UITexture PROGRESS_BURN = UITexture.builder()
        .location(LibResources.PROGRESS_BURN)
        .imageSize(18, 36)
        .build();

    UITexture TAB_LEFT = UITexture.fullImage(ModularUI.ID, "gui/tab/tabs_left", ColorType.DEFAULT);
    UITexture TAB_RIGHT = UITexture.fullImage(ModularUI.ID, "gui/tab/tabs_right", ColorType.DEFAULT);
}
