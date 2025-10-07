package louis.omoshiroikamo.client.gui.modularui2.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.ItemHandlerHelper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import louis.omoshiroikamo.common.item.backpack.BackpackGui;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;

public class BackpackItemStackHandler extends ItemStackHandler {

    private final BackpackGui gui;
    private final List<ItemStack> memorizedSlotStack;
    private final List<Boolean> memorizedSlotRespectNbtList;
    private final List<Boolean> sortLockedSlots;

    public BackpackItemStackHandler(int size, BackpackGui gui) {
        super(size);
        this.gui = gui;
        this.memorizedSlotStack = new ArrayList<>(Collections.nCopies(size, (ItemStack) null));
        this.memorizedSlotRespectNbtList = new ArrayList<>(size);
        this.sortLockedSlots = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            this.memorizedSlotRespectNbtList.add(false);
            this.sortLockedSlots.add(false);
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        ItemStack memorized = memorizedSlotStack.get(slot);

        if (memorized == null) {
            return !(stack.getItem() instanceof ItemBackpack);
        } else if (memorizedSlotRespectNbtList.get(slot)) {
            return ItemStack.areItemStacksEqual(stack, memorized);
        } else {
            return stack.isItemEqual(memorized);
        }
    }

    @Override
    public int getStackLimit(int slot, ItemStack stack) {
        return stack.getMaxStackSize() * gui.getTotalStackMultiplier();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64 * gui.getTotalStackMultiplier();
    }

    public ItemStack prioritizedInsertion(int slotIndex, ItemStack stack, boolean simulate) {
        stack = insertItemToMemorySlots(stack, simulate);
        return insertItem(slotIndex, stack, simulate);
    }

    public ItemStack insertItemToMemorySlots(ItemStack stack, boolean simulate) {
        if (stack == null) {
            return null;
        }

        for (int slotIndex = 0; slotIndex < memorizedSlotStack.size(); slotIndex++) {
            ItemStack memorizedStack = memorizedSlotStack.get(slotIndex);
            if (memorizedStack == null || !ItemStack.areItemStacksEqual(stack, memorizedStack)) {
                continue;
            }

            stack = insertItem(slotIndex, stack, simulate);
            if (stack == null) {
                return null;
            }
        }
        return stack;
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
