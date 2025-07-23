package louis.omoshiroikamo.common.plugin.nei.recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.core.helper.OreDictUtils;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.plugin.nei.RecipeHandlerBase;
import louis.omoshiroikamo.common.recipes.MachineRecipe;
import louis.omoshiroikamo.common.recipes.MachineRecipeRegistry;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public class AnvilRecipeHandler extends RecipeHandlerBase {

    @Override
    public String getRecipeName() {
        return "Anvil";
    }

    @Override
    public String getRecipeID() {
        return ModObject.blockAnvil.getRegistryName();
    }

    @Override
    public String getGuiTexture() {
        return LibResources.GUI_NEI_BLANK;
    }

    @Override
    public void loadAllRecipes() {
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockAnvil.unlocalisedName)) {
            if (added.add(recipe)) {
                arecipes.add(new CachedAnvilRecipe(recipe));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack item) {
        super.loadCraftingRecipes(item);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockAnvil.unlocalisedName)) {
            for (ChanceItemStack out : recipe.getItemOutputs()) {
                if (NEIServerUtils.areStacksSameTypeCrafting(out.stack, item)) {
                    if (added.add(recipe)) {
                        arecipes.add(new CachedAnvilRecipe(recipe));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        super.loadUsageRecipes(ingredient);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockAnvil.unlocalisedName)) {
            for (ChanceItemStack in : recipe.getItemInputs()) {
                if (OreDictUtils.isOreDictMatch(in.stack, ingredient)
                    || NEIServerUtils.areStacksSameTypeCrafting(in.stack, ingredient)) {

                    if (added.add(recipe)) {
                        arecipes.add(new CachedAnvilRecipe(recipe));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);
        CachedAnvilRecipe recipe = (CachedAnvilRecipe) arecipes.get(recipeIndex);

        int inputCount = recipe.itemInputs.size();
        int outputCount = recipe.itemOutputs.size();

        int inputRows = (inputCount + 2) / 3;
        int outputRows = (outputCount + 2) / 3;

        int maxRows = Math.max(inputRows, outputRows);

        drawStretchedItemSlot(15, 8, 54, maxRows * 18);
        drawStretchedItemSlot(95, 8, 54, maxRows * 18);
    }

    @Override
    public void drawExtras(int recipeIndex) {
        CachedAnvilRecipe recipe = (CachedAnvilRecipe) arecipes.get(recipeIndex);
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;

        float scale = 0.6f;
        float inverse = 1f / scale;

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1f);

        for (int i = 0; i < recipe.itemInputs.size(); i++) {
            ChanceItemStack is = recipe.itemInputs.get(i);
            if (is == null || is.chance >= 1.0f) continue;

            int row = i / 3;
            int col = i % 3;

            int baseX = 15 + col * 18;
            int baseY = 8 + row * 18;

            String text = String.format("%.0f%%", is.chance * 100);
            int stringWidth = font.getStringWidth(text);

            int x = (int) ((baseX + 17 - stringWidth * scale) * inverse);
            int y = (int) ((baseY + 2) * inverse);

            font.drawStringWithShadow(text, x, y, 0xFFFFFF);
        }

        for (int i = 0; i < recipe.itemOutputs.size(); i++) {
            ChanceItemStack is = recipe.itemOutputs.get(i);
            if (is == null || is.chance >= 1.0f) continue;

            int row = i / 3;
            int col = i % 3;

            int baseX = 95 + col * 18;
            int baseY = 8 + row * 18;

            String text = String.format("%.0f%%", is.chance * 100);
            int stringWidth = font.getStringWidth(text);

            int x = (int) ((baseX + 17 - stringWidth * scale) * inverse);
            int y = (int) ((baseY + 2) * inverse);

            font.drawStringWithShadow(text, x, y, 0xFFFFFF);
        }

        GL11.glPopMatrix();
    }

    public class CachedAnvilRecipe extends CachedBaseRecipe {

        private final List<ChanceItemStack> itemInputs = new ArrayList<>();
        private final List<ChanceItemStack> itemOutputs = new ArrayList<>();
        private final int energyCost;

        public CachedAnvilRecipe(MachineRecipe recipe) {
            if (recipe.getItemInputs() != null) itemInputs.addAll(recipe.getItemInputs());
            if (recipe.getItemOutputs() != null) itemOutputs.addAll(recipe.getItemOutputs());
            this.energyCost = recipe.energyCost;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < itemInputs.size(); i++) {
                ChanceItemStack is = itemInputs.get(i);
                if (is == null) continue;
                int col = i % 3;
                int row = i / 3;
                int x = 15 + col * 18;
                int y = 8 + row * 18;
                result.add(new PositionedStack(is.stack, x + 1, y + 1));
            }
            return result;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < itemOutputs.size(); i++) {
                ChanceItemStack is = itemOutputs.get(i);
                if (is == null) continue;
                int col = i % 3;
                int row = i / 3;
                int x = 95 + col * 18;
                int y = 8 + row * 18;
                result.add(new PositionedStack(is.stack, x + 1, y + 1));
            }

            // Tính tổng chiều cao block input/output để căn giữa pickaxe
            int inputRows = (itemInputs.size() + 2) / 3;
            int outputRows = (itemOutputs.size() + 2) / 3;
            int maxHeight = Math.max(inputRows, outputRows) * 18;
            int centerY = 8 + (maxHeight - 18) / 2;

            result.add(new PositionedStack(new ItemStack(Items.diamond_pickaxe), 73, centerY + 1));
            return result;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        public int getEnergyCost() {
            return energyCost;
        }
    }
}
