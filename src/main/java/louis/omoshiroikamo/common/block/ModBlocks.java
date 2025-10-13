package louis.omoshiroikamo.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.anvil.BlockAnvil;
import louis.omoshiroikamo.common.block.electrolyzer.BlockElectrolyzer;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.furnace.BlockFurnace;
import louis.omoshiroikamo.common.block.material.BlockMaterial;
import louis.omoshiroikamo.common.block.material.alabaster.BlockAlabaster;
import louis.omoshiroikamo.common.block.material.basalt.BlockBasalt;
import louis.omoshiroikamo.common.block.material.hardenedStone.BlockHardenedStone;
import louis.omoshiroikamo.common.block.material.structureFrame.BlockStructureFrame;
import louis.omoshiroikamo.common.block.multiblock.multiblockUpgrade.BlockModifier;
import louis.omoshiroikamo.common.block.multiblock.part.energy.BlockEnergyInOut;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.BlockFluidInOut;
import louis.omoshiroikamo.common.block.multiblock.part.item.BlockItemInOut;
import louis.omoshiroikamo.common.block.multiblock.solarArray.BlockSolarArray;
import louis.omoshiroikamo.common.block.multiblock.solarArray.BlockSolarCell;
import louis.omoshiroikamo.common.ore.OreRegister;

public class ModBlocks {

    public static Block blockConnectable;
    public static Block blockMaterial;
    public static Block blockAnvil;
    public static Block blockFurnace;

    public static Block blockSolarArray;
    public static Block blockSolarCell;
    public static Block blockStructureFrame;
    public static Block blockModifier;
    public static Block blockBasalt;
    public static Block blockAlabaster;
    public static Block blockHardenedStone;
    public static Block blockMica;

    public static Block blockFluidInOut;
    public static Block blockEnergyInOut;
    public static Block blockItemInOut;
    public static Block blockElectrolyzer;

    public static void init() {
        blockSolarArray = BlockSolarArray.create();
        blockSolarCell = BlockSolarCell.create();
        blockStructureFrame = BlockStructureFrame.create();
        blockModifier = BlockModifier.create();
        blockBasalt = BlockBasalt.create();
        blockAlabaster = BlockAlabaster.create();
        blockHardenedStone = BlockHardenedStone.create();
        blockMica = BlockOK.create(ModObject.blockMica, "mica", Material.rock);

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
