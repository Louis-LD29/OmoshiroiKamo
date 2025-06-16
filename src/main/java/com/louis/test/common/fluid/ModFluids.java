package com.louis.test.common.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;

import com.louis.test.lib.LibMisc;

public class ModFluids {

    public static final String MANA = "mana";
    public static final String STEAM = "steam";

    public static Fluid fluidMana;
    public static BlockFluidEio blockMana;
    public static Fluid fluidSteam;
    public static BlockFluidEio blockSteam;
    public static ItemBucketEio itemBucketSteam;
    public static Fluid fluidHydrogen;
    public static BlockFluidEio blockHydrogen;
    public static ItemBucketEio itemBucketHydrogen;
    public static Fluid fluidOxygen;
    public static BlockFluidEio blockOxygen;
    public static ItemBucketEio itemBucketOxygen;

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

        f = new Fluid("steam").setDensity(-1000) // Steam nhẹ hơn không khí, thường dùng giá trị âm
            .setViscosity(1000) // Thấp hơn nước, vì steam loãng hơn
            .setTemperature(373); // 100°C = 373K
        FluidRegistry.registerFluid(f);
        fluidSteam = FluidRegistry.getFluid(f.getName());
        blockSteam = BlockFluidEio.Steam.create(fluidSteam, Material.water);
        itemBucketSteam = ItemBucketEio.create(fluidSteam, false);

        f = new Fluid("hydrogen").setDensity(-1000)
            .setViscosity(100)
            .setTemperature(20 + 273);
        FluidRegistry.registerFluid(f);
        fluidHydrogen = FluidRegistry.getFluid(f.getName());
        blockHydrogen = BlockFluidEio.Hydrogen.create(fluidHydrogen, Material.water);
        itemBucketHydrogen = ItemBucketEio.create(fluidHydrogen, false);

        f = new Fluid("oxygen").setDensity(-500)
            .setViscosity(150)
            .setTemperature(20 + 273);
        FluidRegistry.registerFluid(f);
        fluidOxygen = FluidRegistry.getFluid(f.getName());
        blockOxygen = BlockFluidEio.Oxygen.create(fluidOxygen, Material.water);
        itemBucketOxygen = ItemBucketEio.create(fluidOxygen, false);
    }

}
