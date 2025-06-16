package com.louis.test.common.recipes;

import net.minecraftforge.common.MinecraftForge;

import com.louis.test.common.block.electrolyzer.ElectrolyzerRecipes;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        ElectrolyzerRecipes.init();
    }
}
