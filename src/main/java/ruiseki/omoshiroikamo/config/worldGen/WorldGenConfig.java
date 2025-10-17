package ruiseki.omoshiroikamo.config.worldGen;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main World Gen settings")
@Config.LangKey(LibResources.CONFIG + "worldGenConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.worldGen", configSubDirectory = LibMisc.MOD_ID)
public class WorldGenConfig {

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(WorldGenConfig.class);
    }

    @Config.DefaultBoolean(true)
    public static boolean enableHardenedStoneGeneration;

    @Config.DefaultInt(30)
    public static int hardenedStoneNodeSize;

    @Config.DefaultInt(12)
    public static int hardenedStoneNodes;

    @Config.DefaultInt(0)
    public static int hardenedStoneMinHeight;

    @Config.DefaultInt(12)
    public static int hardenedStoneMaxHeight;

    @Config.DefaultBoolean(true)
    public static boolean enableAlabasterGeneration;

    @Config.DefaultInt(22)
    public static int alabasterNodes;

    @Config.DefaultInt(30)
    public static int alabasterNodeSize;

    @Config.DefaultInt(40)
    public static int alabasterMinHeight;

    @Config.DefaultInt(200)
    public static int alabasterMaxHeight;

    @Config.DefaultBoolean(true)
    public static boolean enableBasaltGeneration;

    @Config.DefaultInt(14)
    public static int basaltNodes;

    @Config.DefaultInt(28)
    public static int basaltNodeSize;

    @Config.DefaultInt(8)
    public static int basaltMinHeight;

    @Config.DefaultInt(32)
    public static int basaltMaxHeight;
}
