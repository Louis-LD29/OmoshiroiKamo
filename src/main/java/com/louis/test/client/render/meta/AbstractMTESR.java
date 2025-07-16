package com.louis.test.client.render.meta;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

public abstract class AbstractMTESR extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        renderDynamic(tile, x, y, z, f);
    }

    public abstract void renderDynamic(TileEntity tile, double x, double y, double z, float f);

    public abstract void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix,
        Matrix4 rotationMatrix);

    public abstract void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix,
        Matrix4 rotationMatrix);
}
