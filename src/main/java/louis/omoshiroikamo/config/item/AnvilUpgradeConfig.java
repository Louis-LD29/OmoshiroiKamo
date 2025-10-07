package louis.omoshiroikamo.config.item;

import com.gtnewhorizon.gtnhlib.config.Config;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

@Config(
    modid = LibMisc.MOD_ID,
    category = "item.anvil_upgrade",
    configSubDirectory = LibMisc.MOD_ID,
    filename = "anvil_upgrade")
public class AnvilUpgradeConfig {

    @Config.Comment("Main anvil upgrade settings")
    public static final AnvilUpgrade anvilUpgradeConfig = new AnvilUpgrade();

    @Config.LangKey(LibResources.CONFIG + "anvilUpgradeConfig")
    public static class AnvilUpgrade {

        @Config.DefaultInt(100000)
        public int energyTier1Capacity;

        @Config.DefaultInt(250000)
        public int energyTier2Capacity;

        @Config.DefaultInt(500000)
        public int energyTier3Capacity;

        @Config.DefaultInt(1000000)
        public int energyTier4Capacity;

        @Config.DefaultInt(2500000)
        public int energyTier5Capacity;

        @Config.DefaultInt(10)
        public int energyTier1Cost;

        @Config.DefaultInt(10)
        public int energyTier2Cost;

        @Config.DefaultInt(15)
        public int energyTier3Cost;

        @Config.DefaultInt(20)
        public int energyTier4Cost;

        @Config.DefaultInt(25)
        public int energyTier5Cost;
    }
}
