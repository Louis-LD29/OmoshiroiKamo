package louis.omoshiroikamo.config;

import net.minecraftforge.common.config.Configuration;

import louis.omoshiroikamo.api.ore.OreEntry;

public class OreConfig {

    public final String displayName;
    public final int meta;

    public final int veinSize;
    public final int minY;
    public final int maxY;
    public final float chancePerChunk;
    public final boolean enabled;
    public final int color;
    public final int hardness;
    public final int resistance;
    public final int harvestLevel;

    public OreConfig(String displayName, int meta, int veinSize, int minY, int maxY, float chancePerChunk,
        boolean enabled, int color, int hardness, int resistance, int harvestLevel) {
        this.displayName = displayName;
        this.meta = meta;
        this.veinSize = veinSize;
        this.minY = minY;
        this.maxY = maxY;
        this.chancePerChunk = chancePerChunk;
        this.enabled = enabled;
        this.color = color;
        this.hardness = hardness;
        this.resistance = resistance;
        this.harvestLevel = harvestLevel;
    }

    public static OreConfig defaultFor(String name) {
        int defaultMeta = Config.oreConfigs.size();
        return new OreConfig(name, defaultMeta, 10, 12, 64, 0.4f, true, 0x7A1F1F, 5, 10, 2);
    }

    public static OreConfig loadFromConfig(Configuration config, OreEntry entry) {
        String cat = Config.sectionOre.name + "."
            + entry.getName()
                .toLowerCase();
        int meta = entry.defaults.meta;

        int veinSize = config.getInt("veinSize", cat, 8, 1, 64, "Vein size per generation");
        int minY = config.getInt("minY", cat, 20, 0, 255, "Minimum Y level to spawn");
        int maxY = config.getInt("maxY", cat, 64, 0, 255, "Maximum Y level to spawn");
        float chance = (float) config.getFloat("chancePerChunk", cat, 10f, 0f, 100f, "Vein spawn attempts per chunk");
        boolean enabled = config.getBoolean("enabled", cat, true, "Enable generation for this ore");
        int defaultColor = entry.defaults.color;
        int color = config
            .get(
                cat,
                "color",
                defaultColor,
                String.format("Material display color as RGB integer (0x%06X), format: 0xRRGGBB", defaultColor))
            .getInt();
        int hardness = config
            .getInt("hardness", cat, entry.defaults.hardness, 1, 100, "Block hardness (e.g., 3 = stone)");
        int resistance = config.getInt("resistance", cat, entry.defaults.resistance, 1, 100, "Explosion resistance");
        int harvestLevel = config
            .getInt("harvestLevel", cat, entry.defaults.harvestLevel, 0, 5, "Tool level required to harvest");

        return new OreConfig(
            entry.getName(),
            meta,
            veinSize,
            minY,
            maxY,
            chance,
            enabled,
            color,
            hardness,
            resistance,
            harvestLevel);
    }
}
