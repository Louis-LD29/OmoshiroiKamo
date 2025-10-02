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

    @Config.LangKey(LibResources.CONFIG_LANG_KEY + "anvilUpgradeConfig")
    public static class AnvilUpgrade {

        @Config.DefaultInt(50000)
        public int manaPowerStorageBase;

        @Config.DefaultInt(10)
        public int manaUpgradeDiamondCost;
    }
}
