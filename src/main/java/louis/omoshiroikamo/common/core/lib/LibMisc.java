package louis.omoshiroikamo.common.core.lib;

import net.minecraft.launchwrapper.Launch;

import louis.omoshiroikamo.Tags;
import louis.omoshiroikamo.common.core.lang.Lang;

public final class LibMisc {

    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:Baubles;" + "required-after:endercore;"
        + "required-after:structurelib;"
        + "required-after:modularui2;"
        + "required-after:neid;"
        + "after:ImmersiveEngineering;"
        + "after:TConstruct;";

    // Network Contants
    public static final String NETWORK_CHANNEL = MOD_ID;
    // Proxy Constants
    public static final String PROXY_COMMON = Tags.MOD_GROUP + ".CommonProxy";
    public static final String PROXY_CLIENT = Tags.MOD_GROUP + ".ClientProxy";
    public static final String GUI_FACTORY = Tags.MOD_GROUP + ".common.config.ConfigFactory";
    public static final Lang lang = new Lang();

    public static final boolean SNAPSHOT_BUILD = Boolean.parseBoolean(Tags.SNAPSHOT_BUILD);
    public static final boolean DEV_ENVIRONMENT = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final String VERSION_URL = System.getProperty(
        MOD_ID + ".versionUrl",
        "https://raw.githubusercontent.com/Louis-LD29/OmoshiroiKamo/master/updatejson/update.json");

}
