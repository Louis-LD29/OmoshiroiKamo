package louis.omoshiroikamo.common.block.multiblock.solarArray;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.BlockOK;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockSolarCell extends BlockOK {

    protected BlockSolarCell() {
        super(ModObject.blockSolarCell, TESolarCell.class);
    }

    public static BlockSolarCell create() {
        BlockSolarCell res = new BlockSolarCell();
        res.init();
        return res;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TESolarCell();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "solar_cell_top");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        setBlockBounds(0f, 0f, 0f, 1f, 13f / 16f, 1f);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

}
