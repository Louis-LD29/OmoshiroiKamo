package com.louis.test.common.recipes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.louis.test.common.core.helper.Logger;
import com.louis.test.common.core.lib.LibMisc;

import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeLoader {

    public static final File RECIPE_FOLDER = new File("config/" + LibMisc.MOD_ID + "/recipes");
    private static final Gson gson = new Gson();

    public static int loadRecipes(String name) {
        if (!RECIPE_FOLDER.exists()) {
            RECIPE_FOLDER.mkdirs();
        }

        if (!name.endsWith(".json")) {
            name = name + ".json";
        }

        File file = new File(RECIPE_FOLDER, name);

        if (!file.exists()) {
            String resourcePath = "/assets/" + LibMisc.MOD_ID.toLowerCase() + "/recipes/" + name;
            try (InputStream in = RecipeLoader.class.getResourceAsStream(resourcePath)) {
                if (in != null) {
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        Logger.warn("Copied recipe file from resource: " + name);
                    }
                } else {
                    Logger.warn("Recipe resource not found: " + resourcePath);
                    return 0;
                }
            } catch (IOException e) {
                Logger.warn("Failed to copy recipe: " + e);
                return 0;
            }
        }

        return loadRecipesFromFile(file);
    }

    public static int loadRecipesFromFile(File file) {
        int loaded = 0;

        try (FileReader reader = new FileReader(file)) {
            List<JsonRecipe> recipes = gson.fromJson(reader, new TypeToken<List<JsonRecipe>>() {}.getType());

            if (recipes == null) {
                Logger.warn("No recipes found in file: " + file.getName());
                return 0;
            }

            int index = 0;
            for (JsonRecipe r : recipes) {
                index++;

                if (r == null || r.machine == null || (r.enabled != null && !r.enabled)) continue;

                RecipeBuilder builder = new RecipeBuilder();

                // --- Item Inputs ---
                if (r.itemInputs != null) {
                    for (JsonStack i : r.itemInputs) {
                        if (i.oredict != null && !i.oredict.isEmpty()) {
                            List<ItemStack> oreStacks = OreDictionary.getOres(i.oredict);
                            if (!oreStacks.isEmpty()) {
                                builder.addItemInput(i.oredict, i.amount);
                            } else {
                                Logger.warn("[WARN] oreDict NOT FOUND: " + i.oredict);
                            }
                            continue;
                        }

                        int meta = (i.meta >= 0) ? i.meta : OreDictionary.WILDCARD_VALUE;

                        Item item = GameRegistry.findItem(i.modid, i.name);
                        if (item != null) {
                            builder.addItemInput(new ItemStack(item, i.amount, meta));
                            continue;
                        }

                        Block block = GameRegistry.findBlock(i.modid, i.name);
                        if (block != null) {
                            builder.addItemInput(new ItemStack(block, i.amount, meta));
                            continue;
                        }

                        Logger.warn("[WARN] Item/Block input NOT FOUND: " + i.modid + ":" + i.name);
                    }
                }

                // --- Fluid Inputs ---
                if (r.fluidInputs != null) {
                    for (JsonStack f : r.fluidInputs) {
                        Fluid fluid = FluidRegistry.getFluid(f.name);
                        if (fluid != null) {
                            builder.addFluidInput(fluid, f.amount);
                        } else {
                            Logger.warn("[WARN] Fluid input NOT FOUND: " + f.modid + ":" + f.name);
                        }
                    }
                }

                // --- Item Outputs ---
                if (r.itemOutputs != null) {
                    for (JsonStack i : r.itemOutputs) {
                        if (i.oredict != null && !i.oredict.isEmpty()) {
                            List<ItemStack> oreStacks = OreDictionary.getOres(i.oredict);
                            if (!oreStacks.isEmpty()) {
                                builder.addItemOutput(i.oredict, i.amount);
                            } else {
                                Logger.warn("[WARN] oreDict output NOT FOUND: " + i.oredict);
                            }
                            continue;
                        }

                        int meta = (i.meta >= 0) ? i.meta : OreDictionary.WILDCARD_VALUE;

                        Item item = GameRegistry.findItem(i.modid, i.name);
                        if (item != null) {
                            builder.addItemOutput(new ItemStack(item, i.amount, meta));
                            continue;
                        }

                        Block block = GameRegistry.findBlock(i.modid, i.name);
                        if (block != null) {
                            builder.addItemOutput(new ItemStack(block, i.amount, meta));
                            continue;
                        }

                        Logger.warn("[WARN] Item/Block output NOT FOUND: " + i.modid + ":" + i.name);
                    }
                }

                // --- Fluid Outputs ---
                if (r.fluidOutputs != null) {
                    for (JsonStack f : r.fluidOutputs) {
                        Fluid fluid = FluidRegistry.getFluid(f.name);
                        if (fluid != null) {
                            builder.addFluidOutput(fluid, f.amount);
                        } else {
                            Logger.warn("[WARN] Fluid output NOT FOUND: " + f.modid + ":" + f.name);
                        }
                    }
                }

                // --- Optional Fields ---
                if (r.energyCost != null && r.energyCost > 0) {
                    builder.setEnergyCost(r.energyCost);
                }

                if (r.requiredTemperature != null && r.requiredTemperature > 0) {
                    builder.setTemperature(r.requiredTemperature);
                }

                if (r.requiredPressure != null && r.requiredPressure > 0) {
                    builder.setPressure(r.requiredPressure);
                }

                // --- Create UID ---
                String uid = r.machine + ":" + hashInputsAndOutputs(builder);
                builder.setUid(uid);

                // --- Regiter recipe ---
                MachineRecipeRegistry.register(r.machine, builder);
                loaded++;

            }

            Logger.warn("[INFO] Loaded " + loaded + " recipes from: " + file.getName());

        } catch (Exception e) {
            Logger.warn("[ERROR] Failed to load recipes from " + file.getName());
            e.printStackTrace();
        }

        return loaded;
    }

    public static String hashInputsAndOutputs(RecipeBuilder builder) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Ghép toàn bộ thông tin input/output lại thành 1 chuỗi
            StringBuilder data = new StringBuilder();

            if (builder.getItemInputs() != null) {
                for (ItemStack stack : builder.getItemInputs()) {
                    data.append(
                        stack.getItem()
                            .getUnlocalizedName())
                        .append(":")
                        .append(stack.stackSize)
                        .append(";");
                }
            }

            if (builder.getFluidInputs() != null) {
                for (FluidStack fluid : builder.getFluidInputs()) {
                    data.append(
                        fluid.getFluid()
                            .getName())
                        .append(":")
                        .append(fluid.amount)
                        .append(";");
                }
            }

            if (builder.getItemOutputs() != null) {
                for (ItemStack stack : builder.getItemOutputs()) {
                    data.append(
                        stack.getItem()
                            .getUnlocalizedName())
                        .append(":")
                        .append(stack.stackSize)
                        .append(";");
                }
            }

            if (builder.getFluidOutputs() != null) {
                for (FluidStack fluid : builder.getFluidOutputs()) {
                    data.append(
                        fluid.getFluid()
                            .getName())
                        .append(":")
                        .append(fluid.amount)
                        .append(";");
                }
            }

            data.append("T=")
                .append(builder.getTemperature());
            data.append(";P=")
                .append(builder.getPressure());
            data.append(";E=")
                .append(builder.getEnergyCost());

            // Băm SHA-256 → rút gọn thành 8 ký tự hex
            byte[] hash = digest.digest(
                data.toString()
                    .getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 4; i++) { // lấy 4 byte đầu → 8 ký tự
                hex.append(String.format("%02x", hash[i]));
            }

            return hex.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "invalidhash";
        }
    }

    static class JsonStack {

        public String modid;
        public String oredict;
        public String name;
        public int meta;
        public int amount;
    }

    static class JsonRecipe {

        public String machine;
        public Boolean enabled;
        public List<JsonStack> itemInputs;
        public List<JsonStack> fluidInputs;
        public List<JsonStack> itemOutputs;
        public List<JsonStack> fluidOutputs;
        public Integer energyCost;
        public Integer requiredTemperature;
        public Integer requiredPressure;

    }

}
