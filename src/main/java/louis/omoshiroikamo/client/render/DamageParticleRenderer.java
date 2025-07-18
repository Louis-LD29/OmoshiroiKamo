package louis.omoshiroikamo.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import louis.omoshiroikamo.common.config.Config;

/**
 * Inspired by or partially based on ToroHealth Damage Indicators
 *
 * <a href="https://www.curseforge.com/minecraft/mc-mods/torohealth-damage-indicators">...</a>
 * <p>
 * <p>
 * Licensed under GNU GENERAL PUBLIC LICENSE, Version 3 (GPLv3)
 * See: <a href="https://www.gnu.org/licenses/gpl-3.0.html">...</a>
 */
public class DamageParticleRenderer extends EntityFX {

    protected static final float GRAVITY = 0.1F;
    protected static final float SIZE = 3.0F;
    protected static final int LIFESPAN = 12;

    protected String text;
    protected boolean shouldOnTop = true;
    protected boolean grow = true;
    protected float scale = 1.0F;
    private int damage;

    public DamageParticleRenderer(int damage, World world, double x, double y, double z, double motionX, double motionY,
        double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.particleGravity = GRAVITY;
        this.particleScale = SIZE;
        this.particleMaxAge = LIFESPAN;
        this.damage = damage;
        this.text = Integer.toString(Math.abs(damage));
        this.noClip = true;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTicks, float rotX, float rotZ, float rotYZ,
        float rotXY, float rotXZ) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity viewer = mc.renderViewEntity;

        double x = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX;
        double y = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY;
        double z = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ;

        GL11.glPushMatrix();

        if (this.shouldOnTop) {
            GL11.glDepthFunc(GL11.GL_ALWAYS); // 519
        } else {
            GL11.glDepthFunc(GL11.GL_LEQUAL); // 515
        }

        GL11.glTranslated(x, y, z + 0.1);
        GL11.glRotatef(-viewer.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(viewer.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(
            -0.025F * this.particleScale * this.scale,
            -0.025F * this.particleScale * this.scale,
            0.025F * this.particleScale * this.scale);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int color = (damage < 0) ? Config.healColor : Config.damageColor;

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GL11.glColor4f(red, green, blue, 1.0F);

        FontRenderer fontRenderer = mc.fontRenderer;
        int width = fontRenderer.getStringWidth(this.text) / 2;
        fontRenderer.drawString(this.text, -width, 0, color);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();

        if (this.grow) {
            this.particleScale *= 1.08F;
            if (this.particleScale > SIZE * 3.0F) {
                this.grow = false;
            }
        } else {
            this.particleScale *= 0.96F;
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}
