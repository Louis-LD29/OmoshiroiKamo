package ruiseki.omoshiroikamo.client.gui.modularui2.slot;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import ruiseki.omoshiroikamo.common.item.backpack.BackpackGui;

public class BackpackSlot extends ModularSlot {

    protected final BackpackGui gui;

    public BackpackSlot(IItemHandler itemHandler, int index, BackpackGui gui) {
        super(itemHandler, index);
        this.gui = gui;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        int multiplier = gui.getTotalStackMultiplier();
        return stack.getMaxStackSize() * multiplier;
    }

    @Override
    public int getSlotStackLimit() {
        int multiplier = gui.getTotalStackMultiplier();
        return 64 * multiplier;
    }
}
