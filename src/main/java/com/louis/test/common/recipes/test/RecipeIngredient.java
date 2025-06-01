package com.louis.test.common.recipes.test;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeIngredient {

    public enum IngredientType {
        ITEM,
        FLUID
    }

    private final IngredientType type;

    private final ItemStack itemStack;
    private final String oreDict;

    private final FluidStack fluidStack;

    private final int slot;
    private final float multiplier;

    // Constructor cho item ingredient
    public RecipeIngredient(ItemStack itemStack, String oreDict, int slot, float multiplier) {
        if (itemStack == null && (oreDict == null || oreDict.isEmpty())) {
            throw new IllegalArgumentException("ItemStack or oreDict must be provided for item ingredient.");
        }
        this.type = IngredientType.ITEM;
        this.itemStack = itemStack;
        this.oreDict = oreDict;
        this.fluidStack = null;
        this.slot = slot;
        this.multiplier = multiplier;
    }

    // Constructor cho fluid ingredient
    public RecipeIngredient(FluidStack fluidStack, int slot, float multiplier) {
        if (fluidStack == null) {
            throw new IllegalArgumentException("FluidStack must be provided for fluid ingredient.");
        }
        this.type = IngredientType.FLUID;
        this.fluidStack = fluidStack.copy();
        this.itemStack = null;
        this.oreDict = null;
        this.slot = slot;
        this.multiplier = multiplier;
    }

    public IngredientType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        if (type != IngredientType.ITEM) throw new IllegalStateException("Not an item ingredient");
        return itemStack.copy();
    }

    public String getOreDict() {
        if (type != IngredientType.ITEM) throw new IllegalStateException("Not an item ingredient");
        return oreDict;
    }

    public FluidStack getFluidStack() {
        if (type != IngredientType.FLUID) throw new IllegalStateException("Not a fluid ingredient");
        return fluidStack.copy();
    }

    public int getSlot() {
        return slot;
    }

    public float getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        if (type == IngredientType.ITEM) {
            return "ItemIngredient[" + (oreDict != null ? "oreDict=" + oreDict : itemStack)
                + ", slot="
                + slot
                + ", multiplier="
                + multiplier
                + "]";
        } else {
            return "FluidIngredient[" + fluidStack.getFluid()
                .getName() + ", slot=" + slot + ", multiplier=" + multiplier + "]";
        }
    }
}
