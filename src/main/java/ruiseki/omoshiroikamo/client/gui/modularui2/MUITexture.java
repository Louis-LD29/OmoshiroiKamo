package ruiseki.omoshiroikamo.client.gui.modularui2;

import net.minecraft.util.ResourceLocation;

import com.cleanroommc.modularui.drawable.UITexture;

import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class MUITexture {

    private static final ResourceLocation ICONS_LOCATION = new ResourceLocation(LibResources.GUI_ICONS);

    // only for usage in GuiTextures
    static UITexture icon(String name, int x, int y, int w, int h) {
        float u = x / 256f;
        float v = y / 256f;
        float uw = w / 256f;
        float vh = h / 256f;

        return UITexture.builder()
            .location(ICONS_LOCATION)
            .imageSize(256, 256)
            .uv(u, v, uw, vh)
            .name(name)
            .build();
    }

    static UITexture icon(String name, int x, int y) {
        return icon(name, x, y, 16, 16);
    }
}
