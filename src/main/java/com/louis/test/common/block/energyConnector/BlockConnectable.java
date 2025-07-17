package com.louis.test.common.block.energyConnector;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.TileEntityEnder;
import com.louis.test.api.enums.ModObject;
import com.louis.test.client.render.block.connectable.ConnectableISBRH;
import com.louis.test.common.block.AbstractBlock;
import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.core.helper.Logger;
import com.louis.test.common.core.lib.LibResources;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockConnectable extends AbstractBlock<TEConnectable> {

    public static String[] blocks = new String[] { "insulator", "connectorULV", "connectorLV", "connectorMV",
        "connectorHV", "connectorEV", "connectorIV" };

    public static final int META_insulator = 0;
    public static final int META_connectorULV = 1;
    public static final int META_connectorLV = 2;
    public static final int META_connectorMV = 3;
    public static final int META_connectorHV = 4;
    public static final int META_connectorEV = 5;
    public static final int META_connectorIV = 6;

    protected BlockConnectable() {
        super(ModObject.blockConnectable, TEConnectable.class);
    }

    public static BlockConnectable create() {
        BlockConnectable res = new BlockConnectable();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockConnectable.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(TEInsulator.class, modObject.unlocalisedName + "_insulator");
        GameRegistry.registerTileEntity(TEConnectorULV.class, modObject.unlocalisedName + "_connectorULV");
        GameRegistry.registerTileEntity(TEConnectorLV.class, modObject.unlocalisedName + "_connectorLV");
        GameRegistry.registerTileEntity(TEConnectorMV.class, modObject.unlocalisedName + "_connectorMV");
        GameRegistry.registerTileEntity(TEConnectorHV.class, modObject.unlocalisedName + "_connectorHV");
        GameRegistry.registerTileEntity(TEConnectorEV.class, modObject.unlocalisedName + "_connectorEV");
        GameRegistry.registerTileEntity(TEConnectorIV.class, modObject.unlocalisedName + "_connectorIV");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        switch (meta) {
            case META_insulator:
                return new TEInsulator();
            case META_connectorULV:
                return new TEConnectorULV();
            case META_connectorLV:
                return new TEConnectorLV();
            case META_connectorMV:
                return new TEConnectorMV();
            case META_connectorHV:
                return new TEConnectorHV();
            case META_connectorEV:
                return new TEConnectorEV();
            case META_connectorIV:
                return new TEConnectorIV();
        }
        return null;
    }

    @Override
    public int getRenderType() {
        return ConnectableISBRH.renderConnectableId;
    }

    private IIcon[] icons;

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        icons = new IIcon[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            String iconName = LibResources.PREFIX_MOD + blocks[i];
            icons[i] = iIconRegister.registerIcon(iconName);
            Logger.info("[IconRegister] Registered icon: " + iconName);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (icons == null || meta < 0 || meta >= icons.length) return blockIcon;
        return icons[meta];
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {

    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        if (world.isRemote) return;
        TileEntity ent = world.getTileEntity(x, y, z);
        if (!(ent instanceof AbstractTE te)) return;
        ForgeDirection fd = ForgeDirection.getOrientation(te.getFacing())
            .getOpposite();
        int nx = x + fd.offsetX;
        int ny = y + fd.offsetY;
        int nz = z + fd.offsetZ;

        if (world.isAirBlock(nx, ny, nz)) {
            world.removeTileEntity(x, y, z);
            Block self = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            self.dropBlockAsItem(world, x, y, z, meta, 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {

        ForgeDirection direction = null;
        float pitch = player.rotationPitch;

        int dx = x, dy = y, dz = z;
        if (pitch > 60) {
            direction = ForgeDirection.UP;
        } else if (pitch < -60) {
            direction = ForgeDirection.DOWN;
        }
        if (direction == null) {
            int yaw = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
            switch (yaw) {
                case 0:
                    direction = ForgeDirection.NORTH;
                    break;
                case 1:
                    direction = ForgeDirection.EAST;
                    break;
                case 2:
                    direction = ForgeDirection.SOUTH;
                    break;
                case 3:
                default:
                    direction = ForgeDirection.WEST;
                    break;
            }

            ForgeDirection opposite = direction.getOpposite();
            int tx = x + opposite.offsetX;
            int ty = y + opposite.offsetY;
            int tz = z + opposite.offsetZ;
            Block targetBlock = world.getBlock(tx, ty, tz);

            if (targetBlock == null || targetBlock.isAir(world, tx, ty, tz)) {
                direction = null;
            }
        }

        if (direction == null) {
            dy = y + 1;
            if (world.isSideSolid(dx, dy, dz, ForgeDirection.UP, false)) {
                direction = ForgeDirection.DOWN;
            } else {
                dy = y - 1;
                if (world.isSideSolid(dx, dy, dz, ForgeDirection.DOWN, false)) {
                    direction = ForgeDirection.UP;
                } else {
                    world.setBlockToAir(x, y, z);
                    if (player instanceof EntityPlayerMP playerMP) {
                        if (!playerMP.inventory.addItemStackToInventory(stack.copy())) {
                            playerMP.dropPlayerItemWithRandomChoice(stack.copy(), false);
                        }
                    }
                    return;
                }
            }
        }

        AbstractTE te = (AbstractTE) world.getTileEntity(x, y, z);
        te.setFacing((short) direction.ordinal());
        if (!world.isRemote) {
            world.markBlockForUpdate(x, y, z);
        }
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        TileEntity ent = world.getTileEntity(x, y, z);
        if (!(ent instanceof AbstractTE te)) return;
        ForgeDirection dir = ForgeDirection.getOrientation(te.getFacing());

        float length = te instanceof TEConnectorULV ? .25f
            : te instanceof TEConnectorLV ? 0.34375f
                : te instanceof TEConnectorMV ? 0.34375f
                    : te instanceof TEConnectorHV ? 0.4375f : te instanceof TEConnectorEV ? 0.4375f : .5f;
        switch (dir) {
            case UP:
                setBlockBounds(0.375F, 0F, 0.375F, 0.625F, length, 0.625F);
                break;
            case DOWN:
                setBlockBounds(0.375F, 1 - length, 0.375F, 0.625F, 1F, 0.625F);
                break;
            case NORTH:
                setBlockBounds(0.375F, 0.375F, 1 - length, 0.625F, 0.625F, 1F);
                break;
            case SOUTH:
                setBlockBounds(0.375F, 0.375F, 0F, 0.625F, 0.625F, length);
                break;
            case EAST:
                setBlockBounds(0F, 0.375F, 0.375F, length, 0.625F, 0.625F);
                break;
            case WEST:
                setBlockBounds(1 - length, 0.375F, 0.375F, 1F, 0.625F, 0.625F);
                break;
        }
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
}
