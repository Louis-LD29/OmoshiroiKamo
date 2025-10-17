package ruiseki.omoshiroikamo.client.render.block.connectable;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import ruiseki.omoshiroikamo.client.models.ModelIEObj;
import ruiseki.omoshiroikamo.client.render.AbstractMTESR;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import ruiseki.omoshiroikamo.common.block.energyConnector.TEConnectorHV;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;
import ruiseki.omoshiroikamo.plugin.chickenbones.Matrix4;

public class ConnectorHVTESR extends AbstractMTESR {

    ModelIEObj modelConnectorHV = new ModelIEObj(LibResources.PREFIX_MODEL + "connectorHV.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockConnectable.getIcon(0, BlockConnectable.META_connectorHV);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TEConnectorHV te)) {
            return;
        }

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

        modelConnectorHV.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.25f, 0.5f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);

        modelConnectorHV.renderItem();

        GL11.glPopMatrix();
    }

}
