package com.louis.test.common.block;

import net.minecraft.block.Block;

import com.louis.test.common.block.electrolyzer.BlockElectrolyzer;
import com.louis.test.common.block.heatsource.BlockHeatSource;
import com.louis.test.common.block.material.BlockMaterial;
import com.louis.test.common.block.meta.BlockMeta;
import com.louis.test.common.block.multiblock.BlockMultiBlock;
import com.louis.test.common.block.multiblock.boiler.BlockBoiler;
import com.louis.test.common.block.multiblock.fluid.BlockFluidFilter;
import com.louis.test.common.block.multiblock.fluid.BlockFluidInput;
import com.louis.test.common.block.multiblock.fluid.BlockFluidOutput;
import com.louis.test.common.block.multiblock.heat.BlockHeatInput;
import com.louis.test.common.block.multiblock.part.energy.BlockEnergyInOut;
import com.louis.test.common.block.multiblock.part.fluid.BlockFluidInOut;
import com.louis.test.common.block.multiblock.part.item.BlockItemInput;
import com.louis.test.common.block.multiblock.part.item.BlockItemOutput;
import com.louis.test.common.block.solar.BlockSolarPanel;
import com.louis.test.common.block.test.BlockTest;

public class ModBlocks {

    public static Block blockMeta;
    public static Block blockMaterial;
    public static Block blockMultiBlock;
    public static Block blockTest;
    public static Block blockSolar;
    public static Block blockBoiler;
    public static Block blockFluidInput;
    public static Block blockFluidOutput;
    public static Block blockFluidInOut;
    public static Block blockEnergyInOut;
    public static Block blockFluidFilter;
    public static Block blockItemInput;
    public static Block blockItemOutput;
    public static Block blockHeatInput;
    public static Block blockHeatSource;
    public static Block blockElectrolyzer;

    public static void init() {
        blockMeta = BlockMeta.create();
        blockMaterial = BlockMaterial.create();
        blockMultiBlock = BlockMultiBlock.create();
        blockTest = BlockTest.create();
        blockSolar = BlockSolarPanel.create();
        blockBoiler = BlockBoiler.create();
        blockFluidInput = BlockFluidInput.create();
        blockFluidOutput = BlockFluidOutput.create();
        blockFluidInOut = BlockFluidInOut.create();
        blockFluidFilter = BlockFluidFilter.create();
        blockItemInput = BlockItemInput.create();
        blockItemOutput = BlockItemOutput.create();
        blockEnergyInOut = BlockEnergyInOut.create();
        blockHeatInput = BlockHeatInput.create();
        blockHeatSource = BlockHeatSource.create();
        blockElectrolyzer = BlockElectrolyzer.create();
    }

}
