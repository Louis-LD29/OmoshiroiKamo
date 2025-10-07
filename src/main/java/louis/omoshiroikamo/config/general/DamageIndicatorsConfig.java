package louis.omoshiroikamo.config.general;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config(
    modid = LibMisc.MOD_ID,
    category = "damage_indicators",
    configSubDirectory = LibMisc.MOD_ID,
    filename = "damage_indicators")
public class DamageIndicatorsConfig {

    @Config.Comment("Main magnet settings")
    public static final DamageIndicators indicatorsConfig = new DamageIndicators();

    @Config.LangKey(LibResources.CONFIG + "damageIndicatorsConfig")
    public static class DamageIndicators {

        @Config.Comment("Color of damage numbers")
        @Config.DefaultInt(0xFFFFFF)
        public int damageColor;

        @Config.Comment("Color of healing numbers")
        @Config.DefaultInt(0x33FF33)
        public int healColor;

        @Config.Comment("Show particles when dealing damage")
        @Config.DefaultBoolean(false)
        public boolean showDamageParticles;
    }
}
