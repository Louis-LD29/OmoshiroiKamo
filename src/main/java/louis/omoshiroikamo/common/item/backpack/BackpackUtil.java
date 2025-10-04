package louis.omoshiroikamo.common.item.backpack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.network.PacketBackPackState;
import louis.omoshiroikamo.plugin.baubles.BaublesUtil;

public class BackpackUtil {

    public static void setCooldown(ItemStack stack, int cooldown, String tag) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null) {
            root = new NBTTagCompound();
            stack.setTagCompound(root);
        }
        root.setInteger(tag, cooldown);

    }

    public static int getCooldown(ItemStack stack, String tag) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null) {
            root = new NBTTagCompound();
            stack.setTagCompound(root);
        }
        if (!root.hasKey(tag)) {
            root.setInteger(tag, 2);
        }
        return root.getInteger(tag);
    }

    public static void addItemToFilter(List<Item> list, String name) {
        if (name == null) {
            return;
        }
        String[] parts = name.split(":");
        if (parts.length == 2) {
            Item item = GameRegistry.findItem(parts[0], parts[1]);
            if (item != null && !list.contains(item)) {
                list.add(item);
            }
        }
    }

    public static class ActiveBackPack {

        ItemStack item;
        int slot;

        ActiveBackPack(ItemStack item, int slot) {
            this.item = item;
            this.slot = slot;
        }
    }

    public static void setBackpackActive(EntityPlayerMP player, PacketBackPackState.SlotType type, int slot,
        boolean isActive) {
        ItemStack stack = null;
        IInventory baubles = null;
        int dropOff = -1;
        switch (type) {
            case INVENTORY:
                stack = player.inventory.getStackInSlot(slot);
                break;
            case ARMOR:
                return;
            case BAUBLES:
                baubles = BaublesUtil.instance()
                    .getBaubles(player);
                if (baubles != null) {
                    stack = baubles.getStackInSlot(slot);
                }
                break;
        }
        if (stack == null || stack.getItem() == null) {
            return;
        }
        if (type == PacketBackPackState.SlotType.BAUBLES && !isActive) {
            ItemStack[] inv = player.inventory.mainInventory;
            for (int i = 0; i < inv.length && dropOff < 0; i++) {
                if (inv[i] == null) {
                    dropOff = i;
                }
            }
            if (dropOff < 0) {
                return;
            }
        }
        switch (type) {
            case INVENTORY:
                player.inventory.setInventorySlotContents(slot, stack);
                player.inventory.markDirty();
                break;
            case ARMOR:
                return;
            case BAUBLES:
                if (dropOff < 0) {
                    baubles.setInventorySlotContents(slot, stack);
                } else {
                    baubles.setInventorySlotContents(slot, null);
                    player.inventory.setInventorySlotContents(dropOff, stack);
                }
                player.inventory.markDirty();
                break;
        }
    }

    public static List<String> getFilter(ItemStack magnet, String tag) {
        List<String> blacklist = new ArrayList<>();
        if (magnet != null && magnet.hasTagCompound()
            && magnet.getTagCompound()
                .hasKey(tag)) {
            NBTTagList list = magnet.getTagCompound()
                .getTagList(tag, 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound entry = list.getCompoundTagAt(i);
                if (entry.hasKey("id")) {
                    blacklist.add(entry.getString("id"));
                }
            }
        }
        return blacklist;
    }

}
