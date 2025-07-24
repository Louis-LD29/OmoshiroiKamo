package louis.omoshiroikamo.common.block.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;

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
