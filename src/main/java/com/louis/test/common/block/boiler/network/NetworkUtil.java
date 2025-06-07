package com.louis.test.common.block.boiler.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;

public class NetworkUtil {

    private static AtomicInteger nextID = new AtomicInteger(0);

    public static void ensureValidNetwork(TileBoilerNetwork cap) {
        World world = cap.getWorldObj();
        Collection<TileBoilerNetwork> neighbours = getNeigbours(cap);
        if (reuseNetwork(cap, neighbours, world)) {
            return;
        }
        BoilerNetwork network = new BoilerNetwork(nextID.getAndIncrement());
        network.init(cap, neighbours, world);
        return;
    }

    public static Collection<TileBoilerNetwork> getNeigbours(TileBoilerNetwork cap) {
        if (!cap.getType()
            .isMultiblock()) {
            return Collections.emptyList();
        }
        Collection<TileBoilerNetwork> res = new ArrayList<TileBoilerNetwork>();
        getNeigbours(cap, res);
        return res;
    }

    public static void getNeigbours(TileBoilerNetwork cap, Collection<TileBoilerNetwork> res) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            BlockCoord bc = cap.getLocation()
                .getLocation(dir);
            TileEntity te = cap.getWorldObj()
                .getTileEntity(bc.x, bc.y, bc.z);
            if (te instanceof TileBoilerNetwork) {
                TileBoilerNetwork neighbour = (TileBoilerNetwork) te;
                if (neighbour.canConnectTo(cap)) {
                    res.add(neighbour);
                }
            }
        }
    }

    private static boolean reuseNetwork(TileBoilerNetwork cap, Collection<TileBoilerNetwork> neighbours, World world) {
        IBoilerNetwork network = null;
        for (TileBoilerNetwork conduit : neighbours) {
            if (network == null) {
                network = conduit.getNetwork();
            } else if (network != conduit.getNetwork()) {
                return false;
            }
        }
        if (network == null) {
            return false;
        }
        if (cap.setNetwork(network)) {
            network.addMember(cap);
            // network.notifyNetworkOfUpdate();
            return true;
        }
        return false;
    }
}
