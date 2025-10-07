package louis.omoshiroikamo.config.general;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config(modid = LibMisc.MOD_ID, category = "feeding", configSubDirectory = LibMisc.MOD_ID, filename = "feeding")
public class FeedingConfig {

    @Config.Comment("Main feeding settings")
    public static final Feeding feedingConfig = new Feeding();

    @Config.LangKey(LibResources.CONFIG + "feedingConfig")
    public static class Feeding {

        @Config.DefaultBoolean(false)
        public boolean feedingAllowInMainInventory;

        @Config.DefaultBoolean(false)
        public boolean feedingAllowInBaublesSlot;
    }
}
