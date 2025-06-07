package com.louis.test.common.block.boiler.network;

import java.util.*;

import net.minecraft.world.World;

import com.louis.test.core.network.PacketHandler;

public class BoilerNetwork implements IBoilerNetwork {

    private static final int IO_CAP = 2000000000;

    private final List<TileBoilerNetwork> capBanks = new ArrayList<TileBoilerNetwork>();

    private final int id;

    private int maxIO;

    private long timeAtLastApply;

    private BoilerType type;

    public BoilerNetwork(int id) {
        this.id = id;
    }

    public void init(TileBoilerNetwork cap, Collection<TileBoilerNetwork> neighbours, World world) {
        if (world.isRemote) {
            throw new UnsupportedOperationException();
        }

        type = cap.getType();
        for (TileBoilerNetwork con : neighbours) {
            IBoilerNetwork network = con.getNetwork();
            if (network != null) {
                network.destroyNetwork();
            }
        }
        setNetwork(world, cap);
    }

    protected void setNetwork(World world, TileBoilerNetwork cap) {
        if (cap == null) {
            return;
        }
        Set<TileBoilerNetwork> work = new HashSet<TileBoilerNetwork>();
        for (;;) {
            IBoilerNetwork network = cap.getNetwork();
            if (network != this) {
                if (network != null) {
                    network.destroyNetwork();
                }
                if (cap.setNetwork(this)) {
                    addMember(cap);
                    NetworkUtil.getNeigbours(cap, work);
                }
            }
            if (work.isEmpty()) {
                return;
            }
            Iterator<TileBoilerNetwork> iter = work.iterator();
            cap = iter.next();
            iter.remove();
        }
    }

    @Override
    public void destroyNetwork() {
        TileBoilerNetwork cap = null;
        for (TileBoilerNetwork cb : capBanks) {
            cb.setNetwork(null);
            if (cap == null) {
                cap = cb;
            }
        }
        capBanks.clear();
        if (cap != null) {
            PacketHandler.INSTANCE.sendToAll(new PacketNetworkStateResponse(this, true));
        }
    }

    @Override
    public Collection<TileBoilerNetwork> getMembers() {
        return capBanks;
    }

    @Override
    public void addMember(TileBoilerNetwork cap) {
        if (!capBanks.contains(cap)) {
            capBanks.add(cap);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public NetworkState getState() {
        return new NetworkState(this);
    }

    @Override
    public void onUpdateEntity(TileBoilerNetwork network) {
        World world = network.getWorldObj();
        if (world == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }
        long curTime = world.getTotalWorldTime();
        if (curTime != timeAtLastApply) {
            timeAtLastApply = curTime;
        }
    }

}
