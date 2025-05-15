package com.louis.test.core.handlers;

import baubles.api.BaublesApi;
import com.louis.test.core.interfaces.IFlightEnablerItem;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class FlightHandler {
    public static final FlightHandler instance = new FlightHandler();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        boolean hasFlightItem = false;

        IInventory baubles = BaublesApi.getBaubles(player);
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack stack = baubles.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof IFlightEnablerItem) {
                hasFlightItem = true;
                break;
            }
        }

        if (hasFlightItem) {
            player.capabilities.allowFlying = true;
            if (!player.onGround) player.capabilities.isFlying = true;
            player.sendPlayerAbilities();
        }
        System.out.println("Login Flight-enabling item in slot: " + hasFlightItem);
    }
}
