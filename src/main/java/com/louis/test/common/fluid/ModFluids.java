package com.louis.test.common.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;

import com.louis.test.lib.LibMisc;

import crazypants.enderio.fluid.FluidFuelRegister;

public class ModFluids {

    public static final String MANA = "mana";

    public static Fluid fluidMana;
    public static BlockFluidEio blockMana;
    public static ItemBucketEio itemBucketMana;

    public static String toCapactityString(IFluidTank tank) {
        if (tank == null) {
            return "0/0 " + MB();
        }
        return tank.getFluidAmount() + "/" + tank.getCapacity() + " " + MB();
    }

    public static String MB() {
        return LibMisc.lang.localize("fluid.millibucket.abr");
    }

    public static void init() {
        Fluid f = new Fluid(MANA).setDensity(500)
            .setViscosity(500)
            .setTemperature(5);
        FluidRegistry.registerFluid(f);
        fluidMana = FluidRegistry.getFluid(f.getName());
        blockMana = BlockFluidEio.Mana.create(fluidMana, Material.water);
        FluidFuelRegister.instance.addCoolant(f, 0.0314f);
        // itemBucketMana = ItemBucketEio.create(fluidMana);
    }

}
