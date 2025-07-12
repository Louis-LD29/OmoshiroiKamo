package com.louis.test.common.block;

import com.enderio.core.common.TileEntityEnder;
import com.louis.test.api.ModObject;
import com.louis.test.api.client.IResourceTooltipProvider;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public abstract class AbstractBlock<T extends AbstractTE> extends BlockEio implements IResourceTooltipProvider {

    public static int renderId;
    protected final Random random;
    protected final ModObject modObject;
    protected final Class<T> teClass;

    protected AbstractBlock(ModObject mo, Class<T> teClass, Material mat) {
        super(mo.unlocalisedName, teClass, mat);
        modObject = mo;
        this.teClass = teClass;
        setHardness(2.0F);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
        random = new Random();
    }

    protected AbstractBlock(ModObject mo, Class<T> teClass) {
        this(mo, teClass, new Material(MapColor.ironColor));
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return false;
    }

    @Override
    public boolean doNormalDrops(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            ((AbstractTE) tile).breakBlock(world, x, y, z, blockBroken, meta);
        }
        world.markBlockForUpdate(x, y, z);
        super.breakBlock(world, x, y, z, blockBroken, meta);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockId) {
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent instanceof AbstractTE te) {
            te.onNeighborBlockChange(world, x, y, z, blockId);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
                                    float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            return ((AbstractTE) tile)
                .onBlockActivated(world, player, ForgeDirection.getOrientation(side), hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof AbstractTE tile) {
            tile.onBlockPlacedBy(world, x, y, z, player, stack);
        }
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemIn) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof AbstractTE tile) {
            tile.dropBlockAsItem(world, x, y, z, itemIn);
        }
        super.dropBlockAsItem(world, x, y, z, itemIn);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof AbstractTE tile) {
            tile.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);
        }
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
        if (te != null) {
            ((AbstractTE) te).processDrop(world, x, y, z, te, stack);
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            return ((AbstractTE) tile).canConnectRedstone(world, x, y, z, side);
        }
        return super.canConnectRedstone(world, x, y, z, side);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            return ((AbstractTE) tile).onBlockEventReceived(world, x, y, z, eventId, eventData);
        }
        return super.onBlockEventReceived(world, x, y, z, eventId, eventData);
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    // Renderer

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            ((AbstractTE) tile).setBlockBoundsBasedOnState(world, x, y, z, this);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
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
