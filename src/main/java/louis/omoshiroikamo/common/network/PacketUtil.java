package louis.omoshiroikamo.common.network;

import louis.omoshiroikamo.api.client.IContainerWithTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketUtil {

    public static boolean isInvalidPacketForGui(MessageContext ctx, TileEntity receivedTile, Class<?> messageClass) {
        if (receivedTile == null) {
            // Invalid, but not harmful
            return true;
        }
        if (ctx.side == Side.CLIENT) {
            return false;
        }
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        Container container = player.openContainer;
        if (!(container instanceof IContainerWithTileEntity)) {
            return true;
        }
        TileEntity expectedTile = ((IContainerWithTileEntity) container).getTileEntity();
        if (receivedTile != expectedTile) {
            return true;
        }
        return false;
    }
}
