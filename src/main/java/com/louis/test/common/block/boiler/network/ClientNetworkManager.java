package com.louis.test.common.block.boiler.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientNetworkManager {

    private static final ClientNetworkManager instance = new ClientNetworkManager();

    static {
        MinecraftForge.EVENT_BUS.register(instance);
    }

    public static ClientNetworkManager getInstance() {
        return instance;
    }

    private final Map<Integer, BoilerClientNetwork> networks = new HashMap<Integer, BoilerClientNetwork>();

    ClientNetworkManager() {}

    public void destroyNetwork(int id) {
        BoilerClientNetwork res = networks.remove(id);
        if (res != null) {
            res.destroyNetwork();
        }
    }

    public void updateState(World world, int id, NetworkState state) {
        BoilerClientNetwork network = getOrCreateNetwork(id);
        network.setState(world, state);
    }

    public void updateEnergy(int id, long energyStored, float avgInput, float avgOutput) {
        BoilerClientNetwork res = networks.get(id);
        if (res == null) {
            return;
        }
    }

    public BoilerClientNetwork getOrCreateNetwork(int id) {
        BoilerClientNetwork res = networks.get(id);
        if (res == null) {
            res = new BoilerClientNetwork(id);
            networks.put(id, res);
        }
        return res;
    }

    public void addToNetwork(int id, TileBoilerNetwork tileCapBank) {
        BoilerClientNetwork network = getOrCreateNetwork(id);
        network.addMember(tileCapBank);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            networks.forEach((id, network) -> network.destroyNetwork());
            networks.clear();
        }
    }

}
