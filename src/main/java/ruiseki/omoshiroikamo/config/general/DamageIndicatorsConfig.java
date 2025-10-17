package ruiseki.omoshiroikamo.config.general;

import com.gtnewhorizon.gtnhlib.config.Config;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main magnet settings")
@Config.LangKey(LibResources.CONFIG + "damageIndicatorsConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.damage_indicators", configSubDirectory = LibMisc.MOD_ID)
public class DamageIndicatorsConfig {

    @Config.Comment("Color of damage numbers")
    @Config.DefaultInt(0xFFFFFF)
    public static int damageColor;

    @Config.Comment("Color of healing numbers")
    @Config.DefaultInt(0x33FF33)
    public static int healColor;

    @Config.Comment("Show particles when dealing damage")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean showDamageParticles;
}
