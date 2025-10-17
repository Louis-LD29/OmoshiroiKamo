package ruiseki.omoshiroikamo.common.network;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import ruiseki.omoshiroikamo.common.item.backpack.BackpackController;

public class PacketBackPackState implements IMessage, IMessageHandler<PacketBackPackState, IMessage> {

    public enum SlotType {
        INVENTORY,
        ARMOR,
        BAUBLES
    }

    public PacketBackPackState() {}

    private boolean isActive;
    private SlotType type;
    private int slot;

    public PacketBackPackState(SlotType slottype, int slot, boolean isActive) {
        this.type = slottype;
        this.slot = slot;
        this.isActive = isActive;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(type.ordinal());
        buf.writeInt(slot);
        buf.writeBoolean(isActive);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = SlotType.values()[buf.readShort()];
        slot = buf.readInt();
        isActive = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(PacketBackPackState message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        BackpackController.setBackpackActive(player, message.type, message.slot, message.isActive);
        return null;
    }
}
