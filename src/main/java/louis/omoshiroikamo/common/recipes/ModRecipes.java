package louis.omoshiroikamo.common.recipes;

import louis.omoshiroikamo.common.recipes.ore.CopperRecipes;
import net.minecraftforge.common.MinecraftForge;

import louis.omoshiroikamo.api.enums.ModObject;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        CopperRecipes.init();
    }
}
