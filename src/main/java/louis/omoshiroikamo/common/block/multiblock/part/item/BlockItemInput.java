package louis.omoshiroikamo.common.block.multiblock.part.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;

public class BlockItemInput extends AbstractBlock<TEItemInput> {

    protected BlockItemInput() {
        super(ModObject.blockItemInput, TEItemInput.class);
    }

    public static BlockItemInput create() {
        BlockItemInput res = new BlockItemInput();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, BlockItemItemInput.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TEItemInput(meta);
    }
}
