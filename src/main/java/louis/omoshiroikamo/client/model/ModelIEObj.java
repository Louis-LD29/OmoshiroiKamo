package louis.omoshiroikamo.client.model;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import com.enderio.core.client.render.RenderUtil;

public abstract class ModelIEObj extends blusunrize.immersiveengineering.client.models.ModelIEObj {

    private static ResourceLocation texture;

    public ModelIEObj(String path) {
        super(path);
    }

    public void setTexture(ResourceLocation tex) {
        this.texture = tex;
    }

    public void renderItem() {
        if (texture != null) {
            RenderUtil.bindTexture(texture);
        } else {
            RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
        }
        model.renderAll();
    }

}
