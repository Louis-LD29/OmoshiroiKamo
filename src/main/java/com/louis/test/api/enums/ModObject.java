package com.louis.test.api.enums;

public enum ModObject {

    blockTest;

    public final String unlocalisedName;

    private ModObject() {
        unlocalisedName = name();
    }
}
