package louis.omoshiroikamo.plugin.waila;

import net.minecraft.entity.EntityLivingBase;

import cpw.mods.fml.common.event.FMLInterModComms;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibMods;
import louis.omoshiroikamo.config.GeneralConfig;
import mcp.mobius.waila.api.IWailaRegistrar;

@SuppressWarnings("deprecation")
public class WailaCompat {

    public static void wailaCallback(IWailaRegistrar registrar) {

        // Configs
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".fluidTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".energyTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".processTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".activeTE");

        // Provider
        registrar.registerBodyProvider(new FluidTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new EnergyTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new ProcessTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new ActiveTEProvider(), AbstractTE.class);

        // ==== Entity ElementType Provider ====
        ElementWailaProvider provider = new ElementWailaProvider();
        registrar.registerBodyProvider(provider, EntityLivingBase.class);
        registrar.registerHeadProvider(provider, EntityLivingBase.class);
        registrar.registerNBTProvider(provider, EntityLivingBase.class);
    }

    public static void init() {
        if (!LibMods.Waila.isLoaded()) {
            return;
        }

        if (LibMods.WDMLA.isLoaded() && GeneralConfig.useWDMLA) {
            Logger.info("Loaded WDMLACompat");
            return;
        }

        String callback = WailaCompat.class.getCanonicalName() + ".wailaCallback";
        FMLInterModComms.sendMessage("Waila", "register", callback);
        Logger.info("Loaded WailaCompat: " + callback);
    }

}
