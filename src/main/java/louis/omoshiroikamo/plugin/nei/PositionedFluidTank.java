/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser Public License v3 which accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to: SirSengir (original work), CovertJaguar, Player, Binnie,
 * MysteriousAges
 ******************************************************************************/
package louis.omoshiroikamo.plugin.nei;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;

public class PositionedFluidTank {

    public FluidTank[] tanks;
    public FluidTank tank;
    public Rectangle position;
    public String overlayTexture;
    public Point overlayTexturePos;
    public boolean flowingTexture = false;
    public boolean showAmount = true;
    public boolean perTick = false;
    public float chance;
    private final List<String> tooltip = new ArrayList<>();

    public PositionedFluidTank(FluidTank[] tanks, Rectangle position, String overlayTexture, Point overlayTexturePos) {
        this.position = position;
        this.tanks = tanks;
        this.tank = tanks[0];
        this.overlayTexture = overlayTexture;
        this.overlayTexturePos = overlayTexturePos;
    }

    public PositionedFluidTank(Collection<FluidStack> fluids, int capacity, Rectangle position, String overlayTexture,
        Point overlayTexturePos) {
        this(createFluidTanks(capacity, fluids), position, overlayTexture, overlayTexturePos);
    }

    public PositionedFluidTank(FluidStack fluid, int capacity, Rectangle position, String overlayTexture,
        Point overlayTexturePos) {
        this(createFluidTanks(capacity, Collections.singletonList(fluid)), position, overlayTexture, overlayTexturePos);
    }

    public PositionedFluidTank(FluidStack fluid, int capacity, Rectangle position) {
        this(fluid, capacity, position, null, null);
    }

    private static FluidTank[] createFluidTanks(int capacity, Collection<FluidStack> fluidStacks) {
        FluidTank[] tanks = new FluidTank[fluidStacks.size()];
        int i = 0;
        for (FluidStack fluidStack : fluidStacks) {
            tanks[i++] = new FluidTank(fluidStacks != null ? fluidStack.copy() : null, capacity);
        }
        return tanks;
    }

    public List<String> handleTooltip(List<String> currenttip) {
        if (this.tank == null || this.tank.getFluid() == null
            || this.tank.getFluid()
                .getFluid() == null
            || this.tank.getFluid().amount <= 0) {
            return currenttip;
        }
        currenttip.add(
            this.tank.getFluid()
                .getLocalizedName());
        if (this.showAmount) {
            currenttip
                .add(EnumChatFormatting.GRAY.toString() + this.tank.getFluid().amount + (this.perTick ? "L/t" : "L"));
        }
        if (!this.tooltip.isEmpty()) {
            for (String tip : this.tooltip) {
                currenttip.add(tip);
            }
        }
        return currenttip;
    }

    public PositionedFluidTank addToTooltip(List<String> lines) {
        for (String tip : lines) {
            this.tooltip.add(tip);
        }
        return this;
    }

    public PositionedFluidTank addToTooltip(String line) {
        this.tooltip.add(line);
        return this;
    }

    public boolean transfer(boolean usage) {
        if (this.tank.getFluid() != null && this.tank.getFluid().amount > 0) {
            if (usage) {
                if (!GuiUsageRecipe.openRecipeGui("liquid", this.tank.getFluid())) {
                    return false;
                }
            } else {
                if (!GuiCraftingRecipe.openRecipeGui("liquid", this.tank.getFluid())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void draw() {
        if (this.tank == null || this.tank.getFluid() == null
            || this.tank.getFluid()
                .getFluid() == null
            || this.tank.getFluid().amount <= 0) {
            return;
        }

        IIcon icon;
        if (this.flowingTexture && this.tank.getFluid()
            .getFluid()
            .getFlowingIcon() != null) {
            icon = this.tank.getFluid()
                .getFluid()
                .getFlowingIcon();
        } else {
            icon = this.tank.getFluid()
                .getFluid()
                .getStillIcon();
        }

        if (icon == null) {
            return;
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int color = this.tank.getFluid()
            .getFluid()
            .getColor(this.tank.getFluid());
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, 1.0f);

        Gui gui = new Gui();
        gui.drawTexturedModelRectFromIcon(this.position.x, this.position.y, icon, 16, 16);

        GL11.glDisable(GL11.GL_BLEND);

        if (this.overlayTexture != null && this.overlayTexturePos != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GuiDraw.changeTexture(this.overlayTexture);
            GuiDraw.drawTexturedModalRect(
                this.position.x,
                this.position.y,
                this.overlayTexturePos.x,
                this.overlayTexturePos.y,
                this.position.width,
                this.position.height);
        }
        if (this.showAmount && this.tank.getFluid() != null) {
            String amountStr = this.tank.getFluid().amount + "L";

            float scale = 0.5f;
            int realX = this.position.x + this.position.width / 2;
            int realY = this.position.y + this.position.height - 5;

            GL11.glPushMatrix();
            GL11.glTranslatef(realX, realY, 0.0f);
            GL11.glScalef(scale, scale, 1.0f);

            GuiDraw.drawStringC(amountStr, 0, 0, 0xFFFFFF, true);

            GL11.glPopMatrix();
        }
    }

    public void drawChance() {
        if (chance <= 0.0f || chance >= 1.0f) {
            return;
        }
        float scale = 0.6f;
        String text = String.format("%.0f%%", chance * 100f);
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int stringWidth = font.getStringWidth(text);

        float inverse = 1f / scale;

        int x = this.position.x + 1;
        int y = this.position.y + 1;

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1.0f);
        font.drawStringWithShadow(
            text,
            (int) ((x + 16 - stringWidth * scale) * inverse),
            (int) (y * inverse),
            0xFFFFFF);
        GL11.glPopMatrix();
    }

    public void setPermutationToRender(int index) {
        this.tank = this.tanks[index];
    }

    public int getPermutationCount() {
        return this.tanks.length;
    }

    public PositionedFluidTank setChance(float chance) {
        this.chance = Math.max(0.0f, Math.min(1.0f, chance));
        if (chance <= 0.0F) {
            this.tooltip.add(
                EnumChatFormatting.GRAY
                    + String.format(NEIUtils.translate("chance"), NEIUtils.translate("chance.never")));
        } else if (chance < 0.01F) {
            this.tooltip.add(
                EnumChatFormatting.GRAY
                    + String.format(NEIUtils.translate("chance"), NEIUtils.translate("chance.lessThan1")));
        } else if (chance != 1.0F) {
            NumberFormat percentFormat = NumberFormat.getPercentInstance();
            percentFormat.setMaximumFractionDigits(2);
            this.tooltip.add(
                EnumChatFormatting.GRAY
                    + String.format(NEIUtils.translate("chance"), String.valueOf(percentFormat.format(chance))));
        }
        return this;
    }
}
