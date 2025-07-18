package louis.omoshiroikamo.common.core.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import louis.omoshiroikamo.api.client.IFlightEnablerItem;

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

        if (hasFlightItem && player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = true;
            if (!player.onGround) player.capabilities.isFlying = true;
            player.sendPlayerAbilities();
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        IInventory baubles = BaublesApi.getBaubles(player);
        boolean hasFlightItem = false;
        if (baubles != null) {
            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                ItemStack stack = baubles.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof IFlightEnablerItem) {
                    hasFlightItem = true;
                    break;
                }
            }
        }

        if (!hasFlightItem && !player.capabilities.isCreativeMode) {
            // Nếu đang bay thì tắt bay
            if (player.capabilities.allowFlying) {
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
                player.sendPlayerAbilities();
            }
        }
    }

}
