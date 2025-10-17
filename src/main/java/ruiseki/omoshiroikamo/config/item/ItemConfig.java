package ruiseki.omoshiroikamo.config.item;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;
import ruiseki.omoshiroikamo.config.general.AnvilUpgradeConfig;

@Config.Comment("Main item settings")
@Config.LangKey(LibResources.CONFIG + "itemConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.items", configSubDirectory = LibMisc.MOD_ID)
public class ItemConfig {

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(ItemConfig.class);
        ConfigurationManager.registerConfig(AnvilUpgradeConfig.class);
    }

    @Config.DefaultBoolean(true)
    public static boolean renderPufferFish;

    @Config.DefaultBoolean(true)
    public static boolean addDurabilityTootip;

    @Config.DefaultBoolean(true)
    public static boolean renderDurabilityBar;

    @Config.DefaultBoolean(true)
    public static boolean renderChargeBar;
}
