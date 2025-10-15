package louis.omoshiroikamo.api.item;

import net.minecraft.item.ItemStack;

public class WeightedItemStack extends WeightedStackBase {

    private final ItemStack stack;

    public WeightedItemStack(ItemStack stack, int itemWeightIn) {
        super(itemWeightIn);
        this.stack = stack;
    }

    @Override
    public boolean isStackEqual(ItemStack other) {
        return this.stack != null && other != null && this.stack.isItemEqual(other);
    }

    @Override
    public ItemStack getMainStack() {
        return this.stack;
    }

    @Override
    public WeightedStackBase copy() {
        return new WeightedItemStack(this.stack.copy(), this.itemWeight);
    }
}
