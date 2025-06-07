package com.louis.test.common.block.boiler.network;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;

import com.enderio.core.common.util.BlockCoord;

public class BoilerClientNetwork implements IBoilerNetwork {

    private final int id;
    private final Map<BlockCoord, TileBoilerNetwork> members = new HashMap<BlockCoord, TileBoilerNetwork>();

    private int stateUpdateCount;

    public BoilerClientNetwork(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setState(World world, NetworkState state) {
        stateUpdateCount++;
    }

    public int getStateUpdateCount() {
        return stateUpdateCount;
    }

    public void setStateUpdateCount(int stateUpdateCount) {
        this.stateUpdateCount = stateUpdateCount;
    }

    @Override
    public void addMember(TileBoilerNetwork network) {
        members.put(network.getLocation(), network);
    }

    @Override
    public Collection<TileBoilerNetwork> getMembers() {
        return members.values();
    }

    @Override
    public void destroyNetwork() {
        for (TileBoilerNetwork cb : members.values()) {
            cb.setNetworkId(-1);
            cb.setNetwork(null);
        }
    }

    @Override
    public NetworkState getState() {
        return new NetworkState(this);
    }

    @Override
    public void onUpdateEntity(TileBoilerNetwork network) {

    }

}
