package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.DimensionBlockPos;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.IICProxy;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.IImmersiveConnectable;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.ImmersiveNetHandler;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.util.IELogger;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public class EventHandler {

    public static boolean validateConnsNextTick = false;

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        if (ImmersiveNetHandler.INSTANCE == null) ImmersiveNetHandler.INSTANCE = new ImmersiveNetHandler();
    }

    // transferPerTick
    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        IESaveData.setDirty(0);
    }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event) {
        IESaveData.setDirty(0);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && validateConnsNextTick
            && FMLCommonHandler.instance()
                .getEffectiveSide() == Side.SERVER) {
            boolean validateConnections = Config.validateConnections;
            int invalidConnectionsDropped = 0;
            for (int dim : ImmersiveNetHandler.INSTANCE.getRelevantDimensions()) {
                World world = MinecraftServer.getServer()
                    .worldServerForDimension(dim);
                if (world == null) {
                    ImmersiveNetHandler.INSTANCE.directConnections.remove(dim);
                    continue;
                }
                if (validateConnections) {
                    for (Connection con : ImmersiveNetHandler.INSTANCE.getAllConnections(world)) {
                        if (!(world.getTileEntity(
                            con.start.posX,
                            con.start.posY,
                            con.start.posZ) instanceof IImmersiveConnectable
                            && world.getTileEntity(
                                con.end.posX,
                                con.end.posY,
                                con.end.posZ) instanceof IImmersiveConnectable)) {
                            ImmersiveNetHandler.INSTANCE.removeConnection(world, con);
                            invalidConnectionsDropped++;
                        }
                    }
                    IELogger.info("removed " + invalidConnectionsDropped + " invalid connections from world");
                }
            }
            if (validateConnections) {
                int invalidProxies = 0;
                Set<DimensionBlockPos> toRemove = new HashSet<>();
                for (Map.Entry<DimensionBlockPos, IICProxy> e : ImmersiveNetHandler.INSTANCE.proxies.entrySet()) {
                    DimensionBlockPos p = e.getKey();
                    World w = MinecraftServer.getServer()
                        .worldServerForDimension(p.dim);
                    if (w == null) {
                        invalidProxies++;
                        toRemove.add(p);
                        continue;
                    }
                    if (!(w.getTileEntity(p.posX, p.posY, p.posZ) instanceof IImmersiveConnectable)) {
                        invalidProxies++;
                        toRemove.add(p);
                    }
                }
                IELogger.info(
                    "Removed " + invalidProxies
                        + " invalid connector proxies (used to transfer power through unloaded chunks)");
            }
            validateConnsNextTick = false;
        }
        if (event.phase == TickEvent.Phase.END && FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            for (Map.Entry<Connection, Integer> e : ImmersiveNetHandler.INSTANCE
                .getTransferedRates(event.world.provider.dimensionId)
                .entrySet()) if (e.getValue() > e.getKey().cableType.getTransferRate()) {
                    if (event.world instanceof WorldServer) for (Vec3 vec : e.getKey()
                        .getSubVertices(event.world))
                        ((WorldServer) event.world)
                            .func_147487_a("flame", vec.xCoord, vec.yCoord, vec.zCoord, 0, 0, .02, 0, 1);
                    ImmersiveNetHandler.INSTANCE.removeConnection(event.world, e.getKey());
                }
            ImmersiveNetHandler.INSTANCE.getTransferedRates(event.world.provider.dimensionId)
                .clear();
        }
    }
}
