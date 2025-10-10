package louis.omoshiroikamo.common.block.multiblock.solarArray;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractMachineBlock;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockSolarArray extends AbstractMachineBlock<TESolarArray> {

    protected BlockSolarArray() {
        super(ModObject.blockSolarArray, TESolarArray.class);
    }

    public static BlockSolarArray create() {
        BlockSolarArray res = new BlockSolarArray();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockSolarArray.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TESolarArray(meta);
    }

    @Override
    protected String getMachineFrontIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "solarPanelFront";
    }

}
