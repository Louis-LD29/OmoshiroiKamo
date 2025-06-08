package com.louis.test.common.block;

import net.minecraft.block.Block;

import com.louis.test.common.block.boiler.BlockBoiler;
import com.louis.test.common.block.multiblock.BlockFluidInput;
import com.louis.test.common.block.multiblock.BlockFluidOutput;
import com.louis.test.common.block.solar.BlockSolarPanel;
import com.louis.test.common.block.test.BlockTest;

public class ModBlocks {

    public static Block blockTest;
    public static Block blockBoiler;
    public static Block blockFluidInput;
    public static Block blockFluidOutput;
    public static Block blockSolar;

    public static void init() {
        blockTest = BlockTest.create();
        blockBoiler = BlockBoiler.create();
        blockFluidInput = BlockFluidInput.create();
        blockFluidOutput = BlockFluidOutput.create();
        blockSolar = BlockSolarPanel.create();
    }

}
