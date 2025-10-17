package ruiseki.omoshiroikamo.common.network;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import com.enderio.core.common.util.BlockCoord;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import ruiseki.omoshiroikamo.common.block.multiblock.AbstractMultiBlockModifierTE;

public class PacketMBClientUpdate implements IMessage, IMessageHandler<PacketMBClientUpdate, IMessage> {

    protected int x;
    protected int y;
    protected int z;
    protected UUID player;
    protected boolean isFormed;
    private boolean isProcessing;
    private int currentDuration;
    private int currentProgress;

    public PacketMBClientUpdate() {}

    public PacketMBClientUpdate(AbstractMultiBlockModifierTE tile) {
        BlockCoord bc = tile.getLocation();
        this.x = bc.x;
        this.y = bc.y;
        this.z = bc.z;
        this.player = tile.getPlayerID();
        this.isFormed = tile.isFormed();
        this.isProcessing = tile.isProcessing();
        this.currentDuration = tile.getCurrentDuration();
        this.currentProgress = tile.getCurrentProgress();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        long msb = buf.readLong();
        long lsb = buf.readLong();
        this.player = new UUID(lsb, msb);
        this.isFormed = buf.readBoolean();
        this.isProcessing = buf.readBoolean();
        this.currentDuration = buf.readInt();
        this.currentProgress = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeLong(this.player.getLeastSignificantBits());
        buf.writeLong(this.player.getMostSignificantBits());
        buf.writeBoolean(this.isFormed);
        buf.writeBoolean(this.isProcessing);
        buf.writeInt(this.currentDuration);
        buf.writeInt(this.currentProgress);
    }

    @Override
    public IMessage onMessage(PacketMBClientUpdate message, MessageContext ctx) {
        if (ctx.getClientHandler() != null) {
            TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(message.x, message.y, message.z);
            if (te instanceof AbstractMultiBlockModifierTE nbb) {
                nbb.setPlayer(message.player);
                nbb.setFormed(message.isFormed);
                nbb.setProcessing(message.isProcessing);
                nbb.setCurrentDuration(message.currentDuration);
                nbb.setCurrentProgress(message.currentProgress);
            }
        }

        return null;
    }
}
