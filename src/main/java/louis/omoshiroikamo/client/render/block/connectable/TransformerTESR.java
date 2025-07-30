package louis.omoshiroikamo.client.render.block.connectable;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import louis.omoshiroikamo.client.models.ModelIEObj;
import louis.omoshiroikamo.client.render.AbstractMTESR;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.energyConnector.TETransformer;
import louis.omoshiroikamo.common.plugin.chickenbones.Matrix4;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class TransformerTESR extends AbstractMTESR {

    ModelIEObj modelTransformer = new ModelIEObj(LibResources.PREFIX_MODEL + "transformer.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockConnectable.getIcon(0, BlockConnectable.META_transformer);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TETransformer te)) return;
        translationMatrix.translate(.5, 0, .5);
        switch (te.getFacing()) {
            case 2: // NORTH
                break;
            case 3: // SOUTH
                rotationMatrix.rotate(Math.toRadians(180), 0, 1, 0);
                break;
            case 4: // WEST
                rotationMatrix.rotate(Math.toRadians(90), 0, 1, 0);
                break;
            case 5: // EAST
                rotationMatrix.rotate(Math.toRadians(-90), 0, 1, 0);
                break;
            default:
                return;
        }
        modelTransformer.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0f, 0.5f);

        GL11.glRotatef(180, 0, 1, 0);
        modelTransformer.renderItem();

        GL11.glPopMatrix();
    }

}
