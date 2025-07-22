package louis.omoshiroikamo.client.render.block.anvil;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import louis.omoshiroikamo.client.model.ModelIEObj;
import louis.omoshiroikamo.client.render.AbstractMTESR;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.anvil.TEAnvil;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class AnvilTESR extends AbstractMTESR {

    ModelIEObj modelAnvil = new ModelIEObj(LibResources.PREFIX_MODEL + "anvil.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return ModBlocks.blockAnvil.getIcon(0, 0);
        }
    };

    @Override
    public void renderDynamic(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TEAnvil te)) return;

        ItemStack stack = te.getStackInSlot(0);
        if (stack == null) return;

        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 6f / 16f, z + 0.5);

        // Nếu không phải block thì quay nằm xuống
        if (!(stack.getItem() instanceof ItemBlock)) {
            GL11.glRotatef(90f, 1f, 0f, 0f);
            GL11.glTranslatef(0f, -0.25f, -0.1f);
        }

        RenderItem renderItem = (RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class);
        EntityItem ghostItem = new EntityItem(tile.getWorldObj(), 0, 0, 0, stack);
        ghostItem.hoverStart = 0f;

        renderItem.doRender(ghostItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix();
    }

    @Override
    public void renderStatic(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        if (!(tile instanceof TEAnvil te)) return;
        translationMatrix.translate(.5, 0, .5);
        modelAnvil.render(tile, tes, translationMatrix, rotationMatrix, 1, false);
    }

    @Override
    public void renderItem(TileEntity tile, Tessellator tes, Matrix4 translationMatrix, Matrix4 rotationMatrix) {
        GL11.glPushMatrix();
        modelAnvil.renderItem();
        GL11.glPopMatrix();
    }

}
