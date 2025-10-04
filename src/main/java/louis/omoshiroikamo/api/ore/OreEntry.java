package louis.omoshiroikamo.api.ore;

public class OreEntry {

    private final String name;
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

    public OreEntry(String name, int meta, int veinSize, int minY, int maxY, float chancePerChunk, boolean enabled,
        int color, int hardness, int resistance, int harvestLevel) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public int getMeta() {
        return meta;
    }

    public String getUnlocalizedName() {
        return name.replace(" ", "");
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getVeinSize() {
        return veinSize;
    }

    public float getChancePerChunk() {
        return chancePerChunk;
    }

    public boolean isEnable() {
        return enabled;
    }

    public int getColor() {
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
        return hardness;
    }

    public int getResistance() {
        return resistance;
    }

    public int getHarvestLevel() {
        return harvestLevel;
    }

}
