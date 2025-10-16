package louis.omoshiroikamo.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.anvil.BlockAnvil;
import louis.omoshiroikamo.common.block.electrolyzer.BlockElectrolyzer;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.furnace.BlockFurnace;
import louis.omoshiroikamo.common.block.material.BlockMaterial;
import louis.omoshiroikamo.common.block.material.machineBase.BlockMachineBase;
import louis.omoshiroikamo.common.block.material.structureFrame.BlockStructureFrame;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierAccuracy;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierFireResistance;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierFlight;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierHaste;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierJumpBoost;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierNightVision;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierNull;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierPiezo;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierRegeneration;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierResistance;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierSaturation;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierSpeed;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierStrength;
import louis.omoshiroikamo.common.block.multiblock.modifier.BlockModifierWaterBreathing;
import louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.BlockNanoBotBeacon;
import louis.omoshiroikamo.common.block.multiblock.part.energy.BlockEnergyInOut;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.BlockFluidInOut;
import louis.omoshiroikamo.common.block.multiblock.part.item.BlockItemInOut;
import louis.omoshiroikamo.common.block.multiblock.solarArray.BlockSolarArray;
import louis.omoshiroikamo.common.block.multiblock.solarArray.cell.BlockSolarCell;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.core.BlockLaserCore;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.lens.BlockLaserLens;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.BlockVoidOreMiner;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.BlockVoidResMiner;
import louis.omoshiroikamo.common.ore.OreRegister;

public class ModBlocks {

    public static Block blockConnectable;
    public static Block blockMaterial;
    public static Block blockAnvil;
    public static Block blockFurnace;

    public static Block blockVoidOreMiner;
    public static Block blockVoidResMiner;
    public static Block blockNanoBotBeacon;
    public static Block blockLaserCore;
    public static Block blockLaserLens;
    public static Block blockSolarArray;
    public static Block blockSolarCell;
    public static Block blockStructureFrame;
    public static Block blockMachineBase;
    public static Block blockModifierNull;
    public static Block blockModifierAccuracy;
    public static Block blockModifierPiezo;
    public static Block blockModifierSpeed;
    public static Block blockModifierFlight;
    public static Block blockModifierNightVision;
    public static Block blockModifierHaste;
    public static Block blockModifierStrength;
    public static Block blockModifierWaterBreathing;
    public static Block blockModifierRegeneration;
    public static Block blockModifierSaturation;
    public static Block blockModifierResistance;
    public static Block blockModifierJumpBoost;
    public static Block blockModifierFireResistance;
    public static Block blockBasalt;
    public static Block blockAlabaster;
    public static Block blockHardenedStone;
    public static Block blockMica;

    public static Block blockFluidInOut;
    public static Block blockEnergyInOut;
    public static Block blockItemInOut;
    public static Block blockElectrolyzer;

    public static void init() {
        blockVoidOreMiner = BlockVoidOreMiner.create();
        blockVoidResMiner = BlockVoidResMiner.create();
        blockNanoBotBeacon = BlockNanoBotBeacon.create();
        blockLaserCore = BlockLaserCore.create();
        blockLaserLens = BlockLaserLens.create();
        blockSolarArray = BlockSolarArray.create();
        blockSolarCell = BlockSolarCell.create();
        blockStructureFrame = BlockStructureFrame.create();
        blockMachineBase = BlockMachineBase.create();
        blockModifierPiezo = BlockModifierPiezo.create();
        blockModifierSpeed = BlockModifierSpeed.create();
        blockModifierAccuracy = BlockModifierAccuracy.create();
        blockModifierFlight = BlockModifierFlight.create();
        blockModifierNightVision = BlockModifierNightVision.create();
        blockModifierHaste = BlockModifierHaste.create();
        blockModifierStrength = BlockModifierStrength.create();
        blockModifierWaterBreathing = BlockModifierWaterBreathing.create();
        blockModifierRegeneration = BlockModifierRegeneration.create();
        blockModifierSaturation = BlockModifierSaturation.create();
        blockModifierResistance = BlockModifierResistance.create();
        blockModifierJumpBoost = BlockModifierJumpBoost.create();
        blockModifierFireResistance = BlockModifierFireResistance.create();
        blockModifierNull = BlockModifierNull.create();
        blockHardenedStone = BlockOK.create(ModObject.blockHardenedStone, "hardened_stone_normal", Material.rock);
        blockBasalt = BlockOK.create(ModObject.blockBasalt, "basalt_normal", Material.rock);
        blockAlabaster = BlockOK.create(ModObject.blockAlabaster, "alabaster_normal", Material.rock);
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
