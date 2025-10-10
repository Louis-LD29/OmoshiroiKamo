package louis.omoshiroikamo.common.block.anvil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.enderio.core.common.TileEntityEnder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.client.render.block.anvil.AnvilISBRH;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockAnvil extends AbstractBlock<TEAnvil> {

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    protected BlockAnvil() {
        super(ModObject.blockAnvil, TEAnvil.class);
        setStepSound(soundTypeStone);
    }

    public static BlockAnvil create() {
        BlockAnvil res = new BlockAnvil();
        res.init();
        return res;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TEAnvil();
    }

    @Override
    public int getRenderType() {
        return AnvilISBRH.renderAnvilId;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        icon = reg.registerIcon(LibResources.PREFIX_MOD + "anvil");
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        setBlockBounds(0f, 0f, 0f, 1f, 6f / 16f, 1f);
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEAnvil anvil) {
            for (int i = 0; i < anvil.getSizeInventory(); i++) {
                ItemStack stack = anvil.getStackInSlot(i);
                if (stack != null) {
                    dropStack(world, x, y, z, stack);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    public static void dropStack(World world, int x, int y, int z, ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) {
            return;
        }

        float dx = world.rand.nextFloat() * 0.8F + 0.1F;
        float dy = world.rand.nextFloat() * 0.8F + 0.1F;
        float dz = world.rand.nextFloat() * 0.8F + 0.1F;

        EntityItem entityItem = new EntityItem(world, x + dx, y + dy, z + dz, stack.copy());

        float motion = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * motion;
        entityItem.motionY = world.rand.nextGaussian() * motion + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * motion;

        world.spawnEntityInWorld(entityItem);
    }

    @Override
    protected String getMachineFrontIconKey(boolean active) {
        return "";
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
