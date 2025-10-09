package louis.omoshiroikamo.common.recipes;

import static louis.omoshiroikamo.common.recipes.machine.RecipeLoader.hashInputsAndOutputs;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.recipes.machine.MachineRecipeRegistry;
import louis.omoshiroikamo.common.recipes.machine.RecipeBuilder;
import louis.omoshiroikamo.common.recipes.machine.RecipeLoader;
import louis.omoshiroikamo.common.recipes.ore.CopperRecipes;

public class ModRecipes {

    public static void init() {

        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeLoader.loadRecipes(ModObject.blockAnvil.unlocalisedName);
        loadVanillaFurnaceRecipes();
        CopperRecipes.init();

        ItemRecipes.init();

    }

    public static void loadAllRecipes() {
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeLoader.loadRecipes(ModObject.blockAnvil.unlocalisedName);
        loadVanillaFurnaceRecipes();
    }

    public static void reloadRecipes() {
        MachineRecipeRegistry.clearAll();

        loadAllRecipes();

        System.out.println("[RecipeLoader] Recipes reloaded.");
    }

    public static void loadVanillaFurnaceRecipes() {
        Map<ItemStack, ItemStack> recipes = FurnaceRecipes.smelting()
            .getSmeltingList();

        for (Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            ItemStack input = entry.getKey();
            ItemStack output = entry.getValue();

            RecipeBuilder builder = new RecipeBuilder();

            builder.addItemInput(input, 1f);
            builder.addItemOutput(output, 1f);

            int burnTime = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal, 1, 0)) / 8;
            builder.setEnergyCost(burnTime);

            String uid = ModObject.blockFurnace.unlocalisedName + ":" + hashInputsAndOutputs(builder);
            builder.setUid(uid);

            MachineRecipeRegistry.register(ModObject.blockFurnace.unlocalisedName, builder);
        }
    }
}
