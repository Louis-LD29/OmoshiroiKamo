package com.louis.test.lib;

import com.louis.test.Tags;
import com.louis.test.core.Lang;

public final class LibMisc {

    public static final String MOD_ID = "Test";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:Baubles;" + "required-after:CoFHLib@(1.0.3B3,);"
        + "required-after:endercore;";

    // Network Contants
    public static final String NETWORK_CHANNEL = MOD_ID;

    // Proxy Constants
    public static final String PROXY_COMMON = "com.louis.test.CommonProxy";
    public static final String PROXY_CLIENT = "com.louis.test.ClientProxy";
    public static final String GUI_FACTORY = "com.louis.test.common.config.ConfigFactory";
    public static final Lang lang = new Lang(MOD_ID);
    public static final String DOMAIN = MOD_ID;

    public static final int[] CONTROL_CODE_COLORS = new int[] { 0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000,
        0xAA00AA, 0xFFAA00, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF };

}
