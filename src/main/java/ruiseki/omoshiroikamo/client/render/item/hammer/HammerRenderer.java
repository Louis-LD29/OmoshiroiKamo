package ruiseki.omoshiroikamo.client.render.item.hammer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.RenderUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

@SideOnly(Side.CLIENT)
public class HammerRenderer implements IItemRenderer {

    public static final IModelCustom model = AdvancedModelLoader
        .loadModel(new ResourceLocation(LibResources.PREFIX_MODEL + "hammer.obj"));

    private static final ResourceLocation Rod = new ResourceLocation(LibResources.PREFIX_ITEM + "hammerRod.png");
    private static final ResourceLocation Head = new ResourceLocation(LibResources.PREFIX_ITEM + "hammerHead.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glScalef(1.75f, 1.75f, 1.75f);
            GL11.glTranslatef(0.3f, 0f, 0.5f);
            GL11.glRotatef(-30f, 0f, 1f, 0f);
        } else if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(1.25f, 1.25f, 1.25f);
            GL11.glTranslatef(0.5f, 0f, 0.5f);
            GL11.glRotatef(-80f, 0F, 1f, 0f);
        } else if (type == ItemRenderType.EQUIPPED) {
            GL11.glScalef(2f, 2f, 2f);
            GL11.glTranslatef(0.5F, 0.5F, 1F);
        }

        RenderUtil.bindTexture(Rod);
        model.renderOnly("rod");

        RenderUtil.bindTexture(Head);
        model.renderOnly("head");

        GL11.glPopMatrix();
    }
}
