package com.louis.test.core.handlers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.louis.test.core.lib.LibObfuscation;

import cpw.mods.fml.relauncher.ReflectionHelper;
import vazkii.botania.api.mana.IManaTooltipDisplay;

public class TooltipAdditionDisplayHandler {

    public static void render() {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;

        if (gui != null && gui instanceof GuiContainer
            && mc.thePlayer != null
            && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Slot slot = ReflectionHelper.getPrivateValue(GuiContainer.class, container, LibObfuscation.THE_SLOT);
            if (slot != null && slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                if (stack != null) {
                    ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                    FontRenderer font = mc.fontRenderer;
                    int mouseX = Mouse.getX() * res.getScaledWidth() / mc.displayWidth;
                    int mouseY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight;

                    List<String> tooltip;
                    try {
                        tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                    } catch (Exception e) {
                        tooltip = new ArrayList<>();
                    }
                    int width = tooltip.stream()
                        .mapToInt(font::getStringWidth)
                        .max()
                        .orElse(0) + 2;
                    int tooltipHeight = (tooltip.size() - 1) * 10 + 5;

                    int offx = 11;
                    int offy = 17;

                    boolean offscreen = mouseX + width + 19 >= res.getScaledWidth();
                    if (offscreen) {
                        offx = -13 - width;
                    }

                    int fixY = res.getScaledHeight() - (mouseY + tooltipHeight);
                    if (fixY < 0) {
                        offy -= fixY;
                    }
                    if (stack.getItem() instanceof IManaTooltipDisplay display)
                        drawManaBar(stack, display, mouseX, mouseY, offx, offy, tooltipHeight);
                }
            }
        }
    }

    private static void drawManaBar(ItemStack stack, IManaTooltipDisplay display, int mouseX, int mouseY, int offx,
        int offy, int tooltipHeight) {
        float fraction = display.getManaFractionForDisplay(stack);
        int manaBarHeight = (int) Math.ceil(tooltipHeight * fraction); // Chiều cao thanh mana tương ứng với tỷ lệ mana
        int manaBarWidth = 2; // Độ rộng thanh mana

        // Vị trí thanh mana (ở bên trái tooltip)
        int barX = mouseX + offx - manaBarWidth - 1;
        int barY = mouseY - offy + 1; // Đỉnh của thanh mana bắt đầu từ đỉnh tooltip

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Nền thanh mana (viền đen)
        Gui.drawRect(barX - 1, barY - 1, barX + manaBarWidth + 1, barY + tooltipHeight + 1, 0xFF000000);

        // Phần đầy mana từ đỉnh tới đáy tooltip
        Gui.drawRect(
            barX,
            barY + tooltipHeight - manaBarHeight,
            barX + manaBarWidth,
            barY + tooltipHeight,
            Color.HSBtoRGB(
                0.75F,
                0.2F + ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F)
                    * 0.15F,
                1F));

        // Phần trống còn lại
        Gui.drawRect(barX, barY, barX + manaBarWidth, barY + tooltipHeight - manaBarHeight, 0xFF555555);
    }
}
