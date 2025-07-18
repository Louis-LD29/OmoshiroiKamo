package louis.omoshiroikamo.client.gui;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import baubles.common.lib.PlayerHandler;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import louis.omoshiroikamo.api.mana.IManaItem;
import louis.omoshiroikamo.client.handler.ClientTickHandler;
import louis.omoshiroikamo.common.core.helper.Helper;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.mana.ManaNetworkHandler;

public class ManaHUD {

    public static final ManaHUD instance = new ManaHUD();
    public static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);

    @SubscribeEvent
    public void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        ManaBar(mc);
        ConvertedManaBar(mc);
        EnergyBar(mc);
    }

    private void ManaBar(Minecraft mc) {
        float currentMana = ClientTickHandler.displayedMana;
        float maxMana = ManaNetworkHandler.getMaxMana();
        if (maxMana <= 0) return;

        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int barWidth = 5;
        int barHeight = 40;
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();
        int hotbarX = (screenWidth - 182) / 2;
        int hotbarY = screenHeight;
        int x = hotbarX + 182 + 7;
        int y = hotbarY;

        float manaPercentage = currentMana / maxMana;
        int filledHeight = (int) Math.ceil(manaPercentage * barHeight);
        mc.getTextureManager()
            .bindTexture(manaBar);
        Gui gui = new Gui();

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x, y, 0);
            GL11.glRotatef(-90, 0.0F, 0.0F, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 0, barHeight, barWidth);

            int color = Color.HSBtoRGB(
                0.528F,
                ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F) * 0.3F
                    + 0.4F,
                1F);
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 15, filledHeight, barWidth);
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
        GL11.glPopMatrix();
    }

    private void ConvertedManaBar(Minecraft mc) {
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        IInventory mainInv = player.inventory;
        IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

        ItemStack selectedItem = Helper.getItemWithHighestMana(mainInv, baublesInv);
        if (selectedItem == null) return;

        if (!(selectedItem.getItem() instanceof IManaItem manaItem)) return;

        float totalCurrentMana = ClientTickHandler.displayedCMana;
        float totalMaxMana = manaItem.getMaxMana(selectedItem);
        if (totalMaxMana <= 0) return;

        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int barWidth = 5;
        int barHeight = 40;
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();
        int hotbarX = (screenWidth - 182) / 2;
        int hotbarY = screenHeight;
        int x = hotbarX + 182 + 13;
        int y = hotbarY;

        float manaPercentage = totalCurrentMana / totalMaxMana;
        int filledHeight = (int) Math.ceil(manaPercentage * barHeight);
        mc.getTextureManager()
            .bindTexture(manaBar);
        Gui gui = new Gui();

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x, y, 0);
            GL11.glRotatef(-90, 0.0F, 0.0F, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 0, barHeight, barWidth);

            int color = Color.HSBtoRGB(
                0.75F,
                0.2F + ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F)
                    * 0.15F,
                1F);
            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 15, filledHeight, barWidth);
            GL11.glColor4f(1F, 1F, 1F, 1F); // reset lại về màu trắng
        }
        GL11.glPopMatrix();
    }

    private void EnergyBar(Minecraft mc) {
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;

        IInventory mainInv = player.inventory;
        IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

        ItemStack selectedItem = Helper.getItemWithHighestMana(mainInv, baublesInv);
        if (selectedItem == null) return;

        if (!(selectedItem.getItem() instanceof IEnergyContainerItem manaEnergyItem)) return;

        float totalCurrentEnergy = manaEnergyItem.getEnergyStored(selectedItem);
        float totalMaxEnergy = manaEnergyItem.getMaxEnergyStored(selectedItem);
        if (totalMaxEnergy <= 0) return;

        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int barWidth = 5;
        int barHeight = 40;
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();
        int hotbarX = (screenWidth - 182) / 2;
        int hotbarY = screenHeight;
        int x = hotbarX + 182 + 19;
        int y = hotbarY;

        float manaPercentage = totalCurrentEnergy / totalMaxEnergy;
        int filledHeight = (int) Math.ceil(manaPercentage * barHeight);

        mc.getTextureManager()
            .bindTexture(manaBar);
        Gui gui = new Gui();

        GL11.glPushMatrix();
        {
            GL11.glTranslatef(x, y, 0);
            GL11.glRotatef(-90, 0.0F, 0.0F, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 0, barHeight, barWidth);

            int color = Color.HSBtoRGB(
                0.9F,
                ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2F) + 1F) * 0.3F
                    + 0.4F,
                1F);

            float r = ((color >> 16) & 0xFF) / 255.0F;
            float g = ((color >> 8) & 0xFF) / 255.0F;
            float b = (color & 0xFF) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
            gui.drawTexturedModalRect(0, -barWidth, 10, 15, filledHeight, barWidth);
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
        GL11.glPopMatrix();
    }

}
