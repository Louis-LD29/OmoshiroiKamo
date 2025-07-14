package com.louis.test.common.fluid.material;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.apache.commons.lang3.StringUtils;

import com.louis.test.api.material.MaterialEntry;
import com.louis.test.api.material.MaterialRegistry;

public class FluidMaterialRegistry {

    public static final Map<MaterialEntry, Block> BLOCKS = new HashMap<>();
    public static final Map<MaterialEntry, Fluid> FLUIDS = new HashMap<>();

    public static FluidMaterialRegistry create() {
        FluidMaterialRegistry result = new FluidMaterialRegistry();
        result.init();
        return result;
    }

    protected void init() {
        for (MaterialEntry entry : MaterialRegistry.all()) {
            String fluidName = StringUtils.uncapitalize(entry.getUnlocalizedName() + ".molten");

            // --- Step 1: Try to get or create Fluid ---
            Fluid fluid = FluidRegistry.getFluid(fluidName);
            if (fluid == null) {

                fluid = new Fluid(fluidName).setDensity((int) Math.round(entry.getDensityKgPerM3()))
                    .setViscosity(estimateViscosity(entry))
                    .setTemperature((int) Math.round(entry.getMeltingPointK()))
                    .setLuminosity(estimateLuminosity(entry));

                if (!FluidRegistry.registerFluid(fluid)) {
                    fluid = FluidRegistry.getFluid(fluidName);
                    if (fluid == null) {
                        System.err.println("[FluidRegistry] Failed to register or retrieve fluid: " + fluidName);
                        continue;
                    }
                }
            }

            // --- Step 2: Get or create fluid block ---
            Block block = fluid.getBlock();
            if (block == null) {
                block = BlockFluidMaterial.create(fluid, Material.lava);
            }

            // --- Step 4: Store all mappings ---
            BLOCKS.put(entry, block);
            FLUIDS.put(entry, fluid);
        }
    }

    public int estimateViscosity(MaterialEntry entry) {
        double base = 5000.0 - Math.min(4000, entry.getMeltingPointK());
        base *= 1.0 + entry.getDensityKgPerM3() / 10000.0;
        return (int) Math.max(500, base);
    }

    private int estimateLuminosity(MaterialEntry entry) {
        double melt = entry.getMeltingPointK();
        return (int) Math.min(15, (melt - 600) / 150);
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
}
