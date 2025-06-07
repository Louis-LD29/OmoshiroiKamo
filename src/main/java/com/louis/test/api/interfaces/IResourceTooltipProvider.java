package com.louis.test.api.interfaces;

import net.minecraft.item.ItemStack;

public interface IResourceTooltipProvider {

    String getUnlocalizedNameForTooltip(ItemStack itemStack);
}
