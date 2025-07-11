package com.louis.test.common.config;

import net.minecraftforge.common.config.Configuration;

import com.louis.test.api.MaterialEntry;

public class MaterialConfig {

    public final String displayName;
    public final double densityKgPerM3;
    public final double specificHeatJPerKgK;
    public final double thermalConductivityWPerMK;
    public final double meltingPointK;
    public final double maxPressureMPa;
    public final double electricalConductivity;
    public final int color;

    public MaterialConfig(String displayName, double densityKgPerM3, double specificHeatJPerKgK,
        double thermalConductivityWPerMK, double meltingPointK, double maxPressureMPa, double electricalConductivity,
        int color) {
        this.displayName = displayName;
        this.densityKgPerM3 = densityKgPerM3;
        this.specificHeatJPerKgK = specificHeatJPerKgK;
        this.thermalConductivityWPerMK = thermalConductivityWPerMK;
        this.meltingPointK = meltingPointK;
        this.maxPressureMPa = maxPressureMPa;
        this.electricalConductivity = electricalConductivity;
        this.color = color;
    }

    public static MaterialConfig loadFromConfig(Configuration config, MaterialEntry entry) {
        String cat = Config.sectionMaterial.name + "." + entry.name;

        double density = config.get(cat, "density", entry.defaults.densityKgPerM3, "Density (kg/m^3)")
            .getDouble();
        double specificHeat = config
            .get(cat, "specificHeat", entry.defaults.specificHeatJPerKgK, "Specific Heat (J/kg.K)")
            .getDouble();
        double thermal = config
            .get(cat, "thermalConductivity", entry.defaults.thermalConductivityWPerMK, "Thermal Conductivity (W/m.K)")
            .getDouble();
        double melting = config.get(cat, "meltingPoint", entry.defaults.meltingPointK, "Melting Point (Kelvin)")
            .getDouble();
        double pressure = config.get(cat, "maxPressure", entry.defaults.maxPressureMPa, "Max Pressure (MPa)")
            .getDouble();
        double electrical = config
            .get(cat, "electricalConductivity", entry.defaults.electricalConductivity, "Electrical Conductivity (S/m)")
            .getDouble();

        int defaultColor = entry.defaults.color;
        String hex = String.format("0x%06X", defaultColor)
            .toUpperCase();
        int color = config
            .get(cat, "color", defaultColor, "Material display color as RGB integer (" + hex + "), format: 0xRRGGBB")
            .getInt();

        return new MaterialConfig(entry.name, density, specificHeat, thermal, melting, pressure, electrical, color);
    }

}
