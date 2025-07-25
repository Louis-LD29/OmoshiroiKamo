package louis.omoshiroikamo.common.plugin.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.IWailaInfoProvider;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class ActiveTEProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
                                     IWailaConfigHandler config) {
        if (!config.getConfig(LibMisc.MOD_ID + ".activeTE")) return currenttip;

        TileEntity tile = accessor.getTileEntity();

        if (tile instanceof IWailaInfoProvider provider && provider.hasActiveStatus()) {
            if (tile instanceof AbstractTE te) {
                if (te.isActive()) {
                    currenttip.add("§aStatus: Running");
                } else {
                    currenttip.add("§7Status: Idle");
                }
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
        int y, int z) {
        return tag;
    }
}
