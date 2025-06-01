package com.louis.test.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class CustomRecipeManager {

    private static final List<CustomRecipe> recipes = new ArrayList<>();

    public static void addRecipe(ItemStack input, ItemStack output) {
        recipes.add(new CustomRecipe(input, output));
    }

    public static CustomRecipe findMatchingRecipe(ItemStack input) {
        for (CustomRecipe recipe : recipes) {
            if (recipe.matches(input)) {
                return recipe;
            }
        }
        return null;
    }

    public static List<CustomRecipe> getAllRecipes() {
        return recipes;
    }
}
