package louis.omoshiroikamo.client.gui.modularui2.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import louis.omoshiroikamo.common.item.backpack.BackpackGui;
import louis.omoshiroikamo.common.item.backpack.ItemBatteryUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemCraftingUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemFeedingUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemMagnetUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemStackUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemUpgrade;

public class BackpackUpgradeSlot extends ModularSlot {

    protected final BackpackGui gui;

    public BackpackUpgradeSlot(IItemHandler itemHandler, int index, BackpackGui gui) {
        super(itemHandler, index);
        this.gui = gui;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        ItemStack originalUpgradeItem = this.getStack();
        if (originalUpgradeItem == null) {
            return true;
        }

        ItemStack newUpgradeItem = player.inventory.getItemStack();
        if (newUpgradeItem == null) {
            if (originalUpgradeItem.getItem() instanceof ItemStackUpgrade original) {
                return gui.canRemoveStackUpgrade(original.multiplier(originalUpgradeItem));
            }
            if (originalUpgradeItem.getItem() instanceof ItemCraftingUpgrade original) {
                return gui.canRemoveCraftingUpgrade();
            }
            return true;
        }

        if (originalUpgradeItem.getItem() instanceof ItemStackUpgrade original
            && newUpgradeItem.getItem() instanceof ItemStackUpgrade newer) {
            return gui
                .canReplaceStackUpgrade(original.multiplier(originalUpgradeItem), newer.multiplier(newUpgradeItem));
        }

        return true;
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (item instanceof ItemStackUpgrade upgrade) {
            return gui.canAddStackUpgrade(upgrade.multiplier(stack));
        }
        if (item instanceof ItemCraftingUpgrade) {
            return gui.canAddCraftingUpgrade();
        }
        if (item instanceof ItemMagnetUpgrade) {
            return gui.canAddMagnetUpgrade();
        }
        if (item instanceof ItemFeedingUpgrade) {
            return gui.canAddFeedingUpgrade();
        }
        if (item instanceof ItemBatteryUpgrade) {
            return gui.canAddBatteryUpgrade();
        }
        return item instanceof ItemUpgrade;
    }

}
