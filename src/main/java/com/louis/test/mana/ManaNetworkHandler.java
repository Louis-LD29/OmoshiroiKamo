package com.louis.test.mana;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import java.util.UUID;

public class ManaNetworkHandler {

    private static final int MAX_MANA = 1000;

    // Lấy hoặc tạo dữ liệu ManaNetwork cho thế giới
    public static ManaNetwork getManaNetworkData(World world) {
        ManaNetwork data = (ManaNetwork) world.loadItemData(ManaNetwork.class, "ManaNetworkData");

        if (data == null) {
            data = new ManaNetwork("ManaNetworkData");
            world.setItemData("ManaNetworkData", data); // Lưu dữ liệu vào thế giới
        }

        return data;
    }

    // Cập nhật currentMana cho player
    public static void setCurrentMana(UUID playerUUID, float mana) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return;
        }

        World world = mcServer.worldServers[0];
        ManaNetwork data = getManaNetworkData(world); // Sử dụng phương thức đã tạo

        data.setCurrentMana(playerUUID, mana); // Cập nhật mana của player
        data.markDirty(); // Đánh dấu dữ liệu đã thay đổi để lưu vào world
    }

    // Lấy currentMana của player
    public static float getCurrentMana(UUID playerUUID) {
        MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (mcServer == null) {
            return 0; // Trả về 0 nếu server không tồn tại
        }

        World world = mcServer.worldServers[0]; // Lấy thế giới từ server
        ManaNetwork data = getManaNetworkData(world); // Sử dụng phương thức đã tạo

        return data.getCurrentMana(playerUUID); // Trả về currentMana của player theo UUID
    }

    // Lấy maxMana của thế giới
    public static int getMaxMana() {
        return MAX_MANA; // Trả về maxMana của thế giới
    }
}
