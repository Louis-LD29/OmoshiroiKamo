package louis.omoshiroikamo.common.plugin.wdmla;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;

import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.core.lib.LibMisc;

@SuppressWarnings("unused")
@WDMlaPlugin
public class ModWDMlaPlugin implements IWDMlaPlugin {

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerFluidStorage(FluidProvider.INSTANCE, AbstractTE.class);
        registration.registerBlockDataProvider(EnergyProvider.INSTANCE, AbstractBlock.class);
    }

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerFluidStorageClient(FluidProvider.INSTANCE);
        registration.registerBlockComponent(FluidThemeBlockProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(EnergyProvider.INSTANCE, AbstractBlock.class);
    }

    public static ResourceLocation Uid(String uid) {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), uid);
    }
}
