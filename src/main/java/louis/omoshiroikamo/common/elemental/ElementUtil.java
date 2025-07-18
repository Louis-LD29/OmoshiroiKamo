package louis.omoshiroikamo.common.elemental;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import louis.omoshiroikamo.api.enums.ElementType;

public class ElementUtil {

    public static final String ELEMENT_MAP_KEY = "ElementMarks";
    public static final int ELEMENT_DURATION = 20 * 12;
    public static final int ELEMENT_COOLDOWN = 50;

    public static void applyElement(EntityLivingBase entity, ElementType type, long worldTime) {
        NBTTagCompound data = entity.getEntityData();
        NBTTagCompound markData = data.hasKey(ELEMENT_MAP_KEY) ? data.getCompoundTag(ELEMENT_MAP_KEY)
            : new NBTTagCompound();

        String key = String.valueOf(type.ordinal());
        String lastKey = key + "_last";

        long lastApply = markData.hasKey(lastKey) ? markData.getLong(lastKey) : -ELEMENT_COOLDOWN;

        // Check cooldown for this element
        if (worldTime - lastApply < ELEMENT_COOLDOWN) return;

        markData.setLong(key, worldTime + ELEMENT_DURATION); // expire tick
        markData.setLong(lastKey, worldTime); // last apply time

        data.setTag(ELEMENT_MAP_KEY, markData);
    }

    public static boolean isElementActive(EntityLivingBase entity, ElementType type, long worldTime) {
        NBTTagCompound data = entity.getEntityData();
        if (!data.hasKey(ELEMENT_MAP_KEY)) return false;

        NBTTagCompound markData = data.getCompoundTag(ELEMENT_MAP_KEY);
        String key = String.valueOf(type.ordinal());
        return markData.hasKey(key) && worldTime < markData.getLong(key);
    }

    public static void clearElement(EntityLivingBase entity, ElementType type) {
        NBTTagCompound data = entity.getEntityData();
        if (!data.hasKey(ELEMENT_MAP_KEY)) return;

        NBTTagCompound markData = data.getCompoundTag(ELEMENT_MAP_KEY);
        String key = String.valueOf(type.ordinal());
        String lastKey = key + "_last";

        markData.removeTag(key);
        markData.removeTag(lastKey);

        data.setTag(ELEMENT_MAP_KEY, markData);
    }

    public static void tickElements(EntityLivingBase entity, long worldTime) {
        if (!entity.getEntityData()
            .hasKey(ELEMENT_MAP_KEY)) return;

        NBTTagCompound data = entity.getEntityData();
        NBTTagCompound markData = data.getCompoundTag(ELEMENT_MAP_KEY);

        List<String> toRemove = new ArrayList<>();

        for (ElementType type : ElementType.VALUES) {
            if (type == ElementType.NONE) continue;

            String key = String.valueOf(type.ordinal());
            String lastKey = key + "_last";

            if (markData.hasKey(key)) {
                long expire = markData.getLong(key);
                if (worldTime >= expire) {
                    toRemove.add(key);
                    toRemove.add(lastKey);
                }
            }
        }

        for (String k : toRemove) {
            markData.removeTag(k);
        }

        data.setTag(ELEMENT_MAP_KEY, markData);
    }

    public static List<ElementType> getActiveElements(EntityLivingBase entity, long worldTime) {
        List<ElementType> result = new ArrayList<>();
        if (!entity.getEntityData()
            .hasKey(ELEMENT_MAP_KEY)) return result;

        NBTTagCompound markData = entity.getEntityData()
            .getCompoundTag(ELEMENT_MAP_KEY);

        for (ElementType type : ElementType.VALUES) {
            if (type == ElementType.NONE) continue;

            String key = String.valueOf(type.ordinal());
            if (markData.hasKey(key) && worldTime < markData.getLong(key)) {
                result.add(type);
            }
        }

        return result;
    }
}
