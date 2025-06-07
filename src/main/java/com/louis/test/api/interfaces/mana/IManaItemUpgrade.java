package com.louis.test.api.interfaces.mana;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.api.interfaces.IRenderUpgrade;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IManaItemUpgrade extends IAdvancedTooltipProvider {

    String getUnlocalizedName();

    int getLevelCost();

    boolean isUpgradeItem(ItemStack stack);

    boolean canAddToItem(ItemStack stack);

    boolean hasUpgrade(ItemStack stack);

    void writeToItem(ItemStack stack);

    void removeFromItem(ItemStack stack);

    ItemStack getUpgradeItem();

    String getUpgradeItemName();

    @Nullable
    @SideOnly(Side.CLIENT)
    IRenderUpgrade getRender();
}
