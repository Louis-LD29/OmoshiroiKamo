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

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event);
        BaubleExpandedSlots.tryAssignSlotsUpToMinimum("ring", 3);
        ModItems.init();
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(ManaRegenHandler.instance);
        FMLCommonHandler.instance().bus().register(ConvertManaRegenHandler.instance);
        FMLCommonHandler.instance().bus().register(FlightHandler.instance);
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {}

}
