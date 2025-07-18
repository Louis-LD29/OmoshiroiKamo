package louis.omoshiroikamo.common.core.lib;

import louis.omoshiroikamo.Tags;
import louis.omoshiroikamo.common.core.lang.Lang;

public final class LibMisc {

    public static final String MOD_ID = "OmoshiroiKamo";
    public static final String MOD_NAME = "Omoshiroi Kamo";
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
    public static final String PROXY_COMMON = "louis.omoshiroikamo.CommonProxy";
    public static final String PROXY_CLIENT = "louis.omoshiroikamo.ClientProxy";
    public static final String GUI_FACTORY = "louis.omoshiroikamo.common.config.ConfigFactory";
    public static final Lang lang = new Lang();

}
