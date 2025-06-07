package com.louis.test.common.block.boiler.network;

import java.util.Collection;

public interface IBoilerNetwork {

    int getId();

    void addMember(TileBoilerNetwork cap);

    Collection<TileBoilerNetwork> getMembers();

    void destroyNetwork();

    NetworkState getState();

    void onUpdateEntity(TileBoilerNetwork network);

}
