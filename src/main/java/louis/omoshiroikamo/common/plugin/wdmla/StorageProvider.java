package louis.omoshiroikamo.common.plugin.wdmla;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.ItemView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.gtnewhorizons.wdmla.plugin.universal.ItemStorageProvider.Extension;

import louis.omoshiroikamo.api.IWailaInfoProvider;

public enum StorageProvider
    implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return ModWDMlaPlugin.Uid("te_storage");
    }

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor accessor, List<ViewGroup<ItemStack>> groups) {
        return Extension.INSTANCE.getClientGroups(accessor, groups);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(Accessor accessor) {
        Object target = accessor.getTarget();
        if (!(target instanceof TileEntity tileEntity)) return null;
        if (!(tileEntity instanceof IWailaInfoProvider provider)) return null;
        if (!provider.hasItemStorage()) return null;
        return Extension.INSTANCE.getGroups(accessor);
    }

    @Override
    public int getDefaultPriority() {
        return 10000;
    }

    @Override
    public boolean shouldRequestData(Accessor accessor) {
        Object target = accessor.getTarget();
        return target instanceof IWailaInfoProvider;
    }
}
