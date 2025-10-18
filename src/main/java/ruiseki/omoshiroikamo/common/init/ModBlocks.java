package ruiseki.omoshiroikamo.common.init;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.block.BlockOK;
import ruiseki.omoshiroikamo.common.block.anvil.BlockAnvil;
import ruiseki.omoshiroikamo.common.block.electrolyzer.BlockElectrolyzer;
import ruiseki.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import ruiseki.omoshiroikamo.common.block.furnace.BlockFurnace;
import ruiseki.omoshiroikamo.common.block.material.BlockMachineBase;
import ruiseki.omoshiroikamo.common.block.material.BlockMaterial;
import ruiseki.omoshiroikamo.common.block.material.BlockStructureFrame;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierAccuracy;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierFireResistance;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierFlight;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierHaste;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierJumpBoost;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierNightVision;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierNull;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierPiezo;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierRegeneration;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierResistance;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierSaturation;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierSpeed;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierStrength;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.BlockModifierWaterBreathing;
import ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon.BlockNanoBotBeacon;
import ruiseki.omoshiroikamo.common.block.multiblock.part.energy.BlockEnergyInOut;
import ruiseki.omoshiroikamo.common.block.multiblock.part.fluid.BlockFluidInOut;
import ruiseki.omoshiroikamo.common.block.multiblock.part.item.BlockItemInOut;
import ruiseki.omoshiroikamo.common.block.multiblock.solarArray.BlockSolarArray;
import ruiseki.omoshiroikamo.common.block.multiblock.solarArray.cell.BlockSolarCell;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.core.BlockLaserCore;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.lens.BlockLaserLens;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.BlockVoidOreMiner;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.BlockVoidResMiner;
import ruiseki.omoshiroikamo.common.util.Logger;

public enum ModBlocks {

    BLOCK_MICA(true, BlockOK.create(ModObject.blockMica, "mica", Material.rock)),
    BLOCK_HARDENED_STONE(true, BlockOK.create(ModObject.blockHardenedStone, "hardened_stone_normal", Material.rock)),
    BLOCK_ALABASTER(true, BlockOK.create(ModObject.blockAlabaster, "alabaster_normal", Material.rock)),
    BLOCK_BASALT(true, BlockOK.create(ModObject.blockBasalt, "basalt_normal", Material.rock)),

    VOID_ORE_MINER(true, BlockVoidOreMiner.create()),
    VOID_RES_MINER(true, BlockVoidResMiner.create()),
    NANO_BOT_BEACON(true, BlockNanoBotBeacon.create()),
    LASER_CORE(true, BlockLaserCore.create()),
    LASER_LENS(true, BlockLaserLens.create()),
    SOLAR_ARRAY(true, BlockSolarArray.create()),
    SOLAR_CELL(true, BlockSolarCell.create()),
    STRUCTURE_FRAME(true, BlockStructureFrame.create()),
    MACHINE_BASE(true, BlockMachineBase.create()),

    MODIFIER_PIEZO(true, BlockModifierPiezo.create()),
    MODIFIER_SPEED(true, BlockModifierSpeed.create()),
    MODIFIER_ACCURACY(true, BlockModifierAccuracy.create()),
    MODIFIER_FLIGHT(true, BlockModifierFlight.create()),
    MODIFIER_NIGHT_VISION(true, BlockModifierNightVision.create()),
    MODIFIER_HASTE(true, BlockModifierHaste.create()),
    MODIFIER_STRENGTH(true, BlockModifierStrength.create()),
    MODIFIER_WATER_BREATHING(true, BlockModifierWaterBreathing.create()),
    MODIFIER_REGENERATION(true, BlockModifierRegeneration.create()),
    MODIFIER_SATURATION(true, BlockModifierSaturation.create()),
    MODIFIER_RESISTANCE(true, BlockModifierResistance.create()),
    MODIFIER_JUMP_BOOST(true, BlockModifierJumpBoost.create()),
    MODIFIER_FIRE_RESISTANCE(true, BlockModifierFireResistance.create()),
    MODIFIER_NULL(true, BlockModifierNull.create()),

    FLUID_IN_OUT(true, BlockFluidInOut.create()),
    ENERGY_IN_OUT(true, BlockEnergyInOut.create()),
    ITEM_IN_OUT(true, BlockItemInOut.create()),
    ELECTROLYZER(true, BlockElectrolyzer.create()),
    CONNECTABLE(true, BlockConnectable.create()),
    MATERIAL(true, BlockMaterial.create()),
    ANVIL(true, BlockAnvil.create()),
    FURNACE(true, BlockFurnace.create());

    public static final ModBlocks[] VALUES = values();

    public static void init() {
        for (ModBlocks block : VALUES) {
            if (!block.isEnabled()) {
                continue;
            }
            try {
                block.get()
                    .init();
                Logger.info("Successfully initialized " + block.name());
            } catch (Exception e) {
                Logger.error("Failed to initialize block: +" + block.name());
            }
        }
    }

    private final boolean enabled;
    private final BlockOK block;

    ModBlocks(boolean enabled, BlockOK block) {
        this.enabled = enabled;
        this.block = block;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BlockOK get() {
        return block;
    }

    public Item getItem() {
        return Item.getItemFromBlock(block);
    }

    public ItemStack newItemStack() {
        return newItemStack(1);
    }

    public ItemStack newItemStack(int count) {
        return newItemStack(count, 0);
    }

    public ItemStack newItemStack(int count, int meta) {
        return new ItemStack(this.get(), count, meta);
    }
}
