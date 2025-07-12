package com.louis.test.common.block.multiblock;

import com.louis.test.api.ModObject;
import com.louis.test.common.block.AbstractBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMultiBlock extends AbstractBlock<TileMultiBlock> {

    protected BlockMultiBlock() {
        super(ModObject.blockMultiblock, TileMultiBlock.class);
    }

    public static BlockMultiBlock create() {
        BlockMultiBlock res = new BlockMultiBlock();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockMultiBlock.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new TileMultiBlock();
    }

}
