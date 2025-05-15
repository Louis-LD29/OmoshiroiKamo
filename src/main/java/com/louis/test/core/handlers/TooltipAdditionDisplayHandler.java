package com.louis.test.core.handlers;

import cofh.api.energy.IEnergyContainerItem;
import com.louis.test.lib.LibObfuscation;
import com.louis.test.core.interfaces.IManaTooltipDisplay;
import cpw.mods.fml.relauncher.ReflectionHelper;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TooltipAdditionDisplayHandler {
    public static void render() {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;

        if(gui != null && gui instanceof GuiContainer && mc.thePlayer != null && mc.thePlayer.inventory.getItemStack() == null) {
            GuiContainer container = (GuiContainer) gui;
            Slot slot = ReflectionHelper.getPrivateValue(GuiContainer.class, container, LibObfuscation.THE_SLOT);
            if(slot != null && slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                if(stack != null) {
                    ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                    FontRenderer font = mc.fontRenderer;
                    int mouseX = Mouse.getX() * res.getScaledWidth() / mc.displayWidth;
                    int mouseY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / mc.displayHeight;

                    List<String> tooltip;
                    try {
                        tooltip = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                    } catch(Exception e) {
                        tooltip = new ArrayList<>();
                    }
                    int width = tooltip.stream().mapToInt(font::getStringWidth).max().orElse(0) + 2;
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

                    boolean hasManaBar = false;
                    if (stack.getItem() instanceof IManaTooltipDisplay) {
                        drawManaBar(stack, (IManaTooltipDisplay) stack.getItem(), mouseX, mouseY, offx, offy, tooltipHeight);
                        hasManaBar = true;
                    }

                    // Vẽ thanh năng lượng nếu item hỗ trợ IEnergyContainerItem
                    if (stack.getItem() instanceof IEnergyContainerItem)
                        drawEnergyBar(stack, (IEnergyContainerItem) stack.getItem(), mouseX, mouseY, offx - (hasManaBar ? 2 : 0), offy, tooltipHeight);
                }
            }
        }
    }

    private static void drawManaBar(ItemStack stack, IManaTooltipDisplay display, int mouseX, int mouseY, int offx, int offy, int tooltipHeight) {
        float fraction = display.getManaFractionForDisplay(stack);
        int manaBarHeight = (int) Math.ceil(tooltipHeight * fraction); // Chiều cao thanh mana tương ứng với tỷ lệ mana
        int manaBarWidth = 1; // Độ rộng thanh mana

        // Vị trí thanh mana (ở bên trái tooltip)
        int barX = mouseX + offx - manaBarWidth - 1;
        int barY = mouseY - offy + 1; // Đỉnh của thanh mana bắt đầu từ đỉnh tooltip

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Nền thanh mana (viền đen)
        Gui.drawRect(barX - 1, barY - 1, barX + manaBarWidth + 1, barY + tooltipHeight + 1, 0xFF000000);

        // Phần đầy mana từ đỉnh tới đáy tooltip
        Gui.drawRect(barX, barY + tooltipHeight - manaBarHeight, barX + manaBarWidth, barY + tooltipHeight,
            Color.HSBtoRGB(0.75F, 0.2F + ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F) * 0.15F, 1F));

        // Phần trống còn lại
        Gui.drawRect(barX, barY, barX + manaBarWidth, barY + tooltipHeight - manaBarHeight, 0xFF555555);
    }

    private static void drawEnergyBar(ItemStack stack, IEnergyContainerItem energyItem, int mouseX, int mouseY, int offx, int offy, int tooltipHeight) {
        if (energyItem.getMaxEnergyStored(stack) == 0) return;
        float energyFraction = energyItem.getEnergyStored(stack) / (float) energyItem.getMaxEnergyStored(stack);
        int energyBarHeight = (int) Math.ceil(tooltipHeight * energyFraction); // Chiều cao thanh năng lượng
        int energyBarWidth = 1; // Độ rộng thanh năng lượng

        // Vị trí thanh năng lượng (bên phải thanh mana)
        int barX = mouseX + offx - energyBarWidth - 1; // Vị trí x sau thanh mana
        int barY = mouseY - offy + 1; // Đỉnh của thanh năng lượng bắt đầu từ đỉnh tooltip

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Nền thanh năng lượng (viền đen)
        Gui.drawRect(barX - 1, barY - 1, barX + energyBarWidth + 1, barY + tooltipHeight + 1, 0xFF000000);

        // Phần đầy năng lượng từ đỉnh tới đáy tooltip
        Gui.drawRect(barX, barY + tooltipHeight - energyBarHeight, barX + energyBarWidth, barY + tooltipHeight,
            Color.HSBtoRGB(0.9F, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F) * 0.3F + 0.4F, 1F));

        // Phần trống còn lại
        Gui.drawRect(barX, barY, barX + energyBarWidth, barY + tooltipHeight - energyBarHeight, 0xFF555555);
    }
}
