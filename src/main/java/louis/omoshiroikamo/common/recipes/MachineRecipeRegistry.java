package louis.omoshiroikamo.common.recipes;

import java.util.*;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class MachineRecipeRegistry {

    private static final Map<String, List<MachineRecipe>> recipeMap = new HashMap<>();
    private static final Map<String, MachineRecipe> uidMap = new HashMap<>();

    /**
     * Register 1 recipe
     */
    public static void register(String machineId, MachineRecipe recipe) {
        recipeMap.computeIfAbsent(machineId, k -> new ArrayList<>())
            .add(recipe);

        // Cập nhật UID map nếu có UID
        String uid = recipe.getUid();
        if (uid != null && !uid.isEmpty()) {
            uidMap.put(uid, recipe);
        }
    }

    public static void register(String machineId, RecipeBuilder builder) {
        register(machineId, builder.build());
    }

    /**
     * Register multiple recipes
     */
    public static void registerAll(String machineId, List<MachineRecipe> recipes) {
        List<MachineRecipe> list = recipeMap.computeIfAbsent(machineId, k -> new ArrayList<>());
        for (MachineRecipe recipe : recipes) {
            list.add(recipe);
            String uid = recipe.getUid();
            if (uid != null && !uid.isEmpty()) {
                uidMap.put(uid, recipe);
            }
        }
    }

    /**
     * Get all recipes for a machine
     */
    public static List<MachineRecipe> getRecipes(String machineId) {
        return recipeMap.getOrDefault(machineId, Collections.emptyList());
    }

    /**
     * Find recipe by UID — O(1) lookup
     */
    public static @Nullable MachineRecipe getRecipeForUid(String uid) {
        return uidMap.get(uid);
    }

    /**
     * Find the best matching recipe
     */
    public static MachineRecipe findMatchingRecipe(String machineId, List<ItemStack> itemInputs,
        List<FluidStack> fluidInputs, int temperature, int pressure, int availableEnergy) {

        MachineRecipe bestMatch = null;
        int bestScore = -1;

        for (MachineRecipe recipe : getRecipes(machineId)) {
            if (recipe.matches(itemInputs, fluidInputs, temperature, pressure, availableEnergy)) {
                int score = recipe.getInputComplexityScore();
                if (score > bestScore) {
                    bestMatch = recipe;
                    bestScore = score;
                }
            }
        }

        return bestMatch;
    }

    /**
     * Find matching recipe (no temp/pressure/energy check)
     */
    public static MachineRecipe findMatchingRecipe(String machineId, List<ItemStack> itemInputs,
        List<FluidStack> fluidInputs) {
        return findMatchingRecipe(machineId, itemInputs, fluidInputs, 0, 0, 0);
    }

    /**
     * Remove all recipes for a specific machine
     */
    public static void clearRecipes(String machineId) {
        List<MachineRecipe> removed = recipeMap.remove(machineId);
        if (removed != null) {
            for (MachineRecipe recipe : removed) {
                String uid = recipe.getUid();
                if (uid != null && !uid.isEmpty()) {
                    uidMap.remove(uid);
                }
            }
        }
    }

    /**
     * Remove all recipes globally
     */
    public static void clearAll() {
        recipeMap.clear();
        uidMap.clear();
    }
}
