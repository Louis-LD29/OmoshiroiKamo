package com.louis.test.common.block.boiler.network;

import com.louis.test.Test;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketNetworkStateResponse implements IMessage, IMessageHandler<PacketNetworkStateResponse, IMessage> {

    private int id;
    private NetworkState state;

    public PacketNetworkStateResponse() {}

    public PacketNetworkStateResponse(IBoilerNetwork network) {
        this(network, false);
    }

    public PacketNetworkStateResponse(IBoilerNetwork network, boolean remove) {
        id = network.getId();
        if (!remove) {
            state = network.getState();
        } else {
            state = null;
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeBoolean(state != null);
        if (state != null) {
            state.writeToBuf(buf);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        boolean hasState = buf.readBoolean();
        if (hasState) {
            state = NetworkState.readFromBuf(buf);
        } else {
            state = null;
        }
    }

    @Override
    public IMessage onMessage(PacketNetworkStateResponse message, MessageContext ctx) {
        if (message.state != null) {
            ClientNetworkManager.getInstance()
                .updateState(Test.proxy.getClientWorld(), message.id, message.state);
        } else {
            ClientNetworkManager.getInstance()
                .destroyNetwork(message.id);
        }
        return null;
    }
}
