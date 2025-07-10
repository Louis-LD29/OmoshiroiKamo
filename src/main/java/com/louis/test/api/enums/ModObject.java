package com.louis.test.api.enums;

import net.minecraft.block.Block;

import com.louis.test.core.lib.LibMisc;

public enum ModObject {

    blockMeta,
    blockMultiblock,
    blockTest,
    blockBoiler,
    blockFluidInput,
    blockFluidOutput,
    blockFluidFilter,
    blockFluidInOut,
    blockItemInput,
    blockItemOutput,
    blockEnergyInOut,
    blockHeatInput,
    blockHeatSource,
    blockElectrolyzer,
    blockSolar,

    blockMaterial,

    itemMaterial,
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
