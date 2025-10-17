package ruiseki.omoshiroikamo.common.util.handler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ruiseki.omoshiroikamo.api.enums.ElementType;
import ruiseki.omoshiroikamo.common.elemental.ElementUtil;

public class ElementalHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (entity.worldObj.isRemote) {
            return;
        }

        long worldTime = entity.worldObj.getTotalWorldTime();
        ElementUtil.tickElements(entity, worldTime);
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {
        EntityLivingBase target = event.entityLiving;
        DamageSource source = event.source;
        Entity direct = source.getSourceOfDamage();
        Entity indirect = source.getEntity();

        long worldTime = target.worldObj.getTotalWorldTime();

        if (direct instanceof EntityArrow && indirect instanceof EntityPlayer shooter) {
            ItemStack bow = shooter.getHeldItem();
            if (bow != null && EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bow) > 0) {
                ElementUtil.applyElement(target, ElementType.PYRO, worldTime);
            }
        }

        if (indirect instanceof EntityPlayer) {
            ItemStack weapon = ((EntityPlayer) indirect).getHeldItem();
            if (weapon != null && EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, weapon) > 0) {
                ElementUtil.applyElement(target, ElementType.PYRO, worldTime);
            }
        }

    }

}
