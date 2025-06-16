package com.louis.test.common.nei;

import java.awt.*;
import java.util.*;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.enderio.core.common.util.FluidUtil;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.recipes.MachineRecipe;
import com.louis.test.common.recipes.MachineRecipeRegistry;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class ElectrolyzerRecipeHandler extends TemplateRecipeHandler {

    public class CachedElectrolyzerRecipe extends CachedRecipe {

        private final List<ItemStack> itemInputs = new ArrayList<>();
        private final List<ItemStack> itemOutputs = new ArrayList<>();
        private final List<FluidStack> fluidInputs = new ArrayList<>();
        private final List<FluidStack> fluidOutputs = new ArrayList<>();

        public CachedElectrolyzerRecipe(MachineRecipe recipe) {
            if (recipe.getItemInputs() != null) itemInputs.addAll(recipe.getItemInputs());
            if (recipe.getItemOutputs() != null) itemOutputs.addAll(recipe.getItemOutputs());
            if (recipe.getFluidInputs() != null) fluidInputs.addAll(recipe.getFluidInputs());
            if (recipe.getFluidOutputs() != null) fluidOutputs.addAll(recipe.getFluidOutputs());
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> result = new ArrayList<>();
            for (int i = 0; i < itemInputs.size(); i++) {
                int x = 15 + i * 18;
                int y = 6;
                result.add(new PositionedStack(itemInputs.get(i), x + 1, y + 1));
            }
            return result;
        }

        @Override
        public PositionedStack getResult() {
            return itemOutputs.isEmpty() ? null : new PositionedStack(itemOutputs.get(0), 95, 6);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> outputs = new ArrayList<>();
            for (int i = 1; i < itemOutputs.size(); i++) {
                outputs.add(new PositionedStack(itemOutputs.get(i), 95 + i * 18, 6 + 1));
            }
            return outputs;
        }
    }

    @Override
    public String getRecipeName() {
        return "Electrolyzer";
    }

    @Override
    public String getOverlayIdentifier() {
        return "Electrolyzer";
    }

    @Override
    public String getGuiTexture() {
        return "test:textures/gui/machine/base.png";
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(
            new TemplateRecipeHandler.RecipeTransferRect(
                new Rectangle(70, 23, 22, 17),
                "Electrolyzer",
                new Object[0]));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if ("liquid".equals(outputId)) {
            loadCraftingRecipes((FluidStack) results[0]);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(FluidStack fluid) {
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (FluidStack out : recipe.getFluidOutputs()) {
                if (out != null && out.isFluidEqual(fluid)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ItemStack out : recipe.getItemOutputs()) {
                if (out != null && NEIServerUtils.areStacksSameTypeCrafting(out, result)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if ("liquid".equals(inputId)) {
            loadUsageRecipes((FluidStack) ingredients[0]);
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.blockElectrolyzer)) {
            List<MachineRecipe> all = MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName);
            for (MachineRecipe recipe : all) {
                arecipes.add(new CachedElectrolyzerRecipe(recipe));
            }
        }

        FluidStack fluid = FluidUtil.getFluidFromItem(ingredient);
        if (fluid != null) {
            loadUsageRecipes(fluid);
        }

        Set<MachineRecipe> matched = new HashSet<>();

        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (ItemStack in : recipe.getItemInputs()) {
                if (in != null && NEIServerUtils.areStacksSameTypeCrafting(in, ingredient)) {
                    matched.add(recipe);
                }
            }
        }
        for (MachineRecipe recipe : matched) {
            arecipes.add(new CachedElectrolyzerRecipe(recipe));
        }

    }

    public void loadUsageRecipes(FluidStack fluid) {
        for (MachineRecipe recipe : MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)) {
            for (FluidStack input : recipe.getFluidInputs()) {
                if (input != null && input.isFluidEqual(fluid)) {
                    arecipes.add(new CachedElectrolyzerRecipe(recipe));
                }
            }
        }
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawBackground(recipeIndex);

        CachedElectrolyzerRecipe recipe = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);

        // Vẽ 3 ô item input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 6;
            drawItemSlotBackground(x, y);
        }

        // Vẽ 3 ô item output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 6;
            drawItemSlotBackground(x, y);
        }

        // Vẽ 3 ô fluid input
        for (int i = 0; i < 3; i++) {
            int x = 15 + i * 18;
            int y = 30;
            drawFluidSlotBackground(x, y);

            if (i < recipe.fluidInputs.size()) {
                FluidStack fluid = recipe.fluidInputs.get(i);
                drawFluidInSlot(fluid, x + 1, y + 1, 16, 16);
            }
        }

        // Vẽ 3 ô fluid output
        for (int i = 0; i < 3; i++) {
            int x = 95 + i * 18;
            int y = 30;
            drawFluidSlotBackground(x, y);

            if (i < recipe.fluidOutputs.size()) {
                FluidStack fluid = recipe.fluidOutputs.get(i);
                drawFluidInSlot(fluid, x + 1, y + 1, 16, 16);
            }
        }
    }

    private void drawItemSlotBackground(int x, int y) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(new ResourceLocation("test:textures/gui/machine/item.png"));
        drawCustomSizedRect(x, y, 0, 0, 18, 18, 18, 18);
    }

    private void drawFluidSlotBackground(int x, int y) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(new ResourceLocation("test:textures/gui/machine/fluid.png"));
        drawCustomSizedRect(x, y, 0, 0, 18, 18, 18, 18);
    }

    public static void drawCustomSizedRect(int x, int y, float u, float v, int width, int height, float texW,
        float texH) {
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x, y + height, 0, u / texW, (v + height) / texH);
        tess.addVertexWithUV(x + width, y + height, 0, (u + width) / texW, (v + height) / texH);
        tess.addVertexWithUV(x + width, y, 0, (u + width) / texW, v / texH);
        tess.addVertexWithUV(x, y, 0, u / texW, v / texH);
        tess.draw();
    }

    private void drawFluidInSlot(FluidStack fluid, int x, int y, int width, int height) {
        if (fluid == null || fluid.getFluid() == null) return;

        IIcon icon = fluid.getFluid()
            .getStillIcon();
        if (icon == null) return;

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color = fluid.getFluid()
            .getColor(fluid);
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, 1.0f);

        Gui gui = new Gui(); // 👈 tạo instance

        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < height; j += 16) {
                int drawWidth = Math.min(16, width - i);
                int drawHeight = Math.min(16, height - j);
                gui.drawTexturedModelRectFromIcon(x, y, icon, drawWidth, drawHeight);

            }
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currenttip, int recipeIndex) {
        CachedElectrolyzerRecipe rec = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);
        Point relMouse = getRelativeMouse();

        FluidStack hovered = getHoveredFluid(rec, relMouse.x, relMouse.y);
        if (hovered != null && hovered.getFluid() != null) {
            currenttip.add(
                String.format(
                    "%s: %d mB",
                    hovered.getFluid()
                        .getLocalizedName(hovered),
                    hovered.amount));
            return currenttip;
        }

        return super.handleTooltip(gui, currenttip, recipeIndex);
    }

    @Override
    public boolean keyTyped(GuiRecipe<?> gui, char keyChar, int keyCode, int recipeIndex) {
        boolean recipeKey = keyCode == Keyboard.KEY_R;
        boolean usageKey = keyCode == Keyboard.KEY_U;
        if (!recipeKey && !usageKey) return super.keyTyped(gui, keyChar, keyCode, recipeIndex);

        CachedElectrolyzerRecipe rec = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);
        Point relMouse = getRelativeMouse();

        FluidStack hovered = getHoveredFluid(rec, relMouse.x, relMouse.y);
        return transferFluidTank(hovered, usageKey);
    }

    @Override
    public boolean mouseClicked(GuiRecipe<?> gui, int button, int recipeIndex) {
        if (button == 0 || button == 1) {
            CachedElectrolyzerRecipe rec = (CachedElectrolyzerRecipe) arecipes.get(recipeIndex);
            Point relMouse = getRelativeMouse();
            FluidStack hovered = getHoveredFluid(rec, relMouse.x, relMouse.y);
            return transferFluidTank(hovered, button == 1);
        }
        return super.mouseClicked(gui, button, recipeIndex);
    }

    @Nullable
    private FluidStack getHoveredFluid(CachedElectrolyzerRecipe rec, int mouseX, int mouseY) {
        // Fluid Input
        for (int i = 0; i < rec.fluidInputs.size(); i++) {
            int x = 15 + i * 18;
            int y = 30;
            if (inRect(mouseX, mouseY, x + 1, y + 1, 16, 16)) {
                return rec.fluidInputs.get(i);
            }
        }

        // Fluid Output
        for (int i = 0; i < rec.fluidOutputs.size(); i++) {
            int x = 95 + i * 18;
            int y = 30;
            if (inRect(mouseX, mouseY, x + 1, y + 1, 16, 16)) {
                return rec.fluidOutputs.get(i);
            }
        }

        return null;
    }

    private Point getRelativeMouse() {
        Point mouse = GuiDraw.getMousePosition();
        Dimension displaySize = GuiDraw.displaySize();

        int ySize = Math.min(Math.max(displaySize.height - 68, 166), 370);
        int guiLeft = (displaySize.width - 176) / 2 + 5;
        int guiTop = (displaySize.height - ySize) / 2 + 40;

        return new Point(mouse.x - guiLeft, mouse.y - guiTop);
    }

    private boolean inRect(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private boolean transferFluidTank(FluidStack tank, boolean usage) {
        if (tank != null && tank.getFluid() != null && tank.amount > 0) {
            Object[] args = new Object[] { tank.copy() };
            if (usage) {
                return GuiUsageRecipe.openRecipeGui("liquid", args);
            } else {
                return GuiCraftingRecipe.openRecipeGui("liquid", args);
            }
        }
        return false;
    }

}
