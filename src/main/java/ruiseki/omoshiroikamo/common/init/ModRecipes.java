package ruiseki.omoshiroikamo.common.init;

import static ruiseki.omoshiroikamo.common.recipe.machine.RecipeHandler.hashInputsAndOutputs;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.recipe.BlockRecipes;
import ruiseki.omoshiroikamo.common.recipe.ItemRecipes;
import ruiseki.omoshiroikamo.common.recipe.machine.MachineRecipeRegistry;
import ruiseki.omoshiroikamo.common.recipe.machine.RecipeBuilder;
import ruiseki.omoshiroikamo.common.recipe.machine.RecipeHandler;
import ruiseki.omoshiroikamo.common.recipe.ore.CopperRecipes;
import ruiseki.omoshiroikamo.common.recipe.voidMiner.VoidMinerRecipes;

public class ModRecipes {

    public static void init() {

        RecipeHandler.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeHandler.loadRecipes(ModObject.blockAnvil.unlocalisedName);
        loadVanillaFurnaceRecipes();
        CopperRecipes.init();

        ItemRecipes.init();
        BlockRecipes.init();
        VoidMinerRecipes.init();
    }

    public static void loadAllRecipes() {
        RecipeHandler.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
        RecipeHandler.loadRecipes(ModObject.blockAnvil.unlocalisedName);
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
