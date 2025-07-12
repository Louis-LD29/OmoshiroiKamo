package com.louis.test.client.gui.modularui2;

import com.cleanroommc.modularui.drawable.UITexture;
import com.louis.test.common.core.lib.LibResources;

import static com.louis.test.client.gui.modularui2.MUITexture.icon;

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

    UITexture CYCLE_IOMODE = UITexture.builder()
        .location(LibResources.CYCLE_IOMODE)
        .imageSize(16, 80)
        .build();

    UITexture PROGRESS_BURN = UITexture.builder()
        .location(LibResources.PROGRESS_BURN)
        .imageSize(18, 36)
        .build();
}
