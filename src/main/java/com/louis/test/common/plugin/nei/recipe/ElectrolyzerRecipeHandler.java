package com.louis.test.common.plugin.nei.recipe;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.basicblock.electrolyzer.TileElectrolyzer;
import com.louis.test.common.core.lib.LibResources;
import com.louis.test.common.plugin.nei.PositionedFluidTank;
import com.louis.test.common.plugin.nei.RecipeHandlerBase;
import com.louis.test.common.recipes.MachineRecipe;
import com.louis.test.common.recipes.MachineRecipeRegistry;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

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
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            arecipes.add(new CachedElectrolyzerRecipe(recipe));
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack item) {
        super.loadCraftingRecipes(item);
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ItemStack out : recipe.getItemOutputs()) {
                if (out != null && NEIServerUtils.areStacksSameTypeCrafting(out, item)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(FluidStack fluid) {
        super.loadCraftingRecipes(fluid);
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (FluidStack out : recipe.getFluidOutputs()) {
                if (out != null && out.isFluidEqual(fluid)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        super.loadUsageRecipes(ingredient);
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ItemStack in : recipe.getItemInputs()) {
                if (in != null && NEIServerUtils.areStacksSameTypeCrafting(in, ingredient)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(FluidStack fluid) {
        super.loadUsageRecipes(fluid);
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (FluidStack input : recipe.getFluidInputs()) {
                if (input != null && input.isFluidEqual(fluid)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
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
            .bindTexture(new ResourceLocation("test:textures/gui/nei/slot.png"));
        GuiDraw.drawTexturedModalRect(71, 21, 0, 18, 20, 20);
        drawProgressBar(71, 21, 0, 38, 20, 20, (int) recipe.getDuration(), 0);
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);

        // Vẽ 3 ô item input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 8;
            drawItemSlotBackground(x, y);
        }

        // Vẽ 3 ô item output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 8;
            drawItemSlotBackground(x, y);
        }

        // Vẽ 3 ô fluid input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 32;
            drawFluidSlotBackground(x, y);
        }

        // Vẽ 3 ô fluid output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 32;
            drawFluidSlotBackground(x, y);
        }
    }

    @Override
    public void drawFluidTanks(int recipeIndex) {
        super.drawFluidTanks(recipeIndex);
        CachedElectrolyzerRecipe recipe = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);
        if (recipe.getFluidInputs() != null) {
            for (PositionedFluidTank fluidTank : recipe.getFluidInputs()) {
                fluidTank.draw();
            }
        }

        if (recipe.getFluidOutputs() != null) {
            for (PositionedFluidTank fluidTank : recipe.getFluidOutputs()) {
                fluidTank.draw();
            }
        }
    }

    @Override
    public List<String> provideTooltip(GuiRecipe<?> guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe,
        Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        CachedElectrolyzerRecipe recipe = (CachedElectrolyzerRecipe) crecipe;
        if (recipe.getFluidInputs() != null) {
            for (PositionedFluidTank tank : recipe.getFluidInputs()) {
                if (tank.position.contains(relMouse)) {
                    tank.handleTooltip(currenttip);
                }
            }
        }

        if (recipe.getFluidOutputs() != null) {
            for (PositionedFluidTank tank : recipe.getFluidOutputs()) {
                if (tank.position.contains(relMouse)) {
                    tank.handleTooltip(currenttip);
                }
            }
        }

        return currenttip;
    }

    @Override
    protected boolean transferFluidTank(GuiRecipe<?> gui, int recipeIndex, boolean usage) {
        super.transferFluidTank(gui, recipeIndex, usage);
        CachedElectrolyzerRecipe recipe = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);
        Point pos = GuiDraw.getMousePosition();
        Point offset = gui.getRecipePosition(recipeIndex);
        Point relMouse = new Point(pos.x - gui.guiLeft - offset.x, pos.y - gui.guiTop - offset.y);

        if (recipe.getFluidInputs() != null) {
            for (PositionedFluidTank tank : recipe.getFluidInputs()) {
                if (tank.position.contains(relMouse)) {
                    return tank.transfer(usage);
                }
            }
        }

        if (recipe.getFluidOutputs() != null) {
            for (PositionedFluidTank tank : recipe.getFluidOutputs()) {
                if (tank.position.contains(relMouse)) {
                    return tank.transfer(usage);
                }
            }
        }

        return false;
    }

    public class CachedElectrolyzerRecipe extends CachedBaseRecipe {

        private final List<ItemStack> itemInputs = new ArrayList<>();
        private final List<ItemStack> itemOutputs = new ArrayList<>();
        private final List<FluidStack> fluidInputs = new ArrayList<>();
        private final List<FluidStack> fluidOutputs = new ArrayList<>();
        private final int energyCost;
        private final int usagePerTick;
        private final int requiredTemperature;
        private final float requiredPressure;

        public CachedElectrolyzerRecipe(MachineRecipe recipe) {
            if (recipe.getItemInputs() != null) itemInputs.addAll(recipe.getItemInputs());
            if (recipe.getItemOutputs() != null) itemOutputs.addAll(recipe.getItemOutputs());
            if (recipe.getFluidInputs() != null) fluidInputs.addAll(recipe.getFluidInputs());
            if (recipe.getFluidOutputs() != null) fluidOutputs.addAll(recipe.getFluidOutputs());
            TileElectrolyzer te = new TileElectrolyzer();

            this.energyCost = recipe.energyCost;
            this.usagePerTick = te.getPowerUsePerTick();
            this.requiredTemperature = recipe.requiredTemperature;
            this.requiredPressure = recipe.requiredPressure;

        }

        public List<PositionedFluidTank> getFluidInputs() {
            List<PositionedFluidTank> result = new ArrayList<>();
            for (int i = 0; i < Math.min(fluidInputs.size(), 3); i++) {
                FluidStack fs = fluidInputs.get(i);
                if (fs == null) continue;
                int x = 15 + i * 18;
                Rectangle rect = new Rectangle(x + 1, 32 + 1, 16, 16);
                result.add(new PositionedFluidTank(fs, 1000, rect));
            }
            return result;
        }

        public List<PositionedFluidTank> getFluidOutputs() {
            List<PositionedFluidTank> result = new ArrayList<>();
            for (int i = 0; i < Math.min(fluidOutputs.size(), 3); i++) {
                FluidStack fs = fluidOutputs.get(i);
                if (fs == null) continue;
                int x = 95 + i * 18;
                Rectangle rect = new Rectangle(x + 1, 32 + 1, 16, 16);
                result.add(new PositionedFluidTank(fs, 1000, rect));
            }
            return result;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < Math.min(itemInputs.size(), 3); i++) {
                ItemStack stack = itemInputs.get(i);
                if (stack == null) continue;
                int x = 15 + i * 18;
                result.add(new PositionedStack(stack, x + 1, 8 + 1));
            }
            return result;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < Math.min(itemOutputs.size(), 3); i++) {
                ItemStack stack = itemOutputs.get(i);
                if (stack == null) continue;
                int x = 95 + i * 18;
                result.add(new PositionedStack(stack, x + 1, 8 + 1));
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
            return (getEnergyCost() / getUsagePerTick()) / 20;
        }

        public float getRequiredPressure() {
            return requiredPressure;
        }

        public int getRequiredTemperature() {
            return requiredTemperature;
        }
    }
}
