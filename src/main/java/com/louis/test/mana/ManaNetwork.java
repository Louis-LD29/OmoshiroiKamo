package com.louis.test.mana;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.lang.reflect.Field;
import java.util.*;

public class ManaNetwork extends WorldSavedData {
    private Map<UUID, Float> currentManaMap;
    private int maxMana = 1000;

    public ManaNetwork(String name) {
        super(name);
        this.currentManaMap = new HashMap<>();
        this.maxMana = maxMana;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        Set<String> keys = getAllKeysFromNBT(nbt);
        System.out.println("playerUUID readFromNBT: " + keys);
        // Đọc mana của từng player
        for (String uuidString : keys) {
            System.out.println("uuidString readFromNBT: " + uuidString);
            UUID uuid = UUID.fromString(uuidString);
            float mana = nbt.getFloat(uuidString); // Lấy mana cho từng UUID
            currentManaMap.put(uuid, mana);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> getAllKeysFromNBT(NBTTagCompound nbt) {
        try {
            Field field = NBTTagCompound.class.getDeclaredField("tagMap"); // hoặc "field_74784_a" nếu obfuscated
            field.setAccessible(true);
            Map<String, NBTBase> tagMap = (Map<String, NBTBase>) field.get(nbt);
            return tagMap.keySet();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        for (Map.Entry<UUID, Float> entry : currentManaMap.entrySet()) {
            nbt.setFloat(entry.getKey().toString(), entry.getValue()); // Sử dụng setFloat thay vì setInteger
        }
    }

    // Lấy currentMana của player (dùng UUID)
    public float getCurrentMana(UUID playerUUID) {
        return currentManaMap.getOrDefault(playerUUID, 0f); // Trả về 0 nếu chưa có
    }

    // Cập nhật currentMana cho player (dùng UUID)
    public void setCurrentMana(UUID playerUUID, float mana) {
        currentManaMap.put(playerUUID, mana);
        markDirty(); // Đánh dấu dữ liệu đã thay đổi
    }

}
