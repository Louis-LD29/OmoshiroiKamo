package ruiseki.omoshiroikamo.plugin.wdmla;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;

import ruiseki.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibMods;
import ruiseki.omoshiroikamo.config.GeneralConfig;

@SuppressWarnings("unused")
@WDMlaPlugin
public class ModWDMlaPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        if (!LibMods.WDMLA.isLoaded() || !GeneralConfig.useWDMLA) {
            return;
        }
        registration.registerBlockComponent(CustomProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(BlockStatusProvider.INSTANCE, AbstractBlock.class);
        registration.registerProgressClient(BlockProcessProvider.INSTANCE);
        registration.registerBlockComponent(FluidProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(EnergyProvider.INSTANCE, AbstractBlock.class);
        registration.registerItemStorageClient(StorageProvider.INSTANCE);
    }

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        if (!LibMods.WDMLA.isLoaded() || !GeneralConfig.useWDMLA) {
            return;
        }
        registration.registerBlockDataProvider(CustomProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockDataProvider(BlockStatusProvider.INSTANCE, AbstractBlock.class);
        registration.registerProgress(BlockProcessProvider.INSTANCE, AbstractBlock.class);
        registration.registerItemStorage(StorageProvider.INSTANCE, AbstractBlock.class);
    }

    public static ResourceLocation Uid(String uid) {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), uid);
    }
}
