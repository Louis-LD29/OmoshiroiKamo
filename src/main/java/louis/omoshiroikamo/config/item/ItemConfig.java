package louis.omoshiroikamo.config.item;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config(modid = LibMisc.MOD_ID, category = "item", configSubDirectory = LibMisc.MOD_ID, filename = "item")
public class ItemConfig {

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(ItemConfig.class);
        ConfigurationManager.registerConfig(AnvilUpgradeConfig.class);
    }

    @Config.Comment("Main item settings")
    public static final Item itemConfig = new Item();

    @Config.LangKey(LibResources.CONFIG + "itemConfig")
    public static class Item {

        @Config.DefaultBoolean(true)
        public boolean renderPufferFish;

        @Config.DefaultBoolean(true)
        public boolean addDurabilityTootip;

        @Config.DefaultBoolean(true)
        public boolean renderDurabilityBar;

        @Config.DefaultBoolean(true)
        public boolean renderChargeBar;
    }
}
