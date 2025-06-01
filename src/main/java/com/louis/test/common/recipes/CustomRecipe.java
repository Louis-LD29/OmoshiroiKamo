package com.louis.test.common.recipes;

import net.minecraft.item.ItemStack;

public class CustomRecipe {

    private final ItemStack input;
    private final ItemStack output;

    public CustomRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public boolean matches(ItemStack inputStack) {
        return input != null && inputStack != null
            && inputStack.isItemEqual(input)
            && inputStack.stackSize >= input.stackSize;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public ItemStack getInput() {
        return input.copy();
    }

}
