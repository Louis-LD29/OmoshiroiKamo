package ruiseki.omoshiroikamo.plugin.wdmla;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerDataProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.StatusHelper;

import ruiseki.omoshiroikamo.api.IWailaInfoProvider;
import ruiseki.omoshiroikamo.common.block.abstractClass.AbstractTE;

public enum BlockStatusProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return;
        }
        if (!provider.hasActiveStatus()) {
            return;
        }

        if (accessor.getServerData()
            .hasKey("State")) {
            byte state = accessor.getServerData()
                .getByte("State");
            switch (state) {
                case 1:
                    tooltip.child(StatusHelper.INSTANCE.runningFine());
                    break;
                case 2:
                    tooltip.child(StatusHelper.INSTANCE.idle());
                    break;
                default:
                    tooltip.child(StatusHelper.INSTANCE.structureIncomplete());
                    break;
            }
        }
    }

    @Override
    public void appendServerData(NBTTagCompound data, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof AbstractTE te)) {
            return;
        }
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return;
        }
        if (!provider.hasActiveStatus()) {
            return;
        }

        if (te.isActive()) {
            data.setByte("State", (byte) 1);
        } else {
            data.setByte("State", (byte) 2);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModWDMlaPlugin.Uid("te_status");
    }
}
