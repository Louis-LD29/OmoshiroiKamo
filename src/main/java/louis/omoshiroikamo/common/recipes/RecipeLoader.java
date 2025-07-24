package louis.omoshiroikamo.common.recipes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

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
                    }
                } else {
                    return 0;
                }
            } catch (IOException e) {
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
                        int meta = (i.meta != null) ? i.meta : 0;
                        int amount = (i.amount != null) ? i.amount : 1;
                        float chance = (i.chance != null) ? i.chance : 1.0f;

                        if (i.oredict != null && !i.oredict.isEmpty()) {
                            List<ItemStack> oreStacks = OreDictionary.getOres(i.oredict);
                            if (!oreStacks.isEmpty()) {
                                builder.addItemInput(i.oredict, amount, chance);
                            }
                            continue;
                        }

                        Item item = GameRegistry.findItem(i.modid, i.name);
                        if (item != null) {
                            builder.addItemInput(new ItemStack(item, amount, meta), chance);
                            continue;
                        }

                        Block block = GameRegistry.findBlock(i.modid, i.name);
                        if (block != null) {
                            builder.addItemInput(new ItemStack(block, amount, meta), chance);
                            continue;
                        }

                    }
                }

                // --- Fluid Inputs ---
                if (r.fluidInputs != null) {
                    for (JsonStack f : r.fluidInputs) {
                        int amount = (f.amount != null) ? f.amount : 1;
                        float chance = (f.chance != null) ? f.chance : 1.0f;
                        Fluid fluid = FluidRegistry.getFluid(f.name);
                        if (fluid != null) {
                            builder.addFluidInput(new FluidStack(fluid, amount), chance);
                        }
                    }
                }

                // --- Item Outputs ---
                if (r.itemOutputs != null) {
                    for (JsonStack i : r.itemOutputs) {
                        int meta = (i.meta != null) ? i.meta : 0;
                        int amount = (i.amount != null) ? i.amount : 1;
                        float chance = (i.chance != null) ? i.chance : 1.0f;

                        if (i.oredict != null && !i.oredict.isEmpty()) {
                            List<ItemStack> oreStacks = OreDictionary.getOres(i.oredict);
                            if (!oreStacks.isEmpty()) {
                                builder.addItemOutput(i.oredict, amount, chance);
                            }
                            continue;
                        }

                        Item item = GameRegistry.findItem(i.modid, i.name);
                        if (item != null) {
                            builder.addItemOutput(new ItemStack(item, amount, meta), chance);
                            continue;
                        }

                        Block block = GameRegistry.findBlock(i.modid, i.name);
                        if (block != null) {
                            builder.addItemOutput(new ItemStack(block, amount, meta), chance);
                            continue;
                        }
                    }
                }

                // --- Fluid Outputs ---
                if (r.fluidOutputs != null) {
                    for (JsonStack f : r.fluidOutputs) {
                        int amount = (f.amount != null) ? f.amount : 1;
                        float chance = (f.chance != null) ? f.chance : 1.0f;
                        Fluid fluid = FluidRegistry.getFluid(f.name);
                        if (fluid != null) {
                            builder.addFluidOutput(new FluidStack(fluid, amount), chance);
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

        } catch (Exception e) {
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
                for (ChanceItemStack chanceStack : builder.getItemInputs()) {
                    data.append(
                        chanceStack.stack.getItem()
                            .getUnlocalizedName())
                        .append(":")
                        .append(chanceStack.stack.stackSize)
                        .append(";");
                }
            }

            if (builder.getFluidInputs() != null) {
                for (ChanceFluidStack chancStack : builder.getFluidInputs()) {
                    data.append(
                        chancStack.stack.getFluid()
                            .getName())
                        .append(":")
                        .append(chancStack.stack.amount)
                        .append(";");
                }
            }

            if (builder.getItemOutputs() != null) {
                for (ChanceItemStack chanceStack : builder.getItemOutputs()) {
                    data.append(
                        chanceStack.stack.getItem()
                            .getUnlocalizedName())
                        .append(":")
                        .append(chanceStack.stack.stackSize)
                        .append(";");
                }
            }

            if (builder.getFluidOutputs() != null) {
                for (ChanceFluidStack chancStack : builder.getFluidOutputs()) {
                    data.append(
                        chancStack.stack.getFluid()
                            .getName())
                        .append(":")
                        .append(chancStack.stack.amount)
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
        public Integer meta;
        public Integer amount;
        public Float chance;
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
