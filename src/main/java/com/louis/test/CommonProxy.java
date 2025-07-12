package com.louis.test;

import com.louis.test.api.energy.MaterialWireType;
import com.louis.test.api.material.MaterialRegistry;
import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.command.ModCommands;
import com.louis.test.common.config.Config;
import com.louis.test.common.core.handlers.ConvertManaRegenHandler;
import com.louis.test.common.core.handlers.FlightHandler;
import com.louis.test.common.core.handlers.ManaRegenHandler;
import com.louis.test.common.fluid.ModFluids;
import com.louis.test.common.item.ModItems;
import com.louis.test.common.plugin.compat.IECompat;
import com.louis.test.common.plugin.nei.IMCForNEI;
import com.louis.test.common.recipes.ModRecipes;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.preInit(event);

        MaterialRegistry.init();
        MaterialWireType.init();

        ModBlocks.init();
        ModItems.init();
        ModFluids.init();
        ModRecipes.init();

        IECompat.preInit();
    }

    public void init(FMLInitializationEvent event) {
        Config.init();
        FMLCommonHandler.instance()
            .bus()
            .register(ManaRegenHandler.instance);
        FMLCommonHandler.instance()
            .bus()
            .register(ConvertManaRegenHandler.instance);
        FMLCommonHandler.instance()
            .bus()
            .register(FlightHandler.instance);

        IECompat.init();

        if (Loader.isModLoaded("Waila")) {
            FMLInterModComms
                .sendMessage("Waila", "register", "com.louis.test.common.plugin.waila.WailaRegistrar.wailaCallback");
        }

        if (Loader.isModLoaded("NotEnoughItems")) {
            IMCForNEI.IMCSender();
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        Config.postInit();
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.init(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        IECompat.serverLoad();
    }

}
