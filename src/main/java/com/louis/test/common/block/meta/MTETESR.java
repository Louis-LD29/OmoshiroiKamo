package com.louis.test.common.block.meta;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.louis.test.api.enums.MetaTileEntity;
import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.plugin.compat.IE.ModelIEObj;
import com.louis.test.core.lib.LibResources;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;

public class MTETESR extends AbstractMTESR {

    ModelIEObj modelInsulator = new ModelIEObj(LibResources.PREFIX_MODEL + "insulator.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockMeta.getIcon(0, MetaTileEntity.INSULATOR.getBaseMeta());
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        translationMatrix.translate(.5, .5, .5);
        TEMeta teMeta = (TEMeta) tile;
        switch (teMeta.facing) {
            case 0:
                break;
            case 1:
                rotationMatrix.rotate(Math.toRadians(180), 0, 0, 1);
                break;
            case 2:
                rotationMatrix.rotate(Math.toRadians(90), 1, 0, 0);
                break;
            case 3:
                rotationMatrix.rotate(Math.toRadians(-90), 1, 0, 0);
                break;
            case 4:
                rotationMatrix.rotate(Math.toRadians(-90), 0, 0, 1);
                break;
            case 5:
                rotationMatrix.rotate(Math.toRadians(90), 0, 0, 1);
                break;
            default:
                // facing unknown: skip rendering
                return;
        }
        if (teMeta.getMeta() == MetaTileEntity.INSULATOR.getBaseMeta()) {
            modelInsulator.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
        }
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        TEMeta teMeta = (TEMeta) tile;
        GL11.glPushMatrix();
        if (teMeta.getMeta() == MetaTileEntity.INSULATOR.getBaseMeta()) {
            GL11.glTranslatef(0.5f, 0.75f, 0.5f);
            GL11.glRotatef(180f, 1f, 0f, 0f);
            GL11.glScalef(1.5f, 1.5f, 1.5f);
            modelInsulator.renderItem();
        }
        GL11.glPopMatrix();
    }

}
