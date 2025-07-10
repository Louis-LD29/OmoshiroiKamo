package com.louis.test.common.plugin.compat.IE;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

import net.minecraft.client.renderer.texture.TextureMap;

import com.enderio.core.client.render.RenderUtil;

public abstract class ModelIEObj extends blusunrize.immersiveengineering.client.models.ModelIEObj {

    public ModelIEObj(String path) {
        super(path);
    }

    public void renderItem() {
        RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
        model.renderAll();

    }
}
