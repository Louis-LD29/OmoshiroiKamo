package ruiseki.omoshiroikamo.config.item;

import com.gtnewhorizon.gtnhlib.config.Config;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main feeding settings")
@Config.LangKey(LibResources.CONFIG + "feedingConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.items.feeding", configSubDirectory = LibMisc.MOD_ID)
public class FeedingConfig {

    @Config.DefaultBoolean(true)
    public static boolean feedingAllowInMainInventory;

    @Config.DefaultBoolean(true)
    public static boolean feedingAllowInBaublesSlot;
}
