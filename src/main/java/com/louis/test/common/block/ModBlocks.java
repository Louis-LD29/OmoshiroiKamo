package com.louis.test.common.block;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import com.louis.test.common.block.test.BlockTest;
import com.louis.test.lib.LibResources;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block blockTest;

    public static void init() {
        blockTest = BlockTest.create();
        initTileEntities();
    }

    private static void initTileEntities() {}

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, LibResources.PREFIX_MOD + key);
    }
}
