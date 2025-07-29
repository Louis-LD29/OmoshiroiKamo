package louis.omoshiroikamo.client.models;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

import com.enderio.core.client.render.RenderUtil;

import louis.omoshiroikamo.client.ClientUtils;
import louis.omoshiroikamo.common.plugin.chickenbones.Matrix4;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is ModelIEObj
 */
public abstract class ModelIEObj {

    public static ArrayList<ModelIEObj> existingStaticRenders = new ArrayList();

    public final String path;
    public WavefrontObject model;
    private static ResourceLocation texture;

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
