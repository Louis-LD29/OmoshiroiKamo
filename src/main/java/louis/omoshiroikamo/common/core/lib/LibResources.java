package louis.omoshiroikamo.common.core.lib;

public class LibResources {

    public static final String PREFIX_MOD = LibMisc.MOD_ID + ":";

    public static final String PREFIX_GUI = PREFIX_MOD + "textures/gui/";
    public static final String PREFIX_BLOCK = PREFIX_MOD + "textures/blocks/";
    public static final String PREFIX_MODEL = PREFIX_MOD + "models/";
    public static final String PREFIX_MATERIAL_FLUID_ICONS = "icons/materialFluids";
    public static final String PREFIX_FLUID_ICONS = "icons/fluids";

    public static final String GUI_MANA_HUD = PREFIX_GUI + "mana.png";
    public static final String GUI_NEI_BLANK = PREFIX_GUI + "nei/neiBlank.png";
    public static final String GUI_SLOT = PREFIX_GUI + "nei/slot.png";
    public static final String GUI_ICONS = PREFIX_GUI + "icons.png";
    public static final String OVERLAY_BUTTON_REDSTONE_ON = PREFIX_GUI + "redstone_on.png";
    public static final String OVERLAY_BUTTON_REDSTONE_OFF = PREFIX_GUI + "redstone_off.png";
    public static final String CYCLE_IOMODE = PREFIX_GUI + "cycle_iomode.png";
    public static final String PROGRESS_BURN = PREFIX_GUI + "progress_burn.png";

    public static final String KEY_LEVEL_COST = "level_cost";
    public static final String KEY_UNLOC_NAME = "unlocalized_name";
    public static final String KEY_UPGRADE_PREFIX = LibMisc.MOD_ID + ".mana.upgrade.";
    public static final String KEY_UPGRADE_ITEM = "upgradeItem";

    // Meta
    public static final int BASE = 0;
    public static final int META1 = 100;
    public static final int META2 = 200;
    public static final int META3 = 300;
    public static final int META4 = 400;
    public static final int META5 = 500;

    // ImmersiveEngineering
    public static final String CHAT = "chat.ImmersiveEngineering.";
    public static final String CHAT_WARN = CHAT + "warning.";
    public static final String CHAT_INFO = CHAT + "info.";
    public static final String DESC = "desc.ImmersiveEngineering.";
    public static final String DESC_INFO = DESC + "info.";
}
