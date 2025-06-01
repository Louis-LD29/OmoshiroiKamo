package com.louis.test;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.block.test.TestRecipeManager;
import com.louis.test.common.config.Config;
import com.louis.test.common.fluid.ModFluids;
import com.louis.test.common.item.ModItems;
import com.louis.test.common.recipes.ManaAnvilRecipe;
import com.louis.test.core.handlers.ConvertManaRegenHandler;
import com.louis.test.core.handlers.FlightHandler;
import com.louis.test.core.handlers.ManaRegenHandler;

import baubles.api.expanded.BaubleExpandedSlots;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.preInit(event);
        BaubleExpandedSlots.tryAssignSlotsUpToMinimum("ring", 3);
        ModItems.init();
        ModBlocks.init();
        ModFluids.init();
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
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
    }

    public void postInit(FMLPostInitializationEvent event) {
        Config.postInit();
        TestRecipeManager.getInstance()
            .loadRecipesFromConfig();
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }
}
