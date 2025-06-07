package com.louis.test.common.block.boiler.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class BoilerType {

    public static final BoilerType SIMPLE = new BoilerType("SIMPLE", "tile.blockCapBank.simple", 500000, true);

    public static final BoilerType ACTIVATED = new BoilerType("ACTIVATED", "tile.blockCapBank.activated", 500000, true);

    private static final List<BoilerType> TYPES = new ArrayList<BoilerType>();

    static {
        TYPES.add(SIMPLE);
        TYPES.add(ACTIVATED);
    }

    public static List<BoilerType> types() {
        return TYPES;
    }

    public static int getMetaFromType(BoilerType type) {
        for (int i = 0; i < TYPES.size(); i++) {
            if (TYPES.get(i) == type) {
                return i;
            }
        }
        return 1;
    }

    public static BoilerType getTypeFromMeta(int meta) {
        meta = MathHelper.clamp_int(meta, 0, TYPES.size() - 1);
        return types().get(meta);
    }

    public static BoilerType getTypeFromUID(String uid) {
        for (BoilerType type : TYPES) {
            if (type.uid.equals(uid)) {
                return type;
            }
        }
        return ACTIVATED;
    }

    private final String uid;
    private final String unlocalizedName;
    private final int maxIO;
    private final boolean isMultiblock;

    public BoilerType(String uid, String unlocalizedName, int maxIO, boolean isMultiblock) {
        this.uid = uid;
        this.unlocalizedName = unlocalizedName;
        this.maxIO = maxIO;
        this.isMultiblock = isMultiblock;
    }

    public int getMaxIO() {
        return maxIO;
    }

    public boolean isMultiblock() {
        return isMultiblock;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getUid() {
        return uid;
    }

    public void writeTypeToNBT(NBTTagCompound nbtRoot) {
        nbtRoot.setString("type", getUid());
    }

    public static BoilerType readTypeFromNBT(NBTTagCompound nbtRoot) {
        if (nbtRoot == null || !nbtRoot.hasKey("type")) {
            return ACTIVATED;
        }
        return getTypeFromUID(nbtRoot.getString("type"));
    }
}
