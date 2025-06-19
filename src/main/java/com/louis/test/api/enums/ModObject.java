package com.louis.test.api.enums;

import com.louis.test.lib.LibMisc;

public enum ModObject {

    blockTest,
    blockBoiler,
    blockFluidInput,
    blockFluidOutput,
    blockFluidFilter,
    blockHeatInput,
    blockHeatSource,
    blockElectrolyzer,
    blockSolar;

    public final String unlocalisedName;

    private ModObject() {
        unlocalisedName = name();
    }

    public String getRegistryName() {
        return LibMisc.MOD_ID + ":" + unlocalisedName;
    }
}
