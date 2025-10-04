package louis.omoshiroikamo.common.block;

import net.minecraft.block.Block;

import louis.omoshiroikamo.common.block.anvil.BlockAnvil;
import louis.omoshiroikamo.common.block.electrolyzer.BlockElectrolyzer;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.furnace.BlockFurnace;
import louis.omoshiroikamo.common.block.material.BlockMaterial;
import louis.omoshiroikamo.common.block.multiblock.BlockMultiBlock;
import louis.omoshiroikamo.common.block.multiblock.part.energy.BlockEnergyInOut;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.BlockFluidInOut;
import louis.omoshiroikamo.common.block.multiblock.part.item.BlockItemInOut;
import louis.omoshiroikamo.common.ore.OreRegister;

public class ModBlocks {

    public static Block blockConnectable;
    public static Block blockMaterial;
    public static Block blockAnvil;
    public static Block blockFurnace;
    public static Block blockMultiBlock;
    public static Block blockSolar;
    public static Block blockBackpack;
    public static Block blockFluidInOut;
    public static Block blockEnergyInOut;
    public static Block blockItemInOut;
    public static Block blockElectrolyzer;

    public static void init() {
        // blockSolar = BlockSolarPanel.create();
        blockMultiBlock = BlockMultiBlock.create();
        blockFluidInOut = BlockFluidInOut.create();
        blockEnergyInOut = BlockEnergyInOut.create();
        blockItemInOut = BlockItemInOut.create();
        blockElectrolyzer = BlockElectrolyzer.create();
        blockConnectable = BlockConnectable.create();
        blockMaterial = BlockMaterial.create();
        blockAnvil = BlockAnvil.create();
        blockFurnace = BlockFurnace.create();
        OreRegister.init();
    }

}
