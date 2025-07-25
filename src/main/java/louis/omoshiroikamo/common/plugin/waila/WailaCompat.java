package louis.omoshiroikamo.common.plugin.waila;

import net.minecraft.entity.EntityLivingBase;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.core.helper.Logger;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.core.lib.LibMods;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaCompat {

    @Deprecated
    public static void wailaCallback(IWailaRegistrar registrar) {

        // Configs
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".fluidTE");
        registrar.addConfig(LibMisc.MOD_NAME, LibMisc.MOD_ID + ".energyTE");

        // Provider
        registrar.registerBodyProvider(new FluidTEDataProvider(), AbstractTE.class);
        registrar.registerBodyProvider(new EnergyTEDataProvider(), AbstractTE.class);

        // ==== Entity ElementType Provider ====
        ElementWailaProvider provider = new ElementWailaProvider();
        registrar.registerBodyProvider(provider, EntityLivingBase.class);
        registrar.registerHeadProvider(provider, EntityLivingBase.class);
        registrar.registerNBTProvider(provider, EntityLivingBase.class);
    }

    public static void init() {
        if (!LibMods.waila) {
            return;
        }
        if (Loader.isModLoaded("wdmla")) {
            return;
        }
        Logger.info("Loaded WailaCompat");
        FMLInterModComms
            .sendMessage("Waila", "register", "com.louis.test.common.plugin.waila.WailaRegistrar.wailaCallback");
    }
}
