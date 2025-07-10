package com.louis.test.core.lib;

import com.louis.test.Tags;
import com.louis.test.core.Lang;

public final class LibMisc {

    public static final String MOD_ID = "Test";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:Baubles;" + "required-after:endercore;"
        + "required-after:structurelib;"
        + "required-after:modularui2;"
        + "required-after:neid;"
        + "after:ImmersiveEngineering";

    // Network Contants
    public static final String NETWORK_CHANNEL = MOD_ID;

    // Proxy Constants
    public static final String PROXY_COMMON = "com.louis.test.CommonProxy";
    public static final String PROXY_CLIENT = "com.louis.test.ClientProxy";
    public static final String GUI_FACTORY = "com.louis.test.common.config.ConfigFactory";
    public static final Lang lang = new Lang(MOD_ID);
    public static final String DOMAIN = MOD_ID;

}
