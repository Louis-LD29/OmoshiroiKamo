package louis.omoshiroikamo.common.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;

import louis.omoshiroikamo.api.fluid.FluidMaterial;
import louis.omoshiroikamo.common.core.lib.LibMisc;

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

        // Steam
        f = new Fluid(FluidMaterial.STEAM.getName())
            .setDensity(
                FluidMaterial.STEAM.isGas() ? -(int) Math.ceil(FluidMaterial.STEAM.getDensity())
                    : (int) Math.ceil(FluidMaterial.STEAM.getDensity()))
            .setViscosity((int) FluidMaterial.STEAM.getViscosity())
            .setTemperature((int) FluidMaterial.STEAM.getTemperatureK());
        FluidRegistry.registerFluid(f);
        fluidSteam = FluidRegistry.getFluid(f.getName());
        blockSteam = BlockFluidEio.Steam.create(fluidSteam, Material.water);
        itemBucketSteam = ItemBucketEio.create(fluidSteam, !FluidMaterial.STEAM.isGas());

        // Hydrogen
        f = new Fluid(FluidMaterial.HYDROGEN.getName())
            .setDensity(
                FluidMaterial.HYDROGEN.isGas() ? -(int) Math.ceil(FluidMaterial.HYDROGEN.getDensity())
                    : (int) Math.ceil(FluidMaterial.HYDROGEN.getDensity()))
            .setViscosity((int) FluidMaterial.HYDROGEN.getViscosity())
            .setTemperature((int) FluidMaterial.HYDROGEN.getTemperatureK());
        FluidRegistry.registerFluid(f);
        fluidHydrogen = FluidRegistry.getFluid(f.getName());
        blockHydrogen = BlockFluidEio.Hydrogen.create(fluidHydrogen, Material.water);
        itemBucketHydrogen = ItemBucketEio.create(fluidHydrogen, !FluidMaterial.HYDROGEN.isGas());

        // Oxygen
        f = new Fluid(FluidMaterial.OXYGEN.getName())
            .setDensity(
                FluidMaterial.OXYGEN.isGas() ? -(int) Math.ceil(FluidMaterial.OXYGEN.getDensity())
                    : (int) Math.ceil(FluidMaterial.OXYGEN.getDensity()))
            .setViscosity((int) FluidMaterial.OXYGEN.getViscosity())
            .setTemperature((int) FluidMaterial.OXYGEN.getTemperatureK());
        FluidRegistry.registerFluid(f);
        fluidOxygen = FluidRegistry.getFluid(f.getName());
        blockOxygen = BlockFluidEio.Oxygen.create(fluidOxygen, Material.water);
        itemBucketOxygen = ItemBucketEio.create(fluidOxygen, !FluidMaterial.OXYGEN.isGas());

    }

}
