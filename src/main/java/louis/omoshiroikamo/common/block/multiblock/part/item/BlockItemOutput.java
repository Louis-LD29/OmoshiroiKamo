package louis.omoshiroikamo.common.block.multiblock.part.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;

public class BlockItemOutput extends AbstractBlock<TEItemOutput> {

    protected BlockItemOutput() {
        super(ModObject.blockItemOutput, TEItemOutput.class);
    }

    public static BlockItemOutput create() {
        BlockItemOutput res = new BlockItemOutput();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, BlockItemItemOutput.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TEItemOutput(meta);
    }
}
