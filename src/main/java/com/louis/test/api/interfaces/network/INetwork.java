package com.louis.test.api.interfaces.network;

import net.minecraft.world.World;

import com.enderio.core.common.util.BlockCoord;

public interface INetwork {

    public int joinDelay = 5;

    BlockCoord getLocation();

    World getWorldObj();

    boolean canConnectTo(INetwork other);

}
