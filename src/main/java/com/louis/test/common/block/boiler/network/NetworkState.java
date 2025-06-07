package com.louis.test.common.block.boiler.network;

import io.netty.buffer.ByteBuf;

public class NetworkState {

    public NetworkState() {}

    public NetworkState(IBoilerNetwork network) {}

    public void writeToBuf(ByteBuf buf) {}

    public static NetworkState readFromBuf(ByteBuf buf) {
        return new NetworkState();
    }

    @Override
    public String toString() {
        return "NetworkClientState";
    }
}
