package louis.omoshiroikamo.common.block.multiblock.solarArray;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractMachineBlock;

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
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
        list.add(new ItemStack(this, 1, 3));
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
}
