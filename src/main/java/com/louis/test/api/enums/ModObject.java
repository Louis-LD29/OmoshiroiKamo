package com.louis.test.api.enums;

public enum ModObject {

    blockTest,
    blockBoiler,
    blockFluidInput,
    blockFluidOutput,
    blockSolar;

    public final String unlocalisedName;

    private ModObject() {
        unlocalisedName = name();
    }
}
