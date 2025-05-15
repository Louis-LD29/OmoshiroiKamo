package com.louis.test.core.handlers;

import baubles.common.lib.PlayerHandler;
import com.louis.test.core.interfaces.IManaItem;
import com.louis.test.mana.ManaNetworkHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class ClientTickHandler {
    public static int ticksWithLexicaOpen = 0;
    public static int pageFlipTicks = 0;
    public static int ticksInGame = 0;
    public static float partialTicks = 0.0F;
    public static float delta = 0.0F;
    public static float total = 0.0F;

    // MỚI: giá trị mana đang hiển thị, khởi = max mana (hoặc 0)
    public static float displayedMana = 0.0F;
    public static float displayedCMana = 0.0F;

    public ClientTickHandler() {
    }

    private void calcDelta() {
        float oldTotal = total;
        total = (float)ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        } else {
            updateDisplayedMana();

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
        float smoothingRate = 2.0F;                // độ “nhanh” của nội suy
        float dt = delta / 20.0F;                  // delta được tính theo ticks, mỗi tick = 1/20s
        float alpha = 1 - (float) Math.exp(-smoothingRate * dt);

        // Nội suy: tiến gần targetMana
        displayedMana += (targetMana - displayedMana) * alpha;
    }

}
