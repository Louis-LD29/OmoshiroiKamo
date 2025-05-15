package com.louis.test.common.block;

import com.louis.test.lib.LibResources;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class ModBlocks {

    public static void init() {
    }
    private static void initTileEntities() {
//        registerTile(TileAltar.class, LibBlockNames.ALTAR);
    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, LibResources.PREFIX_MOD + key);
    }
}
