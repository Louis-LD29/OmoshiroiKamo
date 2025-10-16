package louis.omoshiroikamo.common.network;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import louis.omoshiroikamo.common.util.PlayerUtils;

public class PacketNBBClientFlight implements IMessage, IMessageHandler<PacketNBBClientFlight, IMessage> {

    protected UUID player;
    protected boolean enabled;

    public PacketNBBClientFlight() {}

    public PacketNBBClientFlight(UUID player, boolean enabledFlight) {
        this.player = player;
        this.enabled = enabledFlight;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.enabled = buf.readBoolean();
        long l1 = buf.readLong();
        long l2 = buf.readLong();
        this.player = new UUID(l2, l1);

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.enabled);
        buf.writeLong(this.player.getLeastSignificantBits());
        buf.writeLong(this.player.getMostSignificantBits());
    }

    @Override
    public IMessage onMessage(PacketNBBClientFlight message, MessageContext ctx) {
        if (ctx.getClientHandler() != null) {
            EntityPlayer plr = PlayerUtils
                .getPlayerFromWorldClient(Minecraft.getMinecraft().thePlayer.worldObj, message.player);
            if (plr != null && plr.getUniqueID()
                .compareTo(message.player) == 0) {
                plr.capabilities.allowFlying = message.enabled;
                if (plr.capabilities.isFlying && !message.enabled) {
                    plr.capabilities.isFlying = false;
                }
            }
        }

        return null;
    }
}
