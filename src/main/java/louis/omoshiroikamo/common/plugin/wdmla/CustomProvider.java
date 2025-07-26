package louis.omoshiroikamo.common.plugin.wdmla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;

import louis.omoshiroikamo.api.IWailaInfoProvider;

public enum CustomProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof IWailaInfoProvider provider)) return;

        provider.appendTooltip(tooltip, accessor);
    }

    @Override
    public void appendServerData(NBTTagCompound data, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof IWailaInfoProvider provider)) return;

        provider.appendServerData(data, accessor);
    }

    @Override
    public ResourceLocation getUid() {
        return ModWDMlaPlugin.Uid("te_custom");
    }
}
