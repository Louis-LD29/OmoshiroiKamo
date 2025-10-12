package louis.omoshiroikamo.config.block;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main solar array settings")
@Config.LangKey(LibResources.CONFIG + "solarArrayConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.items.solarArray", configSubDirectory = LibMisc.MOD_ID)
public class SolarArrayConfig {

    @Config.DefaultInt(720)
    public static int peakEnergyTier1;

    @Config.DefaultInt(4000)
    public static int peakEnergyTier2;

    @Config.DefaultInt(15680)
    public static int peakEnergyTier3;

    @Config.DefaultInt(51840)
    public static int peakEnergyTier4;
}
