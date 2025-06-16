package com.louis.test.api.enums;

import net.minecraft.util.EnumChatFormatting;

import com.louis.test.lib.LibMisc;

public enum IoMode {

    NONE(LibMisc.lang.localize("gui.machine.ioMode.none")),
    INPUT(LibMisc.lang.localize("gui.machine.ioMode.input")),
    OUTPUT(LibMisc.lang.localize("gui.machine.ioMode.output")),
    INPUT_OUTPUT(LibMisc.lang.localize("gui.machine.ioMode.inputOutput")),
    DISABLED(LibMisc.lang.localize("gui.machine.ioMode.disabled"));

    private final String unlocalisedName;

    IoMode(String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
    }

    public String getUnlocalisedName() {
        return unlocalisedName;
    }

    public static ConnectionMode getNext(ConnectionMode mode) {
        int ord = mode.ordinal() + 1;
        if (ord >= ConnectionMode.values().length) {
            ord = 0;
        }
        return ConnectionMode.values()[ord];
    }

    public static ConnectionMode getPrevious(ConnectionMode mode) {

        int ord = mode.ordinal() - 1;
        if (ord < 0) {
            ord = ConnectionMode.values().length - 1;
        }
        return ConnectionMode.values()[ord];
    }

    public boolean inputs() {
        return this == INPUT || this == INPUT_OUTPUT;
    }

    public boolean outputs() {
        return this == OUTPUT || this == INPUT_OUTPUT;
    }

    public boolean canOutput() {
        return outputs() || this == NONE;
    }

    public boolean canRecieveInput() {
        return inputs() || this == NONE;
    }

    public String getLocalisedName() {
        return LibMisc.lang.localize(unlocalisedName);
    }

    public String colorLocalisedName() {
        String loc = getLocalisedName();
        switch (this) {
            case DISABLED:
                return EnumChatFormatting.RED + loc;
            case NONE:
                return EnumChatFormatting.GRAY + loc;
            case INPUT:
                return EnumChatFormatting.AQUA + loc;
            case OUTPUT:
                return EnumChatFormatting.GOLD + loc;
            case INPUT_OUTPUT:
                return String.format(
                    LibMisc.lang.localize(this.getUnlocalisedName() + ".colored"),
                    EnumChatFormatting.GOLD,
                    EnumChatFormatting.WHITE,
                    EnumChatFormatting.AQUA);
            default:
                return loc;
        }
    }

    public IoMode next() {
        int index = ordinal() + 1;
        if (index >= values().length) {
            index = 0;
        }
        return values()[index];
    }
}
