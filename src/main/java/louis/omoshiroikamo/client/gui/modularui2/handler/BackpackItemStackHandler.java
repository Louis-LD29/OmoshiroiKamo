package louis.omoshiroikamo.client.gui.modularui2.handler;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.ItemHandlerHelper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import louis.omoshiroikamo.common.item.backpack.BackpackGui;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;

public class BackpackItemStackHandler extends ItemStackHandler {

    private final BackpackGui gui;

    public BackpackItemStackHandler(int size, BackpackGui gui) {
        super(size);
        this.gui = gui;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return !(stack.getItem() instanceof ItemBackpack);
    }

    @Override
    public int getStackLimit(int slot, ItemStack stack) {
        return stack.getMaxStackSize() * gui.getTotalStackMultiplier();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64 * gui.getTotalStackMultiplier();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack == null) {
            return null;
        }
        validateSlotIndex(slot);

        ItemStack existing = stacks.get(slot);
        int limit = getStackLimit(slot, stack);

        if (existing != null) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                return stack;
            }
            limit -= existing.stackSize;
        }

        if (limit <= 0) {
            return stack;
        }

        boolean reachedLimit = stack.stackSize > limit;

        if (!simulate) {
            if (existing == null) {
                stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.stackSize += (reachedLimit ? limit : stack.stackSize);
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return null;
        }
        validateSlotIndex(slot);

        ItemStack stack = stacks.get(slot);
        if (stack == null) {
            return null;
        }

        int slotMaxStackSize = stack.getMaxStackSize() * gui.getTotalStackMultiplier();
        int toExtract = Math.min(amount, slotMaxStackSize);

        if (stack.stackSize <= toExtract) {
            if (!simulate) {
                stacks.set(slot, null);
                onContentsChanged(slot);
            }
            return stack;
        } else {
            if (!simulate) {
                stacks.set(slot, ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - toExtract));
                onContentsChanged(slot);
            }
            return ItemHandlerHelper.copyStackWithSize(stack, toExtract);
        }
    }
}
