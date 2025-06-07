package com.louis.test.common.block.boiler.network;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketNetworkIdRequest extends PacketBoiler<PacketNetworkIdRequest, PacketNetworkIdResponse> {

    public PacketNetworkIdRequest() {}

    public PacketNetworkIdRequest(TileBoilerNetwork capBank) {
        super(capBank);
    }

    @Override
    protected PacketNetworkIdResponse handleMessage(TileBoilerNetwork te, PacketNetworkIdRequest message,
        MessageContext ctx) {
        if (te.getNetwork() != null) {
            return new PacketNetworkIdResponse(te);
        }
        return null;
    }
}
