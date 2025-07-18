package louis.omoshiroikamo.common.plugin.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.fluid.IFluidHandlerAdv;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.common.block.AbstractTE;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;

public class FluidTEDataProvider implements IWailaDataProvider {

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
        if (!config.getConfig(LibMisc.MOD_ID + ".fluidTE")) return currenttip;

        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof IFluidHandlerAdv handler)) return currenttip;
        AbstractTE te = (AbstractTE) tileEntity;

        SmartTank[] tanks = handler.getTanks();
        if (tanks == null || tanks.length == 0) return currenttip;

        for (SmartTank tank : tanks) {
            if (tank == null) continue;

            if (tank.getFluid() != null && tank.getFluid()
                .getFluid() != null) {
                String fluidName = tank.getFluid()
                    .getLocalizedName();
                int fluidAmount = tank.getFluidAmount();
                int capacity = tank.getCapacity();
                int fluidTemp = tank.getFluid()
                    .getFluid()
                    .getTemperature();

                currenttip.add(
                    String.format(
                        "§b%s§r: §a%,d§7 / §a%,d§7 mB §8| §e%d§7 K",
                        fluidName,
                        fluidAmount,
                        capacity,
                        fluidTemp));
            } else {
                currenttip.add(String.format("§7Empty§r: §a0§7 / §a%,d§7 mB", tank.getCapacity()));
            }
        }

        if (accessor.getPlayer()
            .isSneaking() && te.getMaterial() != null) {
            int meltPointK = (int) te.getMaterial()
                .getMeltingPointK();
            currenttip.add(String.format("§6Melting Point§r: §c%d§7 K", meltPointK));
        } else if (te.getMaterial() != null) {
            currenttip.add("§7(Hold §eShift§7 for details)");
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
