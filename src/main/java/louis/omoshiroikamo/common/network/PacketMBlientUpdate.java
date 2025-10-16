package louis.omoshiroikamo.common.network;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import com.enderio.core.common.util.BlockCoord;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import louis.omoshiroikamo.common.block.multiblock.AbstractMultiBlockModifierTE;

public class PacketMBlientUpdate implements IMessage, IMessageHandler<PacketMBlientUpdate, IMessage> {

    protected int x;
    protected int y;
    protected int z;
    protected UUID player;

    public PacketMBlientUpdate() {}

    public PacketMBlientUpdate(AbstractMultiBlockModifierTE tile) {
        BlockCoord bc = tile.getLocation();
        this.x = bc.x;
        this.y = bc.y;
        this.z = bc.z;
        this.player = tile.getPlayerID();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        long lsb = buf.readLong();
        long msb = buf.readLong();
        this.player = new UUID(lsb, msb);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeLong(this.player.getLeastSignificantBits());
        buf.writeLong(this.player.getMostSignificantBits());
    }

    @Override
    public IMessage onMessage(PacketMBlientUpdate message, MessageContext ctx) {
        if (ctx.getClientHandler() != null) {
            TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x, message.y, message.z);
            if (te instanceof AbstractMultiBlockModifierTE nbb) {
                nbb.setPlayer(message.player);
            }
        }

        return null;
    }
}
