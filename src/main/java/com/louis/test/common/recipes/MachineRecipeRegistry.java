package com.louis.test.common.recipes;

import java.util.*;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

public class MachineRecipeRegistry {

    // Mỗi máy có 1 danh sách recipe riêng theo id
    private static final Map<String, List<MachineRecipe>> recipeMap = new HashMap<>();

    /**
     * Đăng ký 1 công thức cho máy
     */
    public static void register(String machineId, MachineRecipe recipe) {
        List<MachineRecipe> list = recipeMap.computeIfAbsent(machineId, k -> new ArrayList<>());
        list.add(recipe);
    }

    public static void register(String machineId, RecipeBuilder builder) {
        builder.setUid(machineId);
        register(machineId, builder.build());
    }

    /**
     * Đăng ký nhiều công thức cùng lúc
     */
    public static void registerAll(String machineId, List<MachineRecipe> recipes) {
        List<MachineRecipe> list = recipeMap.computeIfAbsent(machineId, k -> new ArrayList<>());
        list.addAll(recipes);
    }

    /**
     * Lấy toàn bộ công thức của 1 máy
     */
    public static List<MachineRecipe> getRecipes(String machineId) {
        return recipeMap.getOrDefault(machineId, Collections.emptyList());
    }

    /**
     * Tìm recipe theo UID
     */
    public static @Nullable MachineRecipe getRecipeForUid(String uid) {
        for (List<MachineRecipe> recipeList : recipeMap.values()) {
            for (MachineRecipe recipe : recipeList) {
                if (uid.equals(recipe.getUid())) {
                    return recipe;
                }
            }
        }
        return null;
    }

    /**
     * Tìm công thức phù hợp với input hiện tại
     */
    public static MachineRecipe findMatchingRecipe(String machineId, List<ItemStack> itemInputs,
        List<FluidStack> fluidInputs, int temperature, int pressure, int availableEnergy) {
        for (MachineRecipe recipe : getRecipes(machineId)) {
            if (recipe.matches(itemInputs, fluidInputs, temperature, pressure, availableEnergy)) {
                return recipe;
            }
        }
        return null;
    }

    public static MachineRecipe findMatchingRecipe(String machineId, List<ItemStack> itemInputs,
        List<FluidStack> fluidInputs) {
        return findMatchingRecipe(machineId, itemInputs, fluidInputs, 0, 0, 0);
    }

    /**
     * Xóa toàn bộ công thức của máy (nếu cần)
     */
    public static void clearRecipes(String machineId) {
        recipeMap.remove(machineId);
    }

    /**
     * Xóa toàn bộ công thức của tất cả các máy
     */
    public static void clearAll() {
        recipeMap.clear();
    }
}
