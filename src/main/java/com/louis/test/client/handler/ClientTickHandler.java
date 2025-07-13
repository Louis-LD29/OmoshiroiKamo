package com.louis.test.client.handler;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.louis.test.api.mana.IManaItem;
import com.louis.test.common.core.helper.Helper;
import com.louis.test.common.mana.ManaNetworkHandler;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientTickHandler {

    public static final ClientTickHandler instance = new ClientTickHandler();

    public static int pageFlipTicks = 0;
    public static int ticksInGame = 0;
    public static float partialTicks = 0.0F;
    public static float delta = 0.0F;
    public static float total = 0.0F;
    public static float displayedMana = 0.0F;
    public static float displayedCMana = 0.0F;

    public ClientTickHandler() {}

    private void calcDelta() {
        float oldTotal = total;
        total = (float) ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        } else {
            updateDisplayedMana();
            updateDisplayedCMana();
            TooltipAdditionDisplayHandler.render();
            this.calcDelta();
        }

    }

    @SubscribeEvent
    public void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ticksInGame++;
                partialTicks = 0.0F;
            }
            this.calcDelta();
        }
    }

    public static void notifyPageChange() {
        if (pageFlipTicks == 0) {
            pageFlipTicks = 5;
        }
    }

    private void updateDisplayedMana() {
        // Lấy mana thực tế
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        UUID uuid = mc.thePlayer.getUniqueID();
        float targetMana = ManaNetworkHandler.getCurrentMana(uuid);
        float maxMana = ManaNetworkHandler.getMaxMana();
        if (maxMana <= 0) {
            displayedMana = 0;
            return;
        }

        // 2 tham số: tốc độ (rate) và delta (giây)
        float smoothingRate = 2.0F; // độ “nhanh” của nội suy
        float dt = delta / 20.0F; // delta được tính theo ticks, mỗi tick = 1/20s
        float alpha = 1 - (float) Math.exp(-smoothingRate * dt);

        // Nội suy: tiến gần targetMana
        displayedMana += (targetMana - displayedMana) * alpha;
    }

    private void updateDisplayedCMana() {
        // Lấy mana thực tế
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        IInventory mainInv = player.inventory;
        IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

        ItemStack selectedItem = Helper.getItemWithHighestMana(mainInv, baublesInv);
        if (selectedItem == null) return;
        IManaItem manaItem = (IManaItem) selectedItem.getItem();
        float targetMana = manaItem.getMana(selectedItem);
        float maxMana = manaItem.getMaxMana(selectedItem);

        if (maxMana <= 0) {
            displayedCMana = 0;
            return;
        }

        // 2 tham số: tốc độ (rate) và delta (giây)
        float smoothingRate = 2.0F; // độ “nhanh” của nội suy
        float dt = delta / 20.0F; // delta được tính theo ticks, mỗi tick = 1/20s
        float alpha = 1 - (float) Math.exp(-smoothingRate * dt);

        // Nội suy: tiến gần targetMana
        displayedCMana += (targetMana - displayedCMana) * alpha;
    }

}
