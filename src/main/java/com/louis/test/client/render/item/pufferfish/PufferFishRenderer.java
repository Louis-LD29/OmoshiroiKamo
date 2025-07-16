package com.louis.test.client.render.item.pufferfish;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.RenderUtil;
import com.louis.test.client.model.ModelIEObj;
import com.louis.test.common.core.lib.LibResources;

public class PufferFishRenderer implements IItemRenderer {

    ModelIEObj modelPufferFish = new ModelIEObj(LibResources.PREFIX_MODEL + "pufferfish.obj") {

        @Override
        public IIcon getBlockIcon(String groupName) {
            return null;
        }
    };

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItem() == Items.fish && item.getItemDamage() == 3;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() == Items.fish && item.getItemDamage() == 3) {
            GL11.glPushMatrix();

            long currentTime = System.currentTimeMillis();
            float time = (currentTime % 4000L) / 4000.0f;
            float breathScale = 1.15f + 0.05f * MathHelper.sin(time * (float) Math.PI * 2);

            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glScalef(breathScale, breathScale, breathScale);
                GL11.glTranslatef(0.3F, 0.5F, 0.3F);
                GL11.glRotatef(90F, 0F, 1F, 0F);
                GL11.glRotatef(15F, 1F, 0F, 0F);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslatef(0.5F, 0.05F, 0.5F);
                GL11.glRotatef(175F, 0F, 1F, 0F);
            } else if (type == ItemRenderType.EQUIPPED) {
                GL11.glTranslatef(0.5F, 0.5F, 1F);
                GL11.glRotatef(175F, 0F, 1F, 0F);
                GL11.glRotatef(55F, 1F, 0F, 0F);
            }

            GL11.glScalef(1.2f, 1.2f, 1.2f);
            RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/pufferfish.png"));
            modelPufferFish.model.renderAllExcept("open", "close");

            // === Kiểm tra nếu player đang sneaking ===
            EntityPlayer player = null;

            if (data != null) {
                for (Object o : data) {
                    if (o instanceof EntityPlayer) {
                        player = (EntityPlayer) o;
                        break;
                    }
                }
            }

            if (player != null && player.isSneaking()) {
                float mouthAnim = MathHelper.sin((currentTime % 2000L) / 2000.0f * (float) Math.PI * 2);

                if (mouthAnim > 0.3f) {
                    modelPufferFish.model.renderPart("open");
                } else if (mouthAnim < -0.3f) {
                    modelPufferFish.model.renderPart("close");
                }
            } else {
                modelPufferFish.model.renderPart("close");
            }

            GL11.glPopMatrix();
        }
    }

}
