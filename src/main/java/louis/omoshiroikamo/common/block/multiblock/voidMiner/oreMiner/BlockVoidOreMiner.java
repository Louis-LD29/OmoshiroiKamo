package louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractMachineBlock;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;

public class BlockVoidOreMiner extends AbstractMachineBlock<TEVoidMiner> {

    protected BlockVoidOreMiner() {
        super(ModObject.blockVoidOreMiner, TEVoidMiner.class);
    }

    public static BlockVoidOreMiner create() {
        BlockVoidOreMiner res = new BlockVoidOreMiner();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockVoidOreMiner.class, name);
        GameRegistry.registerTileEntity(TEVoidOreMinerT1.class, "TEVoidOreMinerT1TileEntity");
        GameRegistry.registerTileEntity(TEVoidOreMinerT2.class, "TEVoidOreMinerT2TileEntity");
        GameRegistry.registerTileEntity(TEVoidOreMinerT3.class, "TEVoidOreMinerT3TileEntity");
        GameRegistry.registerTileEntity(TEVoidOreMinerT4.class, "TEVoidOreMinerT4TileEntity");
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
        switch (meta) {
            case 3:
                return new TEVoidOreMinerT4();
            case 2:
                return new TEVoidOreMinerT3();
            case 1:
                return new TEVoidOreMinerT2();
            default:
                return new TEVoidOreMinerT1();
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {

    }
}
