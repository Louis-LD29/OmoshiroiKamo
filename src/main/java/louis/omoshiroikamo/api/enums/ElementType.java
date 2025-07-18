package louis.omoshiroikamo.api.enums;

public enum ElementType {

    NONE("§7"),
    PYRO("§c"),
    CRYO("§b"),
    ELECTRO("§d"),
    HYDRO("§9"),
    ANEMO("§a"),
    GEO("§e"),
    DENDRO("§2");

    private final String colorCode;

    ElementType(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public static final ElementType[] VALUES = values();

    public static ElementType fromOrdinal(int id) {
        if (id < 0 || id >= VALUES.length) return NONE;
        return VALUES[id];
    }
}
