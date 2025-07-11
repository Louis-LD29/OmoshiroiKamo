package com.louis.test.api;

import java.util.*;

import com.louis.test.common.config.Config;
import com.louis.test.common.config.MaterialConfig;

public class MaterialRegistry {

    private static final Map<String, MaterialEntry> REGISTRY = new LinkedHashMap<>();
    private static final List<MaterialEntry> META_INDEXED = new ArrayList<>();

    public static void register(MaterialEntry entry) {
        REGISTRY.put(entry.name, entry);
        META_INDEXED.add(entry);
    }

    public static void register(String name, MaterialConfig config) {
        register(new MaterialEntry(name, config));
    }

    public static MaterialEntry get(String name) {
        MaterialEntry defaultEntry = REGISTRY.get(name);
        if (defaultEntry == null) return null;

        MaterialConfig config = Config.materialConfigs.getOrDefault(name, defaultEntry.defaults);
        return new MaterialEntry(name, config);
    }

    public static MaterialEntry fromMeta(int meta) {
        if (meta < 0 || meta >= META_INDEXED.size()) return META_INDEXED.get(0);
        return META_INDEXED.get(meta);
    }

    public static int indexOf(MaterialEntry entry) {
        return META_INDEXED.indexOf(entry);
    }

    public static Collection<MaterialEntry> all() {
        return Collections.unmodifiableList(META_INDEXED);
    }

    static {
        register(new MaterialEntry("Iron", 7870, 449, 80.2, 1811, 25, 1.0e7, 0xD8D8D8));
        register(new MaterialEntry("Copper", 8960, 385, 401, 1358, 21, 5.96e7, 0xF08048));
        register(new MaterialEntry("Silver", 10490, 235, 429, 1234, 15, 6.30e7, 0xF0F0F0));
        register(new MaterialEntry("Gold", 19300, 129, 318, 1337, 15, 4.52e7, 0xFFE14A));
        register(new MaterialEntry("Zinc", 7135, 388, 116, 692, 10, 1.69e7, 0xBBBBBB));
        register(new MaterialEntry("Aluminum", 2700, 900, 237, 933, 20, 3.77e7, 0xE5E5E5));
        register(new MaterialEntry("Lead", 11340, 128, 35, 600, 5, 4.80e6, 0x5C5C5C));
        register(new MaterialEntry("Titanium", 4507, 522, 21.9, 1941, 50, 2.38e6, 0xC8D8EF));
        register(new MaterialEntry("Chromium", 7190, 449, 93.9, 2180, 40, 7.90e6, 0xB0FF5A));
        register(new MaterialEntry("Nickel", 8908, 440, 90.9, 1728, 35, 1.43e7, 0xCCCCCC));
        register(new MaterialEntry("Carbon Steel", 7850, 486, 50, 1698, 35, 6.00e6, 0x707070));
        register(new MaterialEntry("Stainless Steel", 8000, 500, 16, 1783, 40, 1.35e6, 0xA0A0A0));
        register(new MaterialEntry("Brass", 8500, 380, 109, 1203, 20, 1.60e7, 0xF2D56B));
        register(new MaterialEntry("Inconel 625", 8440, 435, 15, 1623, 55, 2.00e6, 0x8A8A8A));
        register(new MaterialEntry("Tungsten", 19250, 134, 173, 3695, 60, 1.82e7, 0x3A3A3A));
        register(new MaterialEntry("Tungsten Carbide", 15000, 180, 84, 3143, 80, 5.00e6, 0x555555));
        register(new MaterialEntry("Niobium", 8570, 265, 54, 2741, 45, 6.70e6, 0xAFAFFF));

        for (String matName : Config.materialCustom) {
            register(new MaterialEntry(matName, 7870, 449, 80.2, 1811, 25, 1.0e7, 0xD8D8D8));
        }
    }

}
