package ruiseki.omoshiroikamo.client.render.block.connectable;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.client.models.ModelIEObj;
import ruiseki.omoshiroikamo.client.render.AbstractMTESR;
import ruiseki.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import ruiseki.omoshiroikamo.common.block.energyConnector.TEInsulator;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;
import ruiseki.omoshiroikamo.plugin.chickenbones.Matrix4;

@SideOnly(Side.CLIENT)
public class InsulatorTESR extends AbstractMTESR {

    ModelIEObj modelInsulator = new ModelIEObj(LibResources.PREFIX_MODEL + "insulator.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.CONNECTABLE.get()
                .getIcon(0, BlockConnectable.META_insulator);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float f) {

    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TEInsulator te)) {
            return;
        }
        translationMatrix.translate(.5, .5, .5);
        switch (te.getFacing()) {
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
                return;
        }
        modelInsulator.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.75f, 0.5f);
        GL11.glRotatef(180f, 1f, 0f, 0f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);

        modelInsulator.renderItem();

        GL11.glPopMatrix();
    }

}
