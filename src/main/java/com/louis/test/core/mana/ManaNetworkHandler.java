package com.louis.test.core.mana;

import java.util.UUID;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;

public class ManaNetworkHandler {

    private static final int MAX_MANA = 1000;

    public static ManaNetwork getManaNetworkData(World world) {
        ManaNetwork data = (ManaNetwork) world.loadItemData(ManaNetwork.class, "ManaNetworkData");
        if (data == null) {
            data = new ManaNetwork("ManaNetworkData");
            world.setItemData("ManaNetworkData", data);
        }
        return data;
    }

    // Cập nhật currentMana cho player
    public static void setCurrentMana(UUID playerUUID, float mana) {
        MinecraftServer mcServer = FMLCommonHandler.instance()
            .getMinecraftServerInstance();
        if (mcServer == null) {
            return;
        }

        World world = mcServer.worldServers[0];
        ManaNetwork data = getManaNetworkData(world);
        data.setCurrentMana(playerUUID, mana);
        data.markDirty();
    }

    // Lấy currentMana của player
    public static float getCurrentMana(UUID playerUUID) {
        MinecraftServer mcServer = FMLCommonHandler.instance()
            .getMinecraftServerInstance();
        if (mcServer == null) {
            return 0;
        }

        World world = mcServer.worldServers[0];
        ManaNetwork data = getManaNetworkData(world);

        return data.getCurrentMana(playerUUID);
    }

    // Lấy maxMana của thế giới
    public static int getMaxMana() {
        return MAX_MANA; // Trả về maxMana của thế giới
    }
}
