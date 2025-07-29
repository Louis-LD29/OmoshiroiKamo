package louis.omoshiroikamo.common.plugin.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.IWailaInfoProvider;
import louis.omoshiroikamo.common.recipes.IProgressTile;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class ProcessTEDataProvider implements IWailaDataProvider {

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
        if (!config.getConfig(LibMisc.MOD_ID + ".processTE")) return currenttip;

        TileEntity tile = accessor.getTileEntity();

        if (tile instanceof IWailaInfoProvider provider && provider.hasProcessStatus()) {
            if (tile instanceof IProgressTile progressTile) {
                float progress = progressTile.getProgress();
                if (progress > 0.0f) {
                    currenttip.add(String.format("§6Progress§r: §e%.0f%%", progress * 100));
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
