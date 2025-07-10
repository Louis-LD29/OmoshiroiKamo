package com.louis.test.common.config;

import net.minecraftforge.common.config.Configuration;

import com.louis.test.api.enums.Material;

public class MaterialConfig {

    public final double densityKgPerM3;
    public final double specificHeatJPerKgK;
    public final double thermalConductivityWPerMK;
    public final double meltingPointK;
    public final double maxPressureMPa;
    public final double electricalConductivity;
    public final int color;

    public MaterialConfig(double densityKgPerM3, double specificHeatJPerKgK, double thermalConductivityWPerMK,
        double meltingPointK, double maxPressureMPa, double electricalConductivity, int color) {
        this.densityKgPerM3 = densityKgPerM3;
        this.specificHeatJPerKgK = specificHeatJPerKgK;
        this.thermalConductivityWPerMK = thermalConductivityWPerMK;
        this.meltingPointK = meltingPointK;
        this.maxPressureMPa = maxPressureMPa;
        this.electricalConductivity = electricalConductivity;
        this.color = color;
    }

    public static MaterialConfig loadFromConfig(Configuration config, Material mat) {
        String cat = "material settings." + mat.getDisplayName();

        double density = config.get(cat, "density", mat.getDefaultDensityKgPerM3(), "Density (kg/m^3)")
            .getDouble();

        double specificHeat = config.get(cat, "specificHeat", mat.getDefaultSpecificHeat(), "Specific Heat (J/kg.K)")
            .getDouble();

        double thermal = config
            .get(cat, "thermalConductivity", mat.getDefaultThermalConductivity(), "Thermal Conductivity (W/m.K)")
            .getDouble();

        double melting = config.get(cat, "meltingPoint", mat.getDefaultMeltingPointK(), "Melting Point (Kelvin)")
            .getDouble();

        double pressure = config.get(cat, "maxPressure", mat.getDefaultMaxPressureMPa(), "Max Pressure (MPa)")
            .getDouble();

        double electrical = config
            .get(cat, "electricalConductivity", mat.getDefaultElectricalConductivity(), "Electrical Conductivity (S/m)")
            .getDouble();

        int defaultColor = mat.get().color;
        String hex = String.format("0x%06X", defaultColor)
            .toUpperCase();
        int color = config
            .get(
                cat,
                "color",
                mat.getDefaultColor(),
                "Material display color as RGB integer (" + hex + "), format: 0xRRGGBB")
            .getInt();

        return new MaterialConfig(density, specificHeat, thermal, melting, pressure, electrical, color);
    }
}
