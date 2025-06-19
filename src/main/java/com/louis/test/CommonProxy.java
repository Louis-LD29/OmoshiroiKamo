package com.louis.test;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.command.ModCommands;
import com.louis.test.common.config.Config;
import com.louis.test.common.fluid.ModFluids;
import com.louis.test.common.item.ModItems;
import com.louis.test.common.nei.IMCForNEI;
import com.louis.test.common.recipes.ModRecipes;
import com.louis.test.core.handlers.ConvertManaRegenHandler;
import com.louis.test.core.handlers.FlightHandler;
import com.louis.test.core.handlers.ManaRegenHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.preInit(event);
        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        ModRecipes.init();
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

}
