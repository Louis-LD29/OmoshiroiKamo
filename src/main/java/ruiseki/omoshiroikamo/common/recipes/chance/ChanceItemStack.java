package ruiseki.omoshiroikamo.common.recipes.chance;

import net.minecraft.item.ItemStack;

public class ChanceItemStack {

    public final ItemStack stack;
    public final float chance; // từ 0.0f đến 1.0f

    public ChanceItemStack(ItemStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }
}
