package louis.omoshiroikamo.api.ore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.config.OreConfig;

public class OreRegistry {

    private static final Map<String, OreEntry> REGISTRY = new LinkedHashMap<>();
    private static final Map<Integer, OreEntry> META_LOOKUP = new HashMap<>();
    private static final List<OreEntry> META_INDEXED = new ArrayList<>();

    public static void register(OreEntry entry) {
        if (REGISTRY.containsKey(entry.getName())) throw new IllegalStateException("Duplicate ore: " + entry.getName());

        REGISTRY.put(entry.getName(), entry);
        META_LOOKUP.put(entry.meta, entry);

        while (META_INDEXED.size() <= entry.meta) {
            META_INDEXED.add(null);
        }
        META_INDEXED.set(entry.meta, entry);
    }

    public static OreEntry get(String name) {
        OreEntry defaultEntry = REGISTRY.get(name);
        if (defaultEntry == null) return null;

        OreConfig config = Config.oreConfigs.getOrDefault(name, defaultEntry.defaults);
        return new OreEntry(name, defaultEntry.meta, config);
    }

    public static OreEntry fromMeta(int meta) {
        if (meta < 0 || meta >= META_INDEXED.size()) return META_INDEXED.get(0);
        return META_INDEXED.get(meta);
    }

    public static int indexOf(OreEntry entry) {
        return entry.meta;
    }

    public static Collection<OreEntry> all() {
        return Collections.unmodifiableList(
            META_INDEXED.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public static boolean contains(String name) {
        return REGISTRY.containsKey(name);
    }

    public static void init() {
        register(new OreEntry("Hematite", 0, 10, 12, 64, 0.4f, true, 0x7A1F1F));
        register(new OreEntry("Magnetite", 1, 9, 8, 48, 0.35f, true, 0x1C1C1C));
    }

}
