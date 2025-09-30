package louis.omoshiroikamo.api.ore;

import louis.omoshiroikamo.api.fluid.FluidRegistry;
import louis.omoshiroikamo.config.Config;
import louis.omoshiroikamo.config.OreConfig;

public class OreEntry {

    private final String name;
    public final int meta;
    public final OreConfig defaults;

    public OreEntry(String name, int meta, int veinSize, int minY, int maxY, float chancePerChunk, boolean enabled,
        int color, int hardness, int resistance, int harvestLevel) {
        this.name = name;
        this.meta = meta;
        this.defaults = new OreConfig(
            name,
            meta,
            veinSize,
            minY,
            maxY,
            chancePerChunk,
            enabled,
            color,
            hardness,
            resistance,
            harvestLevel);
    }

    public OreEntry(String name, int meta, OreConfig config) {
        this.name = name;
        this.meta = meta;
        this.defaults = config;
    }

    public OreEntry(String name) {
        this(
            name,
            FluidRegistry.all()
                .size(),
            OreConfig.defaultFor(name));
    }

    public String getName() {
        return name;
    }

    public int getMeta() {
        return meta;
    }

    public String getUnlocalizedName() {
        return name.replace(" ", "");
    }

    public OreConfig getConfig() {
        return Config.oreConfigs.getOrDefault(name, defaults);
    }

    public int getMinY() {
        return getConfig().minY;
    }

    public int getMaxY() {
        return getConfig().maxY;
    }

    public int getVeinSize() {
        return getConfig().veinSize;
    }

    public float getChancePerChunk() {
        return getConfig().chancePerChunk;
    }

    public boolean isEnable() {
        return getConfig().enabled;
    }

    public int getColor() {
        int color = getConfig().color;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float brightness = 1.2f;
        r = Math.min(255, (int) (r * brightness));
        g = Math.min(255, (int) (g * brightness));
        b = Math.min(255, (int) (b * brightness));
        return (r << 16) | (g << 8) | b;
    }

    public int getHardness() {
        return getConfig().hardness;
    }

    public int getResistance() {
        return getConfig().resistance;
    }

    public int getHarvestLevel() {
        return getConfig().harvestLevel;
    }

}
