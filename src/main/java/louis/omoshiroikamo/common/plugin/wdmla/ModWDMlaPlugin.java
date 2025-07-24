package louis.omoshiroikamo.common.plugin.wdmla;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.IWDMlaClientRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaCommonRegistration;
import com.gtnewhorizons.wdmla.api.IWDMlaPlugin;
import com.gtnewhorizons.wdmla.api.WDMlaPlugin;
import com.gtnewhorizons.wdmla.plugin.universal.ItemStorageProvider;

import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.block.anvil.BlockAnvil;
import louis.omoshiroikamo.common.core.lib.LibMisc;

@SuppressWarnings("unused")
@WDMlaPlugin
public class ModWDMlaPlugin implements IWDMlaPlugin {

    @Override
    public void registerClient(IWDMlaClientRegistration registration) {
        registration.registerBlockComponent(FluidProvider.INSTANCE, AbstractBlock.class);
        registration.registerBlockComponent(EnergyProvider.INSTANCE, AbstractBlock.class);
    }

    @Override
    public void register(IWDMlaCommonRegistration registration) {
        registration.registerItemStorage(ItemStorageProvider.Extension.INSTANCE, BlockAnvil.class);
    }

    public static ResourceLocation Uid(String uid) {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), uid);
    }
}
