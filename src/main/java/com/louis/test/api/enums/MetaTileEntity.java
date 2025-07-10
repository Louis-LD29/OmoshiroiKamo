package com.louis.test.api.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.louis.test.common.block.meta.MTEBase;
import com.louis.test.common.block.meta.energyConnector.MTEInsulator;

public enum MetaTileEntity {

    INSULATOR(0, MTEInsulator.class)
    // , ULVCONNECTOR(1, MTEConnector.class),
    // LVCONNECTOR(2, MTEConnector.class),
    // MVCONNECTOR(2, MTEConnector.class),
    // HVCONNECTOR(3, MTEConnector.class),
    // EVCONNECTOR(4, MTEConnector.class),
    // IVCONNECTOR(5, MTEConnector.class)
    ;

    private final int baseMeta;
    private final Class<? extends MTEBase> mteClass;

    MetaTileEntity(int baseMeta, Class<? extends MTEBase> mteClass) {
        this.baseMeta = baseMeta;
        this.mteClass = mteClass;
    }

    public MTEBase createInstance(int meta) {
        try {
            return mteClass.getDeclaredConstructor(int.class)
                .newInstance(meta);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MTEBase with meta " + meta, e);
        }
    }

    public static MetaTileEntity fromMeta(int meta) {
        Map.Entry<Integer, MetaTileEntity> entry = META_MAP.floorEntry(meta);
        if (entry != null && meta < entry.getKey() + 100) {
            return entry.getValue();
        }
        throw new IllegalArgumentException("No MetaTileEntity for meta: " + meta);
    }

    public int getBaseMeta() {
        return baseMeta;
    }

    public Class<? extends MTEBase> getMteClass() {
        return mteClass;
    }

    public static int[] getAllBaseMetas() {
        return Arrays.stream(values())
            .mapToInt(MetaTileEntity::getBaseMeta)
            .toArray();
    }

    public static final TreeMap<Integer, MetaTileEntity> META_MAP = new TreeMap<>();

    static {
        for (MetaTileEntity type : values()) {
            META_MAP.put(type.baseMeta, type);
        }
    }
}
