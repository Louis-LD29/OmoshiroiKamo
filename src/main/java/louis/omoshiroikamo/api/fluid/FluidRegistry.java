package louis.omoshiroikamo.api.fluid;

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
import louis.omoshiroikamo.common.config.FluidConfig;

public class FluidRegistry {

    private static final Map<String, FluidEntry> REGISTRY = new LinkedHashMap<>();
    private static final Map<Integer, FluidEntry> META_LOOKUP = new HashMap<>();
    private static final List<FluidEntry> META_INDEXED = new ArrayList<>();

    public static void register(FluidEntry entry) {
        if (REGISTRY.containsKey(entry.getName()))
            throw new IllegalStateException("Duplicate material: " + entry.getName());

        REGISTRY.put(entry.getName(), entry);
        META_LOOKUP.put(entry.meta, entry);

        while (META_INDEXED.size() <= entry.meta) {
            META_INDEXED.add(null);
        }
        META_INDEXED.set(entry.meta, entry);
    }

    public static FluidEntry get(String name) {
        FluidEntry defaultEntry = REGISTRY.get(name);
        if (defaultEntry == null) return null;

        FluidConfig config = Config.fluidConigs.getOrDefault(name, defaultEntry.defaults);
        return new FluidEntry(name, defaultEntry.meta, config);
    }

    public static FluidEntry fromMeta(int meta) {
        if (meta < 0 || meta >= META_INDEXED.size()) return META_INDEXED.get(0);
        return META_INDEXED.get(meta);
    }

    public static int indexOf(FluidEntry entry) {
        return entry.meta;
    }

    public static Collection<FluidEntry> all() {
        return Collections.unmodifiableList(
            META_INDEXED.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }

    public static boolean contains(String name) {
        return REGISTRY.containsKey(name);
    }

    public static void init() {
        // Base fluids
        register(new FluidEntry("Water", 0, 1000, 0.00089, 298.15, 0x3F76E4, false));

        // Common atmospheric gases
        register(new FluidEntry("Hydrogen", 50, 0.08988, 8.76e-6, 298.15, 0xE6FFFF, true));
        register(new FluidEntry("Oxygen", 51, 1.429, 2.08e-5, 298.15, 0xB0E0FF, true));
        register(new FluidEntry("Steam", 52, 0.6, 1.34e-5, 373.15, 0xEEEEEE, true));
        register(new FluidEntry("Nitrogen", 53, 1.2506, 1.76e-5, 298.15, 0xCFEFFB, true));
        register(new FluidEntry("Carbon Dioxide", 54, 1.977, 1.47e-5, 298.15, 0xE0FFF0, true));

        // Fuel gases
        register(new FluidEntry("Methane", 55, 0.656, 1.10e-5, 298.15, 0xE3FFE5, true));
        register(new FluidEntry("Ethylene", 56, 1.178, 9.20e-6, 298.15, 0xFFF0E0, true));
        register(new FluidEntry("Propane", 57, 2.009, 8.00e-6, 298.15, 0xFFF5E2, true));
        register(new FluidEntry("Butane", 58, 2.466, 7.60e-6, 298.15, 0xFFF5D8, true));
        register(new FluidEntry("Acetylene", 59, 1.097, 1.00e-5, 298.15, 0xFFE7C4, true));

        // Inert and noble gases
        register(new FluidEntry("Helium", 60, 0.1786, 1.96e-5, 298.15, 0xFFFFE0, true));
        register(new FluidEntry("Neon", 61, 0.9002, 3.10e-5, 298.15, 0xE8FFFF, true));
        register(new FluidEntry("Argon", 62, 1.784, 2.23e-5, 298.15, 0xD9F4FF, true));
        register(new FluidEntry("Radon", 63, 9.73, 2.50e-5, 298.15, 0xFFE0E0, true));

        // Toxic or industrial gases
        register(new FluidEntry("Ammonia", 64, 0.769, 9.82e-6, 298.15, 0xD0F0FF, true));
        register(new FluidEntry("Hydrogen Sulfide", 65, 1.539, 1.65e-5, 298.15, 0xE6FFE6, true));
        register(new FluidEntry("Chlorine", 66, 3.214, 1.36e-5, 298.15, 0xD8FFBA, true));
        register(new FluidEntry("Sulfur Dioxide", 67, 2.926, 1.34e-5, 298.15, 0xE6FFF2, true));
        register(new FluidEntry("Carbon Monoxide", 68, 1.145, 1.65e-5, 298.15, 0xFFF6F0, true));
        register(new FluidEntry("Nitrogen Dioxide", 69, 3.26, 1.48e-5, 298.15, 0xFFDD88, true));
        register(new FluidEntry("Ozone", 70, 2.14, 1.68e-5, 298.15, 0xA0E0FF, true));
        register(new FluidEntry("Phosgene", 71, 4.25, 1.20e-5, 298.15, 0xFFF0DD, true));
        register(new FluidEntry("Fluorine", 72, 1.696, 2.15e-5, 298.15, 0xFFFFCC, true));

        // Common inorganic acids
        register(new FluidEntry("Sulfuric Acid", 73, 1840, 0.0204, 298.15, 0xE0C0FF, false));
        register(new FluidEntry("Hydrochloric Acid", 74, 1190, 0.0019, 298.15, 0xE0FFE0, false));
        register(new FluidEntry("Nitric Acid", 75, 1510, 0.0022, 298.15, 0xF0E0E0, false));
        register(new FluidEntry("Hydrofluoric Acid", 76, 990, 0.0006, 298.15, 0xD0FFFF, false));

        // Bases and alkaline solutions
        register(new FluidEntry("Sodium Hydroxide Solution", 77, 1310, 0.00065, 298.15, 0xD0F0FF, false));
        register(new FluidEntry("Ammonium Hydroxide", 78, 960, 0.00089, 298.15, 0xCCFFFF, false));

        // Organic solvents
        register(new FluidEntry("Ethanol", 79, 789, 0.0012, 298.15, 0xFFF0F0, false));
        register(new FluidEntry("Methanol", 80, 792, 0.00059, 298.15, 0xFFEAEA, false));
        register(new FluidEntry("Acetone", 81, 784, 0.00032, 298.15, 0xFFF8E0, false));
        register(new FluidEntry("Toluene", 82, 867, 0.00059, 298.15, 0xFFF5F5, false));
        register(new FluidEntry("Benzene", 83, 876, 0.00065, 298.15, 0xFFE8FF, false));
        register(new FluidEntry("Formic Acid", 84, 1220, 0.00148, 298.15, 0xFFF0E8, false));
    }

}
