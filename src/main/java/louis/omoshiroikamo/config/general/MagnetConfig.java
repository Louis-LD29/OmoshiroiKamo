package louis.omoshiroikamo.config.general;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config(modid = LibMisc.MOD_ID, category = "magnet", configSubDirectory = LibMisc.MOD_ID, filename = "magnet")
public class MagnetConfig {

    @Config.Comment("Main magnet settings")
    public static final Magnet magnetConfig = new Magnet();

    @Config.LangKey(LibResources.CONFIG_LANG_KEY + "magnetConfig")
    public static class Magnet {

        @Config.DefaultInt(5)
        public int magnetRange;

        @Config.DefaultInt(20)
        public int magnetMaxItems;

        @Config.DefaultBoolean(false)
        public boolean magnetAllowInMainInventory;

        @Config.DefaultBoolean(false)
        public boolean magnetAllowInBaublesSlot;

        @Config.DefaultStringList({ "appliedenergistics2:item.ItemCrystalSeed", "Botania:livingrock",
            "Botania:manaTablet" })
        public String[] magnetBlacklist;
    }
}
