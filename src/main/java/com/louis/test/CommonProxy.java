package com.louis.test;

import baubles.api.expanded.BaubleExpandedSlots;
import com.louis.test.common.item.ModItems;
import com.louis.test.common.recipes.ManaAnvilRecipe;
import com.louis.test.config.Config;
import com.louis.test.core.handlers.ConvertManaRegenHandler;
import com.louis.test.core.handlers.FlightHandler;
import com.louis.test.core.handlers.ManaRegenHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        BaubleExpandedSlots.tryAssignSlotsUpToMinimum("ring", 3);
        FMLCommonHandler.instance().bus().register(FlightHandler.instance);
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    public void init(FMLInitializationEvent event) {
        ModItems.init();
        MinecraftForge.EVENT_BUS.register(new ManaAnvilRecipe());
        FMLCommonHandler.instance().bus().register(new ManaRegenHandler());
        FMLCommonHandler.instance().bus().register(new ConvertManaRegenHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}

}
