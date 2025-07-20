package louis.omoshiroikamo.common.block;

import net.minecraft.block.Block;

import louis.omoshiroikamo.common.block.basicblock.electrolyzer.BlockElectrolyzer;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.material.BlockMaterial;
import louis.omoshiroikamo.common.block.multiblock.BlockMultiBlock;
import louis.omoshiroikamo.common.block.multiblock.part.energy.BlockEnergyInOut;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.BlockFluidInOut;
import louis.omoshiroikamo.common.block.ore.OreRegister;

public class ModBlocks {

    public static Block blockConnectable;
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
        // blockTest = BlockTest.create();
        // blockSolar = BlockSolarPanel.create();
        // blockBoiler = BlockBoiler.create();
        // blockFluidInput = BlockFluidInput.create();
        // blockFluidOutput = BlockFluidOutput.create();
        // blockItemInput = BlockItemInput.create();
        // blockItemOutput = BlockItemOutput.create();
        // blockHeatInput = BlockHeatInput.create();
        // blockHeatSource = BlockHeatSource.create();
        // blockFluidFilter = BlockFluidFilter.create();
        blockMultiBlock = BlockMultiBlock.create();
        blockFluidInOut = BlockFluidInOut.create();
        blockEnergyInOut = BlockEnergyInOut.create();
        blockElectrolyzer = BlockElectrolyzer.create();
        blockConnectable = BlockConnectable.create();
        blockMaterial = BlockMaterial.create();
        OreRegister.init();
    }

}
