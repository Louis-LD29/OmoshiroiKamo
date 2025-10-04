package louis.omoshiroikamo.common.item.backpack;

import static louis.omoshiroikamo.config.general.FeedingConfig.feedingConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.FoodStats;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.client.gui.BackpackGui;
import louis.omoshiroikamo.client.gui.modularui2.handler.UpgradeItemStackHandler;
import louis.omoshiroikamo.common.network.PacketBackPackState;
import louis.omoshiroikamo.common.network.PacketHandler;

@EventBusSubscriber()
public class BackpackFeedingController {

    private static final String TAG_COOLDOWN = "feedingCooldown";
    private static List<Item> filter = null;

    public BackpackFeedingController() {
        PacketHandler.INSTANCE
            .registerMessage(PacketBackPackState.class, PacketBackPackState.class, PacketHandler.nextID(), Side.SERVER);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        BackpackUtil.ActiveBackPack mag = getActiveMagnet(event.player);
        if (mag != null && event.player.getHealth() > 0f) {
            doHFeeding(event.player, mag);
        }
    }

    private static BackpackUtil.ActiveBackPack getActiveMagnet(EntityPlayer player) {
        ItemStack[] inv = player.inventory.mainInventory;
        int maxSlot = feedingConfig.feedingAllowInMainInventory ? 4 * 9 : 9;
        InventoryBaubles baubleInv = PlayerHandler.getPlayerBaubles(player);
        UpgradeItemStackHandler upgradeHandler = new UpgradeItemStackHandler(BackpackGui.upgradeSlot);

        for (int i = 0; i < maxSlot; i++) {
            ItemStack stack = inv[i];
            if (stack == null || !(stack.getItem() instanceof ItemBackpack)) {
                continue;
            }

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey(BackpackGui.BACKPACKUPGRADE)) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag(BackpackGui.BACKPACKUPGRADE));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemFeedingUpgrade) {
                    return new BackpackUtil.ActiveBackPack(stack, slot);
                }
            }
        }

        if (!feedingConfig.feedingAllowInBaublesSlot) {
            return null;
        }

        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
            ItemStack stack = baubleInv.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemBackpack)) {
                continue;
            }

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey(BackpackGui.BACKPACKUPGRADE)) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag(BackpackGui.BACKPACKUPGRADE));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemFeedingUpgrade) {
                    return new BackpackUtil.ActiveBackPack(stack, slot);
                }
            }
        }

        return null;
    }

    public static void doHFeeding(EntityPlayer player, BackpackUtil.ActiveBackPack mag) {
        int cooldown = BackpackUtil.getCooldown(mag.item, TAG_COOLDOWN);
        initFilter(mag);

        if (cooldown > 0) {
            BackpackUtil.setCooldown(mag.item, cooldown - 1, TAG_COOLDOWN);
            return;
        }

        NBTTagCompound root = mag.item.getTagCompound();
        if (root == null || !root.hasKey(BackpackGui.BACKPACKINV)) {
            return;
        }

        ItemStackHandler handler = new ItemStackHandler(BackpackGui.slot);
        handler.deserializeNBT(root.getCompoundTag(BackpackGui.BACKPACKINV));

        FoodStats foodStats = player.getFoodStats();
        boolean consumedAny = false;
        int missing = 20 - foodStats.getFoodLevel();

        if (missing <= 0) {
            return;
        }

        List<Integer> foodSlots = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack s = handler.getStackInSlot(i);
            if (s != null && s.getItem() instanceof ItemFood food) {
                if (!filter.isEmpty() && !filter.contains(food)) {
                    continue;
                }
                foodSlots.add(i);
            }
        }

        foodSlots.sort(
            Comparator.comparingInt(
                i -> ((ItemFood) handler.getStackInSlot(i)
                    .getItem()).func_150905_g(handler.getStackInSlot(i))));

        for (int slotIndex : foodSlots) {
            ItemStack stack = handler.getStackInSlot(slotIndex);
            if (stack == null) {
                continue;
            }

            ItemFood food = (ItemFood) stack.getItem();

            while (stack.stackSize > 0 && foodStats.getFoodLevel() < 20) {
                int heal = food.func_150905_g(stack);
                int before = foodStats.getFoodLevel();
                int missingNow = 20 - before;

                if (heal > missingNow + 1) {
                    break;
                }

                food.onEaten(stack, player.worldObj, player);
                consumedAny = true;

                if (stack.stackSize <= 0) {
                    handler.setStackInSlot(slotIndex, null);
                    break;
                } else {
                    handler.setStackInSlot(slotIndex, stack);
                }
            }

            if (foodStats.getFoodLevel() >= 20) {
                break;
            }
        }

        if (consumedAny) {
            root.setTag(BackpackGui.BACKPACKINV, handler.serializeNBT());
            mag.item.setTagCompound(root);
            BackpackUtil.setCooldown(mag.item, 2, TAG_COOLDOWN);
        }
    }

    private static void initFilter(BackpackUtil.ActiveBackPack mag) {
        Set<String> names = new HashSet<>();
        names.addAll(BackpackUtil.getFilter(mag.item, BackpackGui.FEEDING_FILTER));

        List<Item> itemList = new ArrayList<>();

        for (String name : names) {
            BackpackUtil.addItemToFilter(itemList, name);
        }

        filter = itemList;
    }

}
