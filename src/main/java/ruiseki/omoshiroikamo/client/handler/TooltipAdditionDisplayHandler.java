package ruiseki.omoshiroikamo.client.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.ReflectionHelper;
import ruiseki.omoshiroikamo.common.util.lib.LibObfuscation;
import vazkii.botania.api.mana.IManaTooltipDisplay;

@Optional.Interface(iface = "vazkii.botania.api.mana.IManaTooltipDisplay", modid = "Botania")
public class TooltipAdditionDisplayHandler {

    @Optional.Method(modid = "Botania")
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
                    if (stack.getItem() instanceof IManaTooltipDisplay display) {}
                }
            }
        }
    }

}
