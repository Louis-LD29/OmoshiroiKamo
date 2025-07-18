package louis.omoshiroikamo.common.core.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import louis.omoshiroikamo.api.mana.IManaItem;
import louis.omoshiroikamo.common.core.helper.Helper;
import louis.omoshiroikamo.common.mana.ManaNetworkHandler;

public class ConvertManaRegenHandler {

    public static final ConvertManaRegenHandler instance = new ConvertManaRegenHandler();
    private final Map<String, Integer> tickCounter = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isClient()) return;

        EntityPlayer player = event.player;
        String key = player.getCommandSenderName();

        int ticks = tickCounter.merge(key, 1, Integer::sum);
        if (ticks < 20) return;
        tickCounter.put(key, 0);

        UUID uuid = player.getUniqueID();
        float currentMana = ManaNetworkHandler.getCurrentMana(uuid);

        ItemStack selectedItem = Helper.getItemWithHighestMana(player.inventory, BaublesApi.getBaubles(player));
        if (selectedItem == null) return;

        IManaItem manaItem = (IManaItem) selectedItem.getItem();
        int current = manaItem.getMana(selectedItem);
        int max = manaItem.getMaxMana(selectedItem);
        if (current >= max) return;

        float neededMana = 0.5f * (max - current);
        float rate = manaItem.getConversionRate(selectedItem);
        float requiredManaFromPlayer = neededMana / rate;

        if (requiredManaFromPlayer > currentMana) {
            requiredManaFromPlayer = currentMana;
            neededMana = requiredManaFromPlayer * rate;
        }

        IEnergyContainerItem energyItem = (manaItem instanceof IEnergyContainerItem) ? (IEnergyContainerItem) manaItem
            : null;
        if (energyItem != null && energyItem.getEnergyStored(selectedItem) > 0) {
            float energySupportedMana = 0.25f * requiredManaFromPlayer;
            int energyCost = (int) Math.ceil(energySupportedMana * 16);
            energyItem.extractEnergy(selectedItem, energyCost, false);
            requiredManaFromPlayer -= energySupportedMana;
        }

        currentMana -= requiredManaFromPlayer;
        manaItem.addMana(selectedItem, (int) Math.ceil(neededMana));

        ManaNetworkHandler.setCurrentMana(uuid, currentMana);
    }
}
