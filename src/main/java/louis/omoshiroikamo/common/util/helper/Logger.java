package louis.omoshiroikamo.common.util.helper;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class Logger {

    public static boolean debug = false;

    public Logger() {}

    public static void log(Level logLevel, Object object) {
        FMLLog.log(LibMisc.MOD_NAME, logLevel, String.valueOf(object), new Object[0]);
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

    public static void debug(Object object) {
        log(Level.DEBUG, object);
    }
}
