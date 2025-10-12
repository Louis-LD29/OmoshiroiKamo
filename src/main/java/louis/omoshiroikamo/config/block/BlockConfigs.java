package louis.omoshiroikamo.config.block;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main Block settings")
@Config.LangKey(LibResources.CONFIG + "blockConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.blocks", configSubDirectory = LibMisc.MOD_ID)
public class BlockConfigs {

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(BlockConfigs.class);
        ConfigurationManager.registerConfig(SolarArrayConfig.class);
    }
}
