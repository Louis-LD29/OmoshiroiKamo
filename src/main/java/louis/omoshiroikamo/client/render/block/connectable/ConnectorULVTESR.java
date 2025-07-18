package louis.omoshiroikamo.client.render.block.connectable;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import louis.omoshiroikamo.client.model.ModelIEObj;
import louis.omoshiroikamo.client.render.AbstractMTESR;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorULV;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class ConnectorULVTESR extends AbstractMTESR {

    ModelIEObj modelConnectorULV = new ModelIEObj(LibResources.PREFIX_MODEL + "connectorULV.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockConnectable.getIcon(0, BlockConnectable.META_connectorULV);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TEConnectorULV te)) return;

        translationMatrix.translate(0.5, 0.5, 0.5);

        ForgeDirection fd = ForgeDirection.getOrientation(te.getFacing());

        switch (fd) {
            case DOWN:
                rotationMatrix.rotate(Math.toRadians(180), 0, 0, 1);
                translationMatrix.translate(0, 0.5, 0);
                break;
            case UP:
                translationMatrix.translate(0, -0.5, 0);
                break;
            case NORTH:
                translationMatrix.translate(0, 0, 0.5);
                rotationMatrix.rotate(Math.toRadians(-90), 1, 0, 0);
                break;
            case SOUTH:
                translationMatrix.translate(0, 0, -0.5);
                rotationMatrix.rotate(Math.toRadians(90), 1, 0, 0);
                break;
            case EAST:
                translationMatrix.translate(-0.5, 0, 0);
                rotationMatrix.rotate(Math.toRadians(-90), 0, 0, 1);
                break;
            case WEST:
                translationMatrix.translate(0.5, 0, 0);
                rotationMatrix.rotate(Math.toRadians(90), 0, 0, 1);
                break;
            default:
                return;
        }

        modelConnectorULV.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.25f, 0.5f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);

        modelConnectorULV.renderItem();

        GL11.glPopMatrix();
    }

}
