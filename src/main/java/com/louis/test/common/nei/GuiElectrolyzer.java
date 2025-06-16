package com.louis.test.common.nei;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiElectrolyzer extends GuiContainer {

    public GuiElectrolyzer() {
        super(new DummyContainer());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Electrolyzer", 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Nếu có texture thì vẽ nền ở đây, nếu không cứ để trống
    }

    // Container trống để NEI chấp nhận
    private static class DummyContainer extends Container {

        @Override
        public boolean canInteractWith(EntityPlayer playerIn) {
            return true;
        }
    }
}
