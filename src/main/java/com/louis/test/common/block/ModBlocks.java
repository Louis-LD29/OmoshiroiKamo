package com.louis.test.common.block;

import net.minecraft.block.Block;

import com.louis.test.common.block.electrolyzer.BlockElectrolyzer;
import com.louis.test.common.block.heatsource.BlockHeatSource;
import com.louis.test.common.block.multiblock.boiler.BlockBoiler;
import com.louis.test.common.block.multiblock.fluid.BlockFluidFilter;
import com.louis.test.common.block.multiblock.fluid.BlockFluidInput;
import com.louis.test.common.block.multiblock.fluid.BlockFluidOutput;
import com.louis.test.common.block.multiblock.heat.BlockHeatInput;
import com.louis.test.common.block.solar.BlockSolarPanel;
import com.louis.test.common.block.test.BlockTest;

public class ModBlocks {

    public static Block blockTest;
    public static Block blockSolar;
    public static Block blockBoiler;
    public static Block blockFluidInput;
    public static Block blockFluidOutput;
    public static Block blockFluidFilter;
    public static Block blockHeatInput;
    public static Block blockHeatSource;
    public static Block blockElectrolyzer;

    public static void init() {
        blockTest = BlockTest.create();
        blockSolar = BlockSolarPanel.create();
        blockBoiler = BlockBoiler.create();
        blockFluidInput = BlockFluidInput.create();
        blockFluidOutput = BlockFluidOutput.create();
        blockFluidFilter = BlockFluidFilter.create();
        blockHeatInput = BlockHeatInput.create();
        blockHeatSource = BlockHeatSource.create();
        blockElectrolyzer = BlockElectrolyzer.create();
    }

}
