package louis.omoshiroikamo.common.block.furnace;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.enderio.core.common.TileEntityEnder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;

public class BlockFurnace extends AbstractBlock<TEFurnace> {

    @SideOnly(Side.CLIENT)
    private IIcon icon, iconFrontOff, iconFrontOn, iconTop;

    protected BlockFurnace() {
        super(ModObject.blockFurnace, TEFurnace.class);
    }

    public static BlockFurnace create() {
        BlockFurnace res = new BlockFurnace();
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
        return new TEFurnace();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.icon = reg.registerIcon("furnace_side");
        this.iconTop = reg.registerIcon("furnace_top");
        this.iconFrontOff = reg.registerIcon("furnace_front_off");
        this.iconFrontOn = reg.registerIcon("furnace_front_on");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side == 1 || side == 0) return iconTop;
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        AbstractTE te = (AbstractTE) tileEntity;
        if (side == te.facing) {
            if (te.isActive()) {
                return iconFrontOn;
            }
            return iconFrontOff;
        }
        return icon;
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {}

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEFurnace anvil) {
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
        if (stack == null || stack.stackSize <= 0) return;

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
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random) {
        if (isActive(worldIn, x, y, z)) {
            int l = worldIn.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = random.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                worldIn.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                worldIn.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                worldIn.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                worldIn.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
        super.randomDisplayTick(worldIn, x, y, z, random);
    }
}
