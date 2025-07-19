package louis.omoshiroikamo.common.fluid;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.StringUtils;

import louis.omoshiroikamo.api.fluid.FluidEntry;
import louis.omoshiroikamo.api.fluid.FluidRegistry;
import louis.omoshiroikamo.common.fluid.material.FluidMaterialRegister;

public class FluidRegister {

    public static Item itemBucketFluid;
    public static final Map<FluidEntry, Block> BLOCKS = new HashMap<>();
    public static final Map<FluidEntry, Fluid> FLUIDS = new HashMap<>();

    public static void init() {
        for (FluidEntry entry : FluidRegistry.all()) {
            String fluidName = StringUtils.uncapitalize(entry.getUnlocalizedName());

            int density = (int) Math.round(entry.getDensityKgPerM3());
            if (entry.isGas()) {
                density = -Math.abs(density); // Đảm bảo khí có density âm
            }

            int viscosity = (int) Math.round(entry.getConfig().viscosityPaS * 1000); // Pa·s → mPa·s (nếu cần đơn vị)
            int temperature = (int) Math.round(entry.getTemperature());

            Fluid fluid = registerFluid(fluidName, density, viscosity, temperature);

            Block block = fluid.getBlock();
            BLOCKS.put(entry, block);
            FLUIDS.put(entry, fluid);
        }

        itemBucketFluid = ItemBucketFluid.create();
    }

    public static Fluid getFluid(FluidEntry entry) {
        return FLUIDS.get(entry);
    }

    public static Block getBlock(FluidEntry entry) {
        return BLOCKS.get(entry);
    }

    public static Fluid getFluid(String materialName) {
        for (Map.Entry<FluidEntry, Fluid> e : FLUIDS.entrySet()) {
            if (e.getKey()
                .getName()
                .equalsIgnoreCase(materialName)) {
                return e.getValue();
            }
        }
        return net.minecraftforge.fluids.FluidRegistry.WATER;
    }

    public static Fluid registerFluid(String name, int density, int viscosity, int temperature) {
        return FluidMaterialRegister.registerFluid(
            name,
            name,
            "fluid." + name,
            "fluid_" + name,
            density,
            viscosity,
            temperature,
            Material.water);
    }
}
