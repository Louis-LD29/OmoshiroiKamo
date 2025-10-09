package louis.omoshiroikamo.client.render.item.backpack;

import java.awt.Color;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.ColorUtil;
import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.vecmath.Vector4f;

import cofh.api.energy.IEnergyContainerItem;
import louis.omoshiroikamo.client.handler.ClientTickHandler;
import louis.omoshiroikamo.client.models.ModelIEObj;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.util.lib.LibResources;
import louis.omoshiroikamo.config.item.ItemConfig;

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

        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glScalef(1.75f, 1.75f, 1.75f);
                GL11.glTranslatef(0.3f, 0f, 0.3f);
                GL11.glRotatef(90f, 0f, 1f, 0f);
                break;
            case INVENTORY:
                GL11.glScalef(1.25f, 1.25f, 1.25f);
                GL11.glTranslatef(0.5f, 0f, 0.5f);
                GL11.glRotatef(-80f, 0F, 1f, 0f);
                break;
            case EQUIPPED:
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                break;
            default:
                break;
        }
        renderModel(item);
        if (type == ItemRenderType.INVENTORY) {
            renderBars(item);
        }

        GL11.glPopMatrix();
    }

    private void renderModel(ItemStack item) {
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

        String material;
        switch (item.getItemDamage()) {
            case 1:
                material = "copper";
                break;
            case 2:
                material = "iron";
                break;
            case 3:
                material = "gold";
                break;
            case 4:
                material = "diamond";
                break;
            case 5:
                material = "netherite";
                break;
            default:
                material = "leather";
                break;
        }

        RenderUtil
            .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/" + material + "_clips.png"));
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
    }

    private void renderBars(ItemStack item) {
        if (EnergyUpgrade.loadFromItem(item) == null
            || (!ItemConfig.renderChargeBar && !ItemConfig.renderDurabilityBar)) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0f, 0f, -0.8f);
        GL11.glRotatef(-55, 0F, 1f, 0f);
        GL11.glScalef(1f / 12f, 1f / 12f, 1f / 12f);
        GL11.glDisable(GL11.GL_LIGHTING);

        boolean hasEnergyUpgrade = EnergyUpgrade.loadFromItem(item) != null;

        double maxDam, dispDamage;

        if (ItemConfig.renderChargeBar && hasEnergyUpgrade) {
            IEnergyContainerItem backpack = (IEnergyContainerItem) item.getItem();
            maxDam = backpack.getMaxEnergyStored(item);
            dispDamage = backpack.getEnergyStored(item);
            Color color = new Color(
                Color.HSBtoRGB(
                    0.9F,
                    ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F)
                        * 0.3F + 0.4F,
                    1F));

            renderBar2(0, maxDam, maxDam - dispDamage, color, color);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void renderBar2(int y, double maxDam, double dispDamage, Color full, Color empty) {
        double ratio = dispDamage / maxDam;
        Vector4f fg = ColorUtil.toFloat(full);
        Vector4f ec = ColorUtil.toFloat(empty);
        fg.interpolate(ec, (float) ratio);
        Vector4f bg = ColorUtil.toFloat(Color.black);
        bg.interpolate(fg, 0.15f);

        int barLength = (int) Math.round(12.0 * (1 - ratio));

        RenderUtil.renderQuad2D(2, y, 0, 12, 1, bg);
        RenderUtil.renderQuad2D(2 + (12 - barLength), y, 0, barLength, 1, fg);
    }
}
