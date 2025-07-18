package louis.omoshiroikamo.common.mana;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class ManaNetwork extends WorldSavedData {

    private final Map<UUID, Float> currentManaMap;

    public ManaNetwork(String name) {
        super(name);
        this.currentManaMap = new HashMap<>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        Set<String> keys = getAllKeysFromNBT(nbt);
        for (String uuidString : keys) {
            UUID uuid = UUID.fromString(uuidString);
            float mana = nbt.getFloat(uuidString);
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
            nbt.setFloat(
                entry.getKey()
                    .toString(),
                entry.getValue()); // Sử dụng setFloat thay vì setInteger
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
