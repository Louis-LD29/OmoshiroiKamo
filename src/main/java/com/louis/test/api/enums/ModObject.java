package com.louis.test.api.enums;

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
}
