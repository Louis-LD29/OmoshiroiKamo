package louis.omoshiroikamo.client.render.block.solarArray;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import louis.omoshiroikamo.client.models.ModelIEObj;
import louis.omoshiroikamo.client.render.AbstractMTESR;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.multiblock.solarArray.TESolarCell;
import louis.omoshiroikamo.common.util.lib.LibResources;
import louis.omoshiroikamo.plugin.chickenbones.Matrix4;

public class SolarCellTESR extends AbstractMTESR {

    ModelIEObj modelSolarCell = new ModelIEObj(LibResources.PREFIX_MODEL + "solar_cell.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockSolarCell.getIcon(0, 0);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float partialTicks) {}

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TESolarCell te)) {
            return;
        }
        translationMatrix.translate(.5, 0, .5);
        modelSolarCell.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        modelSolarCell.renderItem();
        GL11.glPopMatrix();
    }

}
