package ruiseki.omoshiroikamo.plugin.compat;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;

@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2")
public class IC2Compat {

    public static final double EU_TO_RF = 4.0;
    public static final double RF_TO_EU = 1.0 / EU_TO_RF;

    public static int euToRf(double eu) {
        return (int) Math.round(eu * EU_TO_RF);
    }

    public static double rfToEu(int rf) {
        return rf * RF_TO_EU;
    }

    @Optional.Method(modid = "IC2")
    public static void loadIC2Tile(TileEntity tile) {
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) tile));
    }

    @Optional.Method(modid = "IC2")
    public static void unloadIC2Tile(TileEntity tile) {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) tile));
    }

    @Optional.Method(modid = "IC2")
    public static boolean isEnergySink(TileEntity sink) {
        return sink instanceof IEnergySink;
    }

    @Optional.Method(modid = "IC2")
    public static boolean isAcceptingEnergySink(TileEntity sink, TileEntity tile, ForgeDirection fd) {
        return sink instanceof IEnergySink && ((IEnergySink) sink).acceptsEnergyFrom(tile, fd);
    }

    @Optional.Method(modid = "IC2")
    public static double injectEnergy(TileEntity sink, ForgeDirection fd, double amount, double voltage,
        boolean simulate) {
        double demanded = Math.max(0, ((IEnergySink) sink).getDemandedEnergy());
        double accepted = Math.min(demanded, amount);
        if (!simulate) {
            ((IEnergySink) sink).injectEnergy(fd, amount, voltage);
        }
        return amount - accepted;
    }
}
