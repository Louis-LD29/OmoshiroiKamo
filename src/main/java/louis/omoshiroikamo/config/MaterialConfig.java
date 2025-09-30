package louis.omoshiroikamo.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import louis.omoshiroikamo.api.material.MaterialEntry;

public class MaterialConfig {

    public final String displayName;
    public final int meta;
    public final double densityKgPerM3;
    public final double specificHeatJPerKgK;
    public final double thermalConductivityWPerMK;
    public final double meltingPointK;
    public final double maxPressureMPa;
    public final double electricalConductivity;
    public final int color;
    public final int moltenColor;

    public MaterialConfig(String displayName, int meta, double densityKgPerM3, double specificHeatJPerKgK,
        double thermalConductivityWPerMK, double meltingPointK, double maxPressureMPa, double electricalConductivity,
        int color, int moltenColor) {
        this.displayName = displayName;
        this.meta = meta;
        this.densityKgPerM3 = densityKgPerM3;
        this.specificHeatJPerKgK = specificHeatJPerKgK;
        this.thermalConductivityWPerMK = thermalConductivityWPerMK;
        this.meltingPointK = meltingPointK;
        this.maxPressureMPa = maxPressureMPa;
        this.electricalConductivity = electricalConductivity;
        this.color = color;
        this.moltenColor = moltenColor;
    }

    public static MaterialConfig defaultFor(String name) {
        int defaultMeta = Config.materialConfigs.size();
        return new MaterialConfig(name, defaultMeta, 7800, 500, 50, 1800, 200, 1e7, 0x888888, 0xFF5500);
    }

    public static MaterialConfig loadFromConfig(Configuration config, MaterialEntry entry) {
        String cat = Config.sectionMaterial.name.toLowerCase() + "."
            + entry.getName()
                .toLowerCase();

        Property metaProp = config.get(cat, "meta", entry.defaults.meta, "Material Meta");

        int meta;
        if (entry.defaults.meta < 50) {
            meta = metaProp.setToDefault()
                .getInt();
        } else {
            int before = metaProp.getInt();
            meta = before;
        }

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
        int color = config
            .get(
                cat,
                "color",
                defaultColor,
                String.format("Material display color as RGB integer (0x%06X), format: 0xRRGGBB", defaultColor))
            .getInt();

        int defaultMoltenColor = entry.defaults.moltenColor;
        int moltenColor = config.get(
            cat,
            "moltenColor",
            defaultMoltenColor,
            String
                .format("Material display molten color as RGB integer (0x%06X), format: 0xRRGGBB", defaultMoltenColor))
            .getInt();

        return new MaterialConfig(
            entry.getName(),
            meta,
            density,
            specificHeat,
            thermal,
            melting,
            pressure,
            electrical,
            color,
            moltenColor);
    }

}
