package com.louis.test.api.mana;

import com.louis.test.api.client.IAdvancedTooltipProvider;
import com.louis.test.api.client.IRenderUpgrade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

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
