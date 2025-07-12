package com.louis.test.plugin.waila;

import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.core.lib.LibMisc;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fluids.FluidStack;

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
