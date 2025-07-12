package com.louis.test.common.plugin.waila;

import net.minecraft.entity.EntityLivingBase;

import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.core.lib.LibMisc;

import cpw.mods.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegistrar {

    public static void wailaCallback(IWailaRegistrar registrar) {

        // Configs
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".fluidTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".energyTE");

        // Provider
        registrar.registerBodyProvider(new FluidTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new EnergyTEDataProvider(), AbstractTE.class);

        // ==== Entity ElementType Provider ====
        ElementWailaProvider provider = new ElementWailaProvider();
        registrar.registerBodyProvider(provider, EntityLivingBase.class);
        registrar.registerHeadProvider(provider, EntityLivingBase.class);
        registrar.registerNBTProvider(provider, EntityLivingBase.class);
    }

    public static void init() {
        FMLInterModComms
            .sendMessage("Waila", "register", "com.louis.test.common.plugin.waila.WailaRegistrar.wailaCallback");
    }
}
