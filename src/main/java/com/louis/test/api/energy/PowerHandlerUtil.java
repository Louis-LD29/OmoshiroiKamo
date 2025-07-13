package com.louis.test.api.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.common.block.AbstractPoweredTE;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public class PowerHandlerUtil {

    public static final String STORED_ENERGY_NBT_KEY = "storedEnergyRF";

    public static IPowerInterface create(Object o) {
        if (o instanceof IEnergyHandler) {
            System.out.println(
                "[PowerHandlerUtil] Wrapping as IEnergyHandler: " + o.getClass()
                    .getSimpleName());
            return new EnergyHandlerPI((IEnergyHandler) o);
        } else if (o instanceof IEnergyProvider) {
            System.out.println(
                "[PowerHandlerUtil] Wrapping as IEnergyProvider: " + o.getClass()
                    .getSimpleName());
            return new EnergyProviderPI((IEnergyProvider) o);
        } else if (o instanceof IEnergyReceiver) {
            System.out.println(
                "[PowerHandlerUtil] Wrapping as IEnergyReceiver: " + o.getClass()
                    .getSimpleName());
            return new EnergyReceiverPI((IEnergyReceiver) o);
        } else if (o instanceof IEnergyConnection) {
            System.out.println(
                "[PowerHandlerUtil] Wrapping as IEnergyConnection: " + o.getClass()
                    .getSimpleName());
            return new EnergyConnectionPI((IEnergyConnection) o);
        }

        System.out.println(
            "[PowerHandlerUtil] Not power-compatible: " + o.getClass()
                .getSimpleName());
        return null;
    }

    public static int getStoredEnergyForItem(ItemStack item) {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            return 0;
        }

        if (tag.hasKey("storedEnergy")) {
            double storedMj = tag.getDouble("storedEnergy");
            return (int) (storedMj * 10);
        }

        return tag.getInteger(STORED_ENERGY_NBT_KEY);
    }

    public static void setStoredEnergyForItem(ItemStack item, int storedEnergy) {
        NBTTagCompound tag = item.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setInteger(STORED_ENERGY_NBT_KEY, storedEnergy);
        item.setTagCompound(tag);
    }

    public static int recieveInternal(IInternalPoweredTile target, int maxReceive, ForgeDirection from,
        boolean simulate) {

        int result = Math.min(target.getMaxEnergyRecieved(from), maxReceive);
        result = Math.min(target.getMaxEnergyStored() - target.getEnergyStored(), result);
        result = Math.max(0, result);
        if (result > 0 && !simulate) {
            target.setEnergyStored(target.getEnergyStored() + result);
        }
        return result;
    }

    public static int recieveInternal(AbstractPoweredTE target, int maxReceive, ForgeDirection from, boolean simulate) {

        int result = Math.min(target.getMaxEnergyRecieved(), maxReceive);
        result = Math.min(target.getMaxEnergyStored() - target.getEnergyStored(), result);
        result = Math.max(0, result);
        if (result > 0 && !simulate) {
            target.setEnergyStored(target.getEnergyStored() + result);
        }
        return result;
    }

}
