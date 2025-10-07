package louis.omoshiroikamo.client.handler;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.mana.IManaItem;
import louis.omoshiroikamo.common.mana.ManaNetworkHandler;
import louis.omoshiroikamo.common.util.helper.Helper;

@EventBusSubscriber(side = Side.CLIENT)
@SuppressWarnings("unused")
public class ClientTickHandler {

    public static int pageFlipTicks = 0;
    public static int ticksInGame = 0;
    public static float partialTicks = 0.0F;
    public static float delta = 0.0F;
    public static float total = 0.0F;
    public static float displayedMana = 0.0F;
    public static float displayedCMana = 0.0F;

    private static void calcDelta() {
        float oldTotal = total;
        total = (float) ticksInGame + partialTicks;
        delta = total - oldTotal;
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        } else {
            updateDisplayedMana();
            updateDisplayedCMana();
            TooltipAdditionDisplayHandler.render();
            calcDelta();
        }

    }

    @SubscribeEvent
    public static void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ticksInGame++;
                partialTicks = 0.0F;
            }
            calcDelta();
        }
    }

    public static void notifyPageChange() {
        if (pageFlipTicks == 0) {
            pageFlipTicks = 5;
        }
    }

    private static void updateDisplayedMana() {
        // Lấy mana thực tế
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) {
            return;
        }
        UUID uuid = mc.thePlayer.getUniqueID();
        float targetMana = ManaNetworkHandler.getCurrentMana(uuid);
        float maxMana = ManaNetworkHandler.getMaxMana();
        if (maxMana <= 0) {
            displayedMana = 0;
            return;
        }

        float smoothingRate = 2.0F;
        float dt = delta / 20.0F;
        float alpha = 1 - (float) Math.exp(-smoothingRate * dt);

        displayedMana += (targetMana - displayedMana) * alpha;
    }

    private static void updateDisplayedCMana() {
        // Lấy mana thực tế
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player == null) {
            return;
        }

        IInventory mainInv = player.inventory;
        IInventory baublesInv = PlayerHandler.getPlayerBaubles(player);

        ItemStack selectedItem = Helper.getItemWithHighestMana(mainInv, baublesInv);
        if (selectedItem == null) {
            return;
        }
        IManaItem manaItem = (IManaItem) selectedItem.getItem();
        float targetMana = manaItem.getMana(selectedItem);
        float maxMana = manaItem.getMaxMana(selectedItem);

        if (maxMana <= 0) {
            displayedCMana = 0;
            return;
        }

        float smoothingRate = 2.0F;
        float dt = delta / 20.0F;
        float alpha = 1 - (float) Math.exp(-smoothingRate * dt);

        displayedCMana += (targetMana - displayedCMana) * alpha;
    }

}
