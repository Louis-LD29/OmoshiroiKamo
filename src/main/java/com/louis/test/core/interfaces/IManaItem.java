package com.louis.test.core.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IManaItem {
    public int getMana(ItemStack stack);
    public int getMaxMana(ItemStack stack);
    public void addMana(ItemStack stack, int mana);
    public float getConversionRate(ItemStack stack);
}
