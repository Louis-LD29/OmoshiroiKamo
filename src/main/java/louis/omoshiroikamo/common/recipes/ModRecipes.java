package louis.omoshiroikamo.common.recipes;

import net.minecraftforge.common.MinecraftForge;

import louis.omoshiroikamo.api.enums.ModObject;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
    }
}
