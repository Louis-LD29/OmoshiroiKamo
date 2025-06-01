package com.louis.test.api.interfaces.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IManaItem {

    public int getMana(ItemStack stack);

    public int getMaxMana(ItemStack stack);

    public void addMana(ItemStack stack, int mana);

    public void addMana(ItemStack stack, int mana, EntityPlayer player);

    public float getConversionRate(ItemStack stack);

    public void useMana(ItemStack stack, int mana);

    public void useMana(ItemStack stack, int mana, EntityPlayer player);
}
