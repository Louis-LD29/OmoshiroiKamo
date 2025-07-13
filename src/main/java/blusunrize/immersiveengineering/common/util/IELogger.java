package blusunrize.immersiveengineering.common.util;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public class IELogger {

    public static boolean debug = false;

    public IELogger() {}

    public static void log(Level logLevel, Object object) {
        FMLLog.log("ImmersiveEngineering", logLevel, String.valueOf(object), new Object[0]);
    }

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void warn(Object object) {
        log(Level.WARN, object);
    }

    public static void debug(Object object) {}
}
