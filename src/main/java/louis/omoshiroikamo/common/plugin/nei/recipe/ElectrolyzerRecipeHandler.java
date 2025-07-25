package louis.omoshiroikamo.common.plugin.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.electrolyzer.TEElectrolyzer;
import louis.omoshiroikamo.common.core.helper.OreDictUtils;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.plugin.nei.PositionedFluidTank;
import louis.omoshiroikamo.common.plugin.nei.PositionedStackAdv;
import louis.omoshiroikamo.common.plugin.nei.RecipeHandlerBase;
import louis.omoshiroikamo.common.recipes.MachineRecipe;
import louis.omoshiroikamo.common.recipes.MachineRecipeRegistry;
import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public class ElectrolyzerRecipeHandler extends RecipeHandlerBase {

    @Override
    public String getRecipeName() {
        return "Electrolyzer";
    }

    @Override
    public String getRecipeID() {
        return ModObject.blockElectrolyzer.getRegistryName();
    }

    @Override
    public String getGuiTexture() {
        return LibResources.GUI_NEI_BLANK;
    }

    @Override
    public void loadTransferRects() {
        this.addTransferRect(69, 15, 26, 25);
    }

    @Override
    public void loadAllRecipes() {
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            if (added.add(recipe)) {
                arecipes.add(new CachedElectrolyzerRecipe(recipe));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack item) {
        super.loadCraftingRecipes(item);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ChanceItemStack out : recipe.getItemOutputs()) {
                if (NEIServerUtils.areStacksSameTypeCrafting(out.stack, item)) {
                    if (added.add(recipe)) {
                        arecipes.add(new CachedElectrolyzerRecipe(recipe));
                    }
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(FluidStack fluid) {
        super.loadCraftingRecipes(fluid);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ChanceFluidStack out : recipe.getFluidOutputs()) {
                if (out != null && out.stack.isFluidEqual(fluid)) {
                    if (added.add(recipe)) {
                        arecipes.add(new CachedElectrolyzerRecipe(recipe));
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        super.loadUsageRecipes(ingredient);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ChanceItemStack in : recipe.getItemInputs()) {
                if (OreDictUtils.isOreDictMatch(in.stack, ingredient)
                    || NEIServerUtils.areStacksSameTypeCrafting(in.stack, ingredient)) {
                    if (added.add(recipe)) {
                        arecipes.add(new CachedElectrolyzerRecipe(recipe));
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(FluidStack fluid) {
        super.loadUsageRecipes(fluid);
        Set<MachineRecipe> added = new HashSet<>();
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ChanceFluidStack input : recipe.getFluidInputs()) {
                if (input != null && input.stack.isFluidEqual(fluid)) {
                    if (added.add(recipe)) {
                        arecipes.add(new CachedElectrolyzerRecipe(recipe));
                    }
                }
            }
        }
    }

    @Override
    public void drawExtras(int recipeIndex) {
        super.drawExtras(recipeIndex);
        CachedElectrolyzerRecipe recipe = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);

        int textX = 15;
        int textY = 52;
        int lineHeight = 9;
        int line = 0;

        GuiDraw.drawString(
            "Required: " + recipe.getEnergyCost() + " RF",
            textX,
            textY + lineHeight * line++,
            0x000000,
            false);

        GuiDraw.drawString(
            "Usage: " + recipe.getUsagePerTick() + " RF/t",
            textX,
            textY + lineHeight * line++,
            0x000000,
            false);

        GuiDraw.drawString(
            "Duration: " + recipe.getDuration() + " s",
            textX,
            textY + lineHeight * line++,
            0x000000,
            false);

        GuiDraw.drawString(
            "Temperature: " + (recipe.getRequiredTemperature() != 0
                ? recipe.getRequiredTemperature() + " K ~ " + (recipe.getRequiredTemperature() - 273) + " °C"
                : "Not Required"),
            textX,
            textY + lineHeight * line++,
            0x000000,
            false);

        // Vẽ progress bar
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(new ResourceLocation(LibResources.PREFIX_GUI + "nei/slot.png"));
        GuiDraw.drawTexturedModalRect(71, 21, 0, 18, 20, 20);
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);

        // Vẽ 3 ô item input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 8;
            drawItemSlot(x, y);
        }

        // Vẽ 3 ô item output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 8;
            drawItemSlot(x, y);
        }

        // Vẽ 3 ô fluid input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 32;
            drawFluidSlot(x, y);
        }

        // Vẽ 3 ô fluid output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 32;
            drawFluidSlot(x, y);
        }
    }

    public class CachedElectrolyzerRecipe extends CachedBaseRecipe {

        private final List<ChanceItemStack> itemInputs = new ArrayList<>();
        private final List<ChanceItemStack> itemOutputs = new ArrayList<>();
        private final List<ChanceFluidStack> fluidInputs = new ArrayList<>();
        private final List<ChanceFluidStack> fluidOutputs = new ArrayList<>();
        private final int energyCost;
        private final int usagePerTick;
        private final int requiredTemperature;
        private final float requiredPressure;

        public CachedElectrolyzerRecipe(MachineRecipe recipe) {
            if (recipe.getItemInputs() != null) itemInputs.addAll(recipe.getItemInputs());
            if (recipe.getItemOutputs() != null) itemOutputs.addAll(recipe.getItemOutputs());
            if (recipe.getFluidInputs() != null) fluidInputs.addAll(recipe.getFluidInputs());
            if (recipe.getFluidOutputs() != null) fluidOutputs.addAll(recipe.getFluidOutputs());
            TEElectrolyzer te = new TEElectrolyzer();

            this.energyCost = recipe.energyCost;
            this.usagePerTick = te.getPowerUsePerTick();
            this.requiredTemperature = recipe.requiredTemperature;
            this.requiredPressure = recipe.requiredPressure;

        }

        @Override
        public List<PositionedFluidTank> getFluidTanks() {
            List<PositionedFluidTank> tanks = new ArrayList<>();
            PositionedFluidTank tank = this.getFluidTank();
            if (tank != null) {
                tanks.add(tank);
            }
            for (int i = 0; i < Math.min(fluidInputs.size(), 3); i++) {
                ChanceFluidStack fs = fluidInputs.get(i);
                if (fs == null) continue;
                int x = 15 + i * 18;
                Rectangle rect = new Rectangle(x + 1, 32 + 1, 16, 16);
                tanks.add(new PositionedFluidTank(fs.stack, 1000, rect).setChance(fs.chance));
            }
            for (int i = 0; i < Math.min(fluidOutputs.size(), 3); i++) {
                ChanceFluidStack fs = fluidOutputs.get(i);
                if (fs == null) continue;
                int x = 95 + i * 18;
                Rectangle rect = new Rectangle(x + 1, 32 + 1, 16, 16);
                tanks.add(new PositionedFluidTank(fs.stack, 1000, rect).setChance(fs.chance));
            }
            return tanks;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < Math.min(itemInputs.size(), 3); i++) {
                ChanceItemStack is = itemInputs.get(i);
                if (is == null) continue;
                int x = 15 + i * 18;
                result.add(new PositionedStackAdv(is.stack, x + 1, 8 + 1).setChance(is.chance));
            }
            return result;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < Math.min(itemOutputs.size(), 3); i++) {
                ChanceItemStack is = itemOutputs.get(i);
                if (is == null) continue;
                int x = 95 + i * 18;
                result.add(new PositionedStackAdv(is.stack, x + 1, 8 + 1).setChance(is.chance));
            }
            return result;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        public int getEnergyCost() {
            return energyCost;
        }

        public int getUsagePerTick() {
            return usagePerTick;
        }

        public float getDuration() {
            return usagePerTick > 0 ? (energyCost / (float) usagePerTick) / 20f : 0;
        }

        public float getRequiredPressure() {
            return requiredPressure;
        }

        public int getRequiredTemperature() {
            return requiredTemperature;
        }
    }
}
