package com.louis.test.common.plugin.waila;

import net.minecraftforge.fluids.FluidStack;

import com.louis.test.common.block.AbstractTE;
import com.louis.test.core.lib.LibMisc;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistrar {

    public static void wailaCallback(IWailaRegistrar registrar) {

        // Configs
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".fluidTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".energyTE");

        // Provider
        registrar.registerBodyProvider(new FluidTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new EnergyTEDataProvider(), AbstractTE.class);
    }

    public static String fluidNameHelper(FluidStack f) {
        return f.getFluid()
            .getUnlocalizedName();
    }
}
