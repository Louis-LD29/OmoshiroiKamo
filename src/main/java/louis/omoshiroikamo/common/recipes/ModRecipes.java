package louis.omoshiroikamo.common.recipes;

import net.minecraftforge.common.MinecraftForge;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.recipes.ore.CopperRecipes;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeLoader.loadRecipes(ModObject.blockAnvil.unlocalisedName);
        CopperRecipes.init();
    }

    public static void loadAllRecipes() {
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeLoader.loadRecipes(ModObject.blockAnvil.unlocalisedName);
        CopperRecipes.init();
    }

    public static void reloadRecipes() {
        MachineRecipeRegistry.clearAll();

        loadAllRecipes();

        System.out.println("[RecipeLoader] Recipes reloaded.");
    }
}
