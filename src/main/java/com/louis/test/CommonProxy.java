package com.louis.test;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.command.ModCommands;
import com.louis.test.common.config.Config;
import com.louis.test.common.fluid.ModFluids;
import com.louis.test.common.item.ModItems;
import com.louis.test.common.plugin.compat.IE.IECompat;
import com.louis.test.common.plugin.nei.IMCForNEI;
import com.louis.test.common.recipes.ModRecipes;
import com.louis.test.core.handlers.ConvertManaRegenHandler;
import com.louis.test.core.handlers.FlightHandler;
import com.louis.test.core.handlers.ManaRegenHandler;
import com.louis.test.core.handlers.ModEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.*;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.preInit(event);

        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        ModRecipes.init();
        MinecraftForge.EVENT_BUS.register(new ModEvent());

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
