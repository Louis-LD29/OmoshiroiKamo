package com.louis.test.common.block;

import net.minecraft.block.material.Material;

import com.enderio.core.common.BlockEnder;
import com.louis.test.common.TestCreativeTab;

public class BlockEio extends BlockEnder {

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass) {
        super(name, teClass);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass, Material mat) {
        super(name, teClass, mat);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

}
