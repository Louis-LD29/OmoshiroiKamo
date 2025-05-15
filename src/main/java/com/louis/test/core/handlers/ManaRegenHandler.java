package com.louis.test.core.handlers;

import com.louis.test.mana.ManaNetworkHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class ManaRegenHandler {

    private int cachedMaxMana = -1;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.END) return;

        EntityPlayer  player = event.player;
        UUID uuid = player.getUniqueID();

        float current = ManaNetworkHandler.getCurrentMana(uuid);
        int max = getCachedMaxMana();

        if (current >= max || max <= 0) return;

        float regenRate = getRegenRate(player);
        float regenThisTick = regenRate / 20f;
        float newMana = current + regenThisTick;
        if (newMana > max) {
            newMana = max;
        }

        if (newMana != current) {
            ManaNetworkHandler.setCurrentMana(uuid, newMana);
        }
    }

    private float getRegenRate(EntityPlayer player) {
        int food = player.getFoodStats().getFoodLevel();
        boolean sprinting = player.isSprinting();

        if (food == 20) {
            return sprinting ? 50.0f : 60.0f; // no và chạy → 5 | no và đứng → 6
        }

        return sprinting ? 40.0f : 50.0f;
    }

    private int getCachedMaxMana() {
        if (cachedMaxMana < 0 || System.currentTimeMillis() % 5000 < 50) {
            cachedMaxMana = ManaNetworkHandler.getMaxMana();
        }
        return cachedMaxMana;
    }

}
