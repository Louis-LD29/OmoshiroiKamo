package louis.omoshiroikamo.config;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.SimpleGuiConfig;

import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.config.item.ItemConfig;

public class OKGuiConfig extends SimpleGuiConfig {

    public OKGuiConfig(GuiScreen parent) throws ConfigException {
        super(
            parent,
            LibMisc.MOD_ID,
            LibMisc.MOD_NAME,
            GeneralConfig.class,
            ItemConfig.class,
            MagnetConfig.class,
            DamageIndicatorsConfig.class);
    }

}
