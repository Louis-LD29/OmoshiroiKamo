package com.louis.test.common.block.electrolyzer;

import java.awt.*;

import net.minecraft.inventory.Container;

import crazypants.enderio.machine.gui.GuiPoweredMachineBase;
import crazypants.enderio.machine.vat.TileVat;

public class GuiElectrolyzer extends GuiPoweredMachineBase<TileVat> {

    public GuiElectrolyzer(TileVat machine, Container container, String... guiTexture) {
        super(machine, container, guiTexture);
    }

}
