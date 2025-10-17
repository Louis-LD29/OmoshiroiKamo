package ruiseki.omoshiroikamo.common.fluid;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.StringUtils;

import ruiseki.omoshiroikamo.api.material.MaterialEntry;
import ruiseki.omoshiroikamo.api.material.MaterialRegistry;

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
        return ModFluids.registerFluid(
            name,
            name + ".molten",
            "fluid.molten." + name,
            "liquid_" + name,
            density,
            viscosity,
            temperature,
            Material.lava);
    }

}
