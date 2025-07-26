package louis.omoshiroikamo.api.enums;

import net.minecraft.block.Block;

import louis.omoshiroikamo.common.core.lib.LibMisc;

public enum ModObject {

    blockMultiblock,
    blockBoiler,
    blockFluidInOut,
    blockItemInOut,
    blockEnergyInOut,
    blockHeatInput,
    blockHeatSource,
    blockElectrolyzer,
    blockSolar,
    blockMaterial,
    blockConnectable,
    blockAnvil,
    blockFurnace,

    itemOperationOrb,
    itemMaterial,
    itemBucketMaterial,
    itemBucketFluid,
    itemOre,
    itemHammer,
    itemWireCoil;

    public final String unlocalisedName;
    private Block blockInstance;

    private ModObject() {
        this.unlocalisedName = name();
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
