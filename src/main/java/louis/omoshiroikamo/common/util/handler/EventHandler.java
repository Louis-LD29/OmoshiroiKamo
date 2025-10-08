package louis.omoshiroikamo.common.util.handler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.DimensionBlockPos;
import louis.omoshiroikamo.api.energy.wire.IWCProxy;
import louis.omoshiroikamo.api.energy.wire.IWireConnectable;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler.Connection;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.world.WireNetSaveData;
import louis.omoshiroikamo.config.GeneralConfig;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
@EventBusSubscriber()
@SuppressWarnings("unused")
public class EventHandler {

    public static boolean validateConnsNextTick = false;

    @SubscribeEvent
    public static void onLoad(WorldEvent.Load event) {
        if (WireNetHandler.INSTANCE == null) {
            WireNetHandler.INSTANCE = new WireNetHandler();
        }
    }

    @SubscribeEvent
    public static void onSave(WorldEvent.Save event) {
        WireNetSaveData.setDirty(0);
    }

    @SubscribeEvent
    public static void onUnload(WorldEvent.Unload event) {
        WireNetSaveData.setDirty(0);
    }

    @SubscribeEvent
    public static void onWorldTick(WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && validateConnsNextTick
            && FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            boolean validateConnections = GeneralConfig.validateConnections;
            int invalidConnectionsDropped = 0;
            for (int dim : WireNetHandler.INSTANCE.getRelevantDimensions()) {
                World world = MinecraftServer.getServer()
                    .worldServerForDimension(dim);
                if (world == null) {
                    WireNetHandler.INSTANCE.directConnections.remove(dim);
                    continue;
                }
                if (validateConnections) {
                    for (Connection con : WireNetHandler.INSTANCE.getAllConnections(world)) {
                        if (!(world
                            .getTileEntity(con.start.posX, con.start.posY, con.start.posZ) instanceof IWireConnectable
                            && world
                            .getTileEntity(con.end.posX, con.end.posY, con.end.posZ) instanceof IWireConnectable)) {
                            WireNetHandler.INSTANCE.removeConnection(world, con);
                            invalidConnectionsDropped++;
                        }
                    }
                    Logger.info("removed " + invalidConnectionsDropped + " invalid connections from world");
                }
            }
            if (validateConnections) {
                int invalidProxies = 0;
                Set<DimensionBlockPos> toRemove = new HashSet<>();
                for (Map.Entry<DimensionBlockPos, IWCProxy> e : WireNetHandler.INSTANCE.proxies.entrySet()) {
                    DimensionBlockPos p = e.getKey();
                    World w = MinecraftServer.getServer()
                        .worldServerForDimension(p.dim);
                    if (w == null) {
                        invalidProxies++;
                        toRemove.add(p);
                        continue;
                    }
                    if (!(w.getTileEntity(p.posX, p.posY, p.posZ) instanceof IWireConnectable)) {
                        invalidProxies++;
                        toRemove.add(p);
                    }
                }
                Logger.info(
                    "Removed " + invalidProxies
                        + " invalid connector proxies (used to transfer power through unloaded chunks)");
            }
            validateConnsNextTick = false;
        }
        if (event.phase == TickEvent.Phase.END && FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            for (Map.Entry<Connection, Integer> e : WireNetHandler.INSTANCE
                .getTransferedRates(event.world.provider.dimensionId)
                .entrySet())
                if (e.getValue() > e.getKey().cableType.getTransferRate()) {
                    if (event.world instanceof WorldServer) {
                        for (Vec3 vec : e.getKey()
                            .getSubVertices(event.world))
                            ((WorldServer) event.world)
                                .func_147487_a("flame", vec.xCoord, vec.yCoord, vec.zCoord, 0, 0, .02, 0, 1);
                    }
                    WireNetHandler.INSTANCE.removeConnection(event.world, e.getKey());
                }
            WireNetHandler.INSTANCE.getTransferedRates(event.world.provider.dimensionId)
                .clear();
        }
    }
}
