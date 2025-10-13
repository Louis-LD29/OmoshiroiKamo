package louis.omoshiroikamo.api.enums;

import net.minecraft.block.Block;

import louis.omoshiroikamo.common.util.lib.LibMisc;

public enum ModObject {

    blockMultiblock,
    blockBoiler,
    blockFluidInOut,
    blockItemInOut,
    blockEnergyInOut,
    blockElectrolyzer,

    blockSolarArray,
    blockSolarCell,
    blockStructureFrame,
    blockModifier,
    blockBasalt,
    blockAlabaster,
    blockHardenedStone,
    blockMica,

    blockBlockMaterial,
    blockConnectable,
    blockAnvil,
    blockFurnace,

    itemOperationOrb,
    itemBackPack,
    itemUpgrade,
    itemStackUpgrade,
    itemCraftingUpgrade,
    itemMagnetUpgrade,
    itemFeedingUpgrade,
    itemBatteryUpgrade,
    itemEverlastingUpgrade,

    itemStabilizedEnderPear,
    itemPhotovoltaicCell,
    itemAssembler,

    itemItemMaterial,
    itemBucketMaterial,
    itemBucketFluid,
    itemOre,
    itemHammer,
    itemWireCoil;

    public final String unlocalisedName;
    private Block blockInstance;

    ModObject() {
        String raw = name();

        if (raw.startsWith("block")) {
            raw = raw.substring(5);
        } else if (raw.startsWith("item")) {
            raw = raw.substring(4);
        }

        this.unlocalisedName = Character.toLowerCase(raw.charAt(0)) + raw.substring(1);
    }

    public String getRegistryName() {
        return LibMisc.MOD_ID + ":" + unlocalisedName;
    }

    public void setBlock(Block block) {
        this.blockInstance = block;
    }

    public Block getBlock() {
        return this.blockInstance;
    }
}
