package com.louis.test.api.material;

public enum VoltageTier {

    ULV(0, 59, "Ultra Low Voltage (ULV)"),
    LV(60, 119, "Low Voltage (LV)"),
    MV(120, 239, "Medium Voltage (MV)"),
    HV(240, 479, "High Voltage (HV)"),
    EV(480, 959, "Extreme Voltage (EV)"),
    IV(960, 2000, "Insane Voltage (IV)");

    public final int minVoltage;
    public final int maxVoltage;
    public final String displayName;

    VoltageTier(int min, int max, String displayName) {
        this.minVoltage = min;
        this.maxVoltage = max;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static VoltageTier fromVoltage(int voltage) {
        for (VoltageTier tier : values()) {
            if (voltage <= tier.maxVoltage) {
                return tier;
            }
        }
        return IV; // mặc định nếu vượt xa
    }

    public boolean isTooFarFrom(VoltageTier other) {
        return Math.abs(this.ordinal() - other.ordinal()) > 1;
    }

    public boolean isLowerThan(VoltageTier other) {
        return this.ordinal() < other.ordinal();
    }

    public boolean isHigherThan(VoltageTier other) {
        return this.ordinal() > other.ordinal();
    }

    public int getIC2VoltageTier() {
        return this.ordinal();
    }
}
