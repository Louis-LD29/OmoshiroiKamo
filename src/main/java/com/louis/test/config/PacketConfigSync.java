package com.louis.test.config;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketConfigSync implements IMessage, IMessageHandler<PacketConfigSync, IMessage> {
    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf data) {

    }

    @Override
    public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {
        return null;
    }
}
