package louis.omoshiroikamo.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibMisc.MOD_ID);

    public static void init() {
        INSTANCE
            .registerMessage(PacketBackPackState.class, PacketBackPackState.class, PacketHandler.nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketIoMode.class, PacketIoMode.class, PacketHandler.nextID(), Side.SERVER);

        INSTANCE.registerMessage(PacketFluidTanks.class, PacketFluidTanks.class, PacketHandler.nextID(), Side.CLIENT);
        INSTANCE
            .registerMessage(PacketPowerStorage.class, PacketPowerStorage.class, PacketHandler.nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketMBlientUpdate.class, PacketMBlientUpdate.class, nextID(), Side.CLIENT);
        INSTANCE.registerMessage(PacketNBBClientFlight.class, PacketNBBClientFlight.class, nextID(), Side.CLIENT);
    }

    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range) {
        INSTANCE.sendToAllAround(
            message,
            new TargetPoint(te.getWorldObj().provider.dimensionId, te.xCoord, te.yCoord, te.zCoord, range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te) {
        sendToAllAround(message, te, 64);
    }

    public static void sendToAllAround(IMessage message, EntityPlayer player, double range) {
        INSTANCE.sendToAllAround(
            message,
            new TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range));
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }
}
