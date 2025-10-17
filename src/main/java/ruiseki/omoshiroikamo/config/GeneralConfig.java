package ruiseki.omoshiroikamo.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.config.block.BlockConfigs;
import ruiseki.omoshiroikamo.config.general.DamageIndicatorsConfig;
import ruiseki.omoshiroikamo.config.item.FeedingConfig;
import ruiseki.omoshiroikamo.config.item.ItemConfig;
import ruiseki.omoshiroikamo.config.item.MagnetConfig;
import ruiseki.omoshiroikamo.config.worldGen.WorldGenConfig;

@Config(modid = LibMisc.MOD_ID, configSubDirectory = LibMisc.MOD_ID, category = "general")
public class GeneralConfig {

    @Config.DefaultBoolean(true)
    public static boolean useWDMLA;

    @Config.DefaultBoolean(true)
    public static boolean increasedRenderboxes;

    @Config.DefaultBoolean(true)
    public static boolean validateConnections;

    @Config.DefaultBoolean(false)
    public static boolean allowExternalTickSpeedup;

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(GeneralConfig.class);
        ItemConfig.registerConfig();
        BlockConfigs.registerConfig();
        WorldGenConfig.registerConfig();
        ConfigurationManager.registerConfig(MagnetConfig.class);
        ConfigurationManager.registerConfig(FeedingConfig.class);
        ConfigurationManager.registerConfig(DamageIndicatorsConfig.class);
    }
}
