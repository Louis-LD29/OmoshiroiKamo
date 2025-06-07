package com.louis.test.core.helper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.louis.test.api.interfaces.mana.IManaItem;

import cofh.api.energy.IEnergyContainerItem;

public class Helper {

    public static ItemStack getItemWithHighestMana(IInventory... inventories) {
        ItemStack best = null;
        float bestMaxEnergy = -1, bestMaxMana = -1, bestCurrentMana = -1;

        for (IInventory inv : inventories) {
            if (inv == null) continue;

            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack == null || !(stack.getItem() instanceof IManaItem)) continue;

                IManaItem mana = (IManaItem) stack.getItem();
                float maxMana = mana.getMaxMana(stack);
                float currentMana = mana.getMana(stack);
                float maxEnergy = (mana instanceof IEnergyContainerItem)
                    ? ((IEnergyContainerItem) mana).getMaxEnergyStored(stack)
                    : 0;

                if (maxEnergy > bestMaxEnergy || (maxEnergy == bestMaxEnergy
                    && (maxMana > bestMaxMana || (maxMana == bestMaxMana && currentMana > bestCurrentMana)))) {
                    best = stack;
                    bestMaxEnergy = maxEnergy;
                    bestMaxMana = maxMana;
                    bestCurrentMana = currentMana;
                }
            }
        }

        return best;
    }
}
