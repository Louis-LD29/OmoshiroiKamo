package ruiseki.omoshiroikamo.client.gui.modularui2.slot;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class BackpackFilterSlot extends ModularSlot {

    public BackpackFilterSlot(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
    }
}
