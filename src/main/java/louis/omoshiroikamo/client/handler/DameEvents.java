package louis.omoshiroikamo.client.handler;

import static louis.omoshiroikamo.config.general.DamageIndicatorsConfig.indicatorsConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.client.render.DamageParticleRenderer;

/**
 * Inspired by or partially based on ToroHealth Damage Indicators
 *
 * <a href="https://www.curseforge.com/minecraft/mc-mods/torohealth-damage-indicators">...</a>
 * <p>
 * <p>
 * Licensed under GNU GENERAL PUBLIC LICENSE, Version 3 (GPLv3)
 * See: <a href="https://www.gnu.org/licenses/gpl-3.0.html">...</a>
 */
@EventBusSubscriber(side = Side.CLIENT)
public class DameEvents {

    @SubscribeEvent
    public static void displayDamage(LivingUpdateEvent event) {
        displayDamageDealt(event.entityLiving);
    }

    public static void displayDamageDealt(EntityLivingBase entity) {
        if (!entity.worldObj.isRemote) {
            return;
        }

        if (!indicatorsConfig.showDamageParticles) {
            return;
        }
        int currentHealth = (int) Math.ceil(entity.getHealth());
        NBTTagCompound data = entity.getEntityData();

        if (data.hasKey("health")) {
            int previousHealth = data.getInteger("health");
            if (previousHealth != currentHealth) {
                int delta = currentHealth - previousHealth;
                displayParticle(entity, -delta);
            }
        }

        data.setInteger("health", currentHealth);
    }

    private static void displayParticle(Entity entity, int damage) {
        if (damage == 0) {
            return;
        }

        World world = entity.worldObj;
        double motionX = world.rand.nextGaussian() * 0.02;
        double motionY = 0.5f;
        double motionZ = world.rand.nextGaussian() * 0.02;
        DamageParticleRenderer damageIndicator = new DamageParticleRenderer(
            damage,
            world,
            entity.posX,
            entity.posY + entity.height,
            entity.posZ,
            motionX,
            motionY,
            motionZ);
        Minecraft.getMinecraft().effectRenderer.addEffect(damageIndicator);

    }
}
