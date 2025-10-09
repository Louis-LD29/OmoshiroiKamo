package louis.omoshiroikamo.config.item;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main magnet settings")
@Config.LangKey(LibResources.CONFIG + "magnetConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.items.magnet", configSubDirectory = LibMisc.MOD_ID)
public class MagnetConfig {

    @Config.DefaultInt(5)
    public static int magnetRange;

    @Config.DefaultInt(20)
    public static int magnetMaxItems;

    @Config.DefaultBoolean(true)
    public static boolean magnetAllowInMainInventory;

    @Config.DefaultBoolean(true)
    public static boolean magnetAllowInBaublesSlot;
}
