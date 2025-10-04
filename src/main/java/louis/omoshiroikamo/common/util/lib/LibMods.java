package louis.omoshiroikamo.common.util.lib;

import java.util.function.Supplier;

import cpw.mods.fml.common.Loader;

public enum LibMods {

    Waila("Waila"),
    WDMLA("wdmla"),
    Baubles("Baubles"),
    BaublesExpanded("Baubles|Expanded"),
    NotEnoughItems("NotEnoughItems"),
    TConstruct("TConstruct"),
    ImmersiveEngineering("ImmersiveEngineering"),
    BuildCraftEnergy("BuildCraft|Energy"),
    IC2("IC2"),
    BogoSorter("bogosorter"),
    EtFuturum("etfuturum"),;

    public final String modid;
    private final Supplier<Boolean> supplier;
    private Boolean loaded;

    LibMods(String modid) {
        this.modid = modid;
        this.supplier = null;
    }

    LibMods(Supplier<Boolean> supplier) {
        this.supplier = supplier;
        this.modid = null;
    }

    public boolean isLoaded() {
        if (loaded == null) {
            if (supplier != null) {
                loaded = supplier.get();
            } else if (modid != null) {
                loaded = Loader.isModLoaded(modid);
            } else {
                loaded = false;
            }
        }
        return loaded;
    }
}
