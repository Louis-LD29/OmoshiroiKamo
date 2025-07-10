package blusunrize.immersiveengineering.client.models;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.model.obj.WavefrontObject;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public abstract class ModelIEObj {

    public static ArrayList<ModelIEObj> existingStaticRenders = new ArrayList();

    public final String path;
    public WavefrontObject model;

    public ModelIEObj(String path) {
        this.path = path;
        this.model = ClientUtils.getModel(path);

        existingStaticRenders.add(this);
    }

    public WavefrontObject rebindModel() {
        model = ClientUtils.getModel(path);
        return model;
    }

    public void render(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix,
        int offsetLighting, boolean invertFaces, String... renderedParts) {
        ClientUtils.renderStaticWavefrontModel(
            tile,
            model,
            tes,
            translationMatrix,
            rotationMatrix,
            offsetLighting,
            invertFaces,
            renderedParts);
    }

    public abstract IIcon getBlockIcon(String groupName);
}
