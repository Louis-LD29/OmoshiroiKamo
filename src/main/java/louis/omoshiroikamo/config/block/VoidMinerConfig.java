package louis.omoshiroikamo.config.block;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config.Comment("Main Void Miner settings")
@Config.LangKey(LibResources.CONFIG + "voidMinerConfig")
@Config(modid = LibMisc.MOD_ID, category = "general.blocks.voidMiner", configSubDirectory = LibMisc.MOD_ID)
public class VoidMinerConfig {

    @Config.DefaultInt(400)
    public static int tickOreTier1;

    @Config.DefaultInt(64)
    public static int tickOreTier2;

    @Config.DefaultInt(16)
    public static int tickOreTier3;

    @Config.DefaultInt(4)
    public static int tickOreTier4;

    @Config.DefaultInt(400)
    public static int tickResTier1;

    @Config.DefaultInt(64)
    public static int tickResTier2;

    @Config.DefaultInt(16)
    public static int tickResTier3;

    @Config.DefaultInt(4)
    public static int tickResTier4;

    @Config.DefaultInt(320000)
    public static int energyCostOreTier1;

    @Config.DefaultInt(160000)
    public static int energyCostOreTier2;

    @Config.DefaultInt(80000)
    public static int energyCostOreTier3;

    @Config.DefaultInt(40000)
    public static int energyCostOreTier4;

    @Config.DefaultInt(320000)
    public static int energyCostResTier1;

    @Config.DefaultInt(160000)
    public static int energyCostResTier2;

    @Config.DefaultInt(80000)
    public static int energyCostResTier3;

    @Config.DefaultInt(40000)
    public static int energyCostResTier4;
}
