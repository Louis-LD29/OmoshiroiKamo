package com.louis.test.client.gui.modularui2;

import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.drawable.UITexture;
import com.louis.test.common.core.lib.LibResources;

public class MUITexture {

    private static final ResourceLocation ICONS_LOCATION = new ResourceLocation(LibResources.GUI_ICONS);

    // only for usage in GuiTextures
    static UITexture icon(String name, int x, int y, int w, int h) {
        return UITexture.builder()
            .location(ICONS_LOCATION)
            .imageSize(256, 256)
            .uv(x, y, w, h)
            .name(name)
            .build();
    }

    static UITexture icon(String name, int x, int y) {
        return icon(name, x, y, 16, 16);
    }
}
