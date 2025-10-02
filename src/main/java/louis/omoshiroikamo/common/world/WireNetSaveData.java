package louis.omoshiroikamo.common.world;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.DimensionBlockPos;
import louis.omoshiroikamo.api.energy.wire.IWCProxy;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler.Connection;
import louis.omoshiroikamo.common.util.handlers.EventHandler;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is WireNetSaveData
 */

public class WireNetSaveData extends WorldSavedData {

    private static WireNetSaveData INSTANCE;
    public static final String dataName = "OK-SaveData";

    public WireNetSaveData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int[] savedDimensions = nbt.getIntArray("savedDimensions");
        for (int dim : savedDimensions) {
            NBTTagList connectionList = nbt.getTagList("connectionList" + dim, 10);
            WireNetHandler.INSTANCE.clearAllConnections(dim);
            for (int i = 0; i < connectionList.tagCount(); i++) {
                NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
                Connection con = Connection.readFromNBT(conTag);
                if (con != null) {
                    WireNetHandler.INSTANCE.addConnection(dim, con.start, con);
                }
            }
        }
        NBTTagList l = nbt.getTagList("iwcProxies", 10);
        int max = l.tagCount();
        for (int i = 0; i < max; i++) WireNetHandler.INSTANCE.addProxy(IWCProxy.readFromNBT(l.getCompoundTagAt(i)));

        EventHandler.validateConnsNextTick = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        Integer[] relDim = WireNetHandler.INSTANCE.getRelevantDimensions()
            .toArray(new Integer[0]);
        int[] savedDimensions = new int[relDim.length];
        for (int ii = 0; ii < relDim.length; ii++) savedDimensions[ii] = relDim[ii];

        nbt.setIntArray("savedDimensions", savedDimensions);
        for (int dim : savedDimensions) {
            World world = MinecraftServer.getServer()
                .worldServerForDimension(dim);
            if (world != null) {
                NBTTagList connectionList = new NBTTagList();
                for (Connection con : WireNetHandler.INSTANCE.getAllConnections(world)) {
                    connectionList.appendTag(con.writeToNBT());
                }
                nbt.setTag("connectionList" + dim, connectionList);
            }
        }

        NBTTagList iwcProxies = new NBTTagList();
        for (Map.Entry<DimensionBlockPos, IWCProxy> prox : WireNetHandler.INSTANCE.proxies.entrySet()) {
            NBTTagCompound c = prox.getValue()
                .writeToNBT();
            iwcProxies.appendTag(c);
        }
        nbt.setTag("iwcProxies", iwcProxies);
    }

    public static void setDirty(int dimension) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER && INSTANCE != null) {
            INSTANCE.markDirty();
        }
    }

    public static void setInstance(int dimension, WireNetSaveData in) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            INSTANCE = in;
        }
    }

}
