package louis.omoshiroikamo.common.plugin.wdmla;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;

import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.core.lib.LibMisc;

@SuppressWarnings("unused")
@WDMlaPlugin
public class ModWDMlaPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(FluidProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(EnergyProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(BlockStatusProvider.INSTANCE, AbstractBlock.class);
        registration.registerItemStorageClient(StorageProvider.INSTANCE);
    }

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerBlockDataProvider(BlockStatusProvider.INSTANCE, AbstractBlock.class);
        registration.registerItemStorage(StorageProvider.INSTANCE, AbstractBlock.class);
    }

    public static ResourceLocation Uid(String uid) {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), uid);
    }
}
