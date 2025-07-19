package louis.omoshiroikamo.common.fluid.material;

import java.util.HashMap;
import java.util.Map;

import louis.omoshiroikamo.common.fluid.FluidFuelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.fluid.ItemBucketMaterial;

public class FluidMaterialRegister {

    public static Item itemBucketMaterial;
    public static final Map<MaterialEntry, Block> BLOCKS = new HashMap<>();
    public static final Map<MaterialEntry, Fluid> FLUIDS = new HashMap<>();

    public static void init() {
        for (MaterialEntry entry : MaterialRegistry.all()) {
            String fluidName = StringUtils.uncapitalize(entry.getUnlocalizedName());

            Fluid fluid = registerFluid(
                fluidName,
                (int) Math.round(entry.getDensityKgPerM3()),
                estimateViscosity(entry),
                (int) Math.round(entry.getMeltingPointK()));
            Block block = fluid.getBlock();

            BLOCKS.put(entry, block);
            FLUIDS.put(entry, fluid);
        }
        itemBucketMaterial = ItemBucketMaterial.create();
    }

    public static int estimateViscosity(MaterialEntry entry) {
        double base = 5000.0 - Math.min(4000, entry.getMeltingPointK());
        base *= 1.0 + entry.getDensityKgPerM3() / 10000.0;
        return (int) Math.max(500, base);
    }

    // --- Helper Methods ---

    public static Fluid getFluid(MaterialEntry entry) {
        return FLUIDS.get(entry);
    }

    public static Block getBlock(MaterialEntry entry) {
        return BLOCKS.get(entry);
    }

    public static Fluid getFluid(String materialName) {
        for (Map.Entry<MaterialEntry, Fluid> e : FLUIDS.entrySet()) {
            if (e.getKey()
                .getName()
                .equalsIgnoreCase(materialName)) {
                return e.getValue();
            }
        }
        return FluidRegistry.WATER;
    }

    public static MaterialEntry getMaterialEntryFromFluid(Fluid fluid) {
        for (Map.Entry<MaterialEntry, Fluid> e : FLUIDS.entrySet()) {
            if (e.getValue() == fluid) {
                return e.getKey();
            }
        }
        return null;
    }

    public static Fluid registerFluid(String name, int density, int viscosity, int temperature) {
        return registerFluid(
            name,
            name + ".molten",
            "fluid.molten." + name,
            "liquid_" + name,
            density,
            viscosity,
            temperature,
            Material.lava);
    }

    public static Fluid registerFluid(String name, String fluidName, String blockName, String texture, int density,
        int viscosity, int temperature, Material material) {
        // create the new fluid
        Fluid fluid = new Fluid(fluidName).setDensity(density)
            .setViscosity(viscosity)
            .setTemperature(temperature);

        if (material == Material.lava) fluid.setLuminosity(12);

        // register it if it's not already existing
        boolean isFluidPreRegistered = !FluidRegistry.registerFluid(fluid);

        // register our fluid block for the fluid
        // this constructor implicitly does fluid.setBlock to it, that's why it's not called separately
        BlockFluidMaterial block = new BlockFluidMaterial(fluid, material, texture);
        block.setBlockName(blockName);
        GameRegistry.registerBlock(block, blockName);

        fluid.setBlock(block);
        block.setFluid(fluid);

        // if the fluid was already registered we use that one instead
        if (isFluidPreRegistered) {
            fluid = FluidRegistry.getFluid(fluidName);
            // don't change the fluid icons of already existing fluids
            if (fluid.getBlock() != null) block.suppressOverwritingFluidIcons();
            // if no block is registered with an existing liquid, we set our own
            else fluid.setBlock(block);
        }

        return fluid;
    }
}
