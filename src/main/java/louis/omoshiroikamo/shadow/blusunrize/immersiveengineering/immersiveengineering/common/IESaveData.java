package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.DimensionBlockPos;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.IICProxy;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.ImmersiveNetHandler;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

public class IESaveData extends WorldSavedData {

    // private static HashMap<Integer, IESaveData> INSTANCE = new HashMap<Integer, IESaveData>();
    private static IESaveData INSTANCE;
    public static final String dataName = "ImmersiveEngineering-SaveData";

    public IESaveData(String s) {
        super(s);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int[] savedDimensions = nbt.getIntArray("savedDimensions");
        for (int dim : savedDimensions) {
            NBTTagList connectionList = nbt.getTagList("connectionList" + dim, 10);
            ImmersiveNetHandler.INSTANCE.clearAllConnections(dim);
            for (int i = 0; i < connectionList.tagCount(); i++) {
                NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
                Connection con = Connection.readFromNBT(conTag);
                if (con != null) {
                    ImmersiveNetHandler.INSTANCE.addConnection(dim, con.start, con);
                }
            }
        }
        NBTTagList l = nbt.getTagList("iicProxies", 10);
        int max = l.tagCount();
        for (int i = 0; i < max; i++)
            ImmersiveNetHandler.INSTANCE.addProxy(IICProxy.readFromNBT(l.getCompoundTagAt(i)));

        EventHandler.validateConnsNextTick = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        Integer[] relDim = ImmersiveNetHandler.INSTANCE.getRelevantDimensions()
            .toArray(new Integer[0]);
        int[] savedDimensions = new int[relDim.length];
        for (int ii = 0; ii < relDim.length; ii++) savedDimensions[ii] = relDim[ii];

        nbt.setIntArray("savedDimensions", savedDimensions);
        for (int dim : savedDimensions) {
            World world = MinecraftServer.getServer()
                .worldServerForDimension(dim);
            if (world != null) {
                NBTTagList connectionList = new NBTTagList();
                for (Connection con : ImmersiveNetHandler.INSTANCE.getAllConnections(world)) {
                    connectionList.appendTag(con.writeToNBT());
                }
                nbt.setTag("connectionList" + dim, connectionList);
            }
        }

        NBTTagList iicProxies = new NBTTagList();
        for (Map.Entry<DimensionBlockPos, IICProxy> prox : ImmersiveNetHandler.INSTANCE.proxies.entrySet()) {
            NBTTagCompound c = prox.getValue()
                .writeToNBT();
            iicProxies.appendTag(c);
        }
        nbt.setTag("iicProxies", iicProxies);
    }

    public static void setDirty(int dimension) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER && INSTANCE != null) INSTANCE.markDirty();
    }

    public static void setInstance(int dimension, IESaveData in) {
        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) INSTANCE = in;
    }

}
