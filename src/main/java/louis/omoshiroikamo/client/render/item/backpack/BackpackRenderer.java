package louis.omoshiroikamo.client.render.item.backpack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.RenderUtil;

import louis.omoshiroikamo.client.models.ModelIEObj;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BackpackRenderer implements IItemRenderer {

    ModelIEObj modelBackpack = new ModelIEObj(LibResources.PREFIX_MODEL + "backpack_base.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return null;
        }
    };

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
            GL11.glTranslatef(0.3f, 0f, 0.3f);
            GL11.glRotatef(90f, 0f, 1f, 0f);
        } else if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(1.25f, 1.25f, 1.25f);
            GL11.glTranslatef(0.5f, 0f, 0.5f);
            GL11.glRotatef(-80f, 0F, 1f, 0f);
        } else if (type == ItemRenderType.EQUIPPED) {
            // GL11.glScalef(2f, 2f, 2f);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/backpack_border.png"));
        modelBackpack.model.renderOnly("trim1", "trim2", "trim3", "trim4", "trim5", "padding1");

        RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/backpack_cloth.png"));
        modelBackpack.model.renderOnly(
            "inner1",
            "inner2",
            "outer1",
            "outer2",
            "left_trim1",
            "right_trim1",
            "bottom_trim1",
            "body",
            "pouch1",
            "pouch2",
            "top1",
            "top2",
            "top3",
            "bottom1",
            "bottom2",
            "bottom3",
            "lip1");

        switch (item.getItemDamage()) {
            case 1:
                RenderUtil
                    .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/copper_clips.png"));
                break;
            case 2:
                RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/iron_clips.png"));
                break;
            case 3:
                RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/gold_clips.png"));
                break;
            case 4:
                RenderUtil
                    .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/diamond_clips.png"));
                break;
            case 5:
                RenderUtil
                    .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/netherite_clips.png"));
                break;
            default:
                RenderUtil
                    .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/leather_clips.png"));
                break;
        }

        modelBackpack.model.renderOnly(
            "top4",
            "right1",
            "right2",
            "right_clip1",
            "right_clip2",
            "left1",
            "left2",
            "left_clip1",
            "left_clip2",
            "clip1",
            "clip2",
            "clip3",
            "clip4",
            "opening1",
            "opening2",
            "opening3",
            "opening4",
            "opening5");

        GL11.glPopMatrix();
    }
}
