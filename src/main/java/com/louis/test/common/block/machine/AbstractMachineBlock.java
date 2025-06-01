package com.louis.test.common.block.machine;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.enderio.core.common.TileEntityEnder;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.IResourceTooltipProvider;
import com.louis.test.common.block.BlockEio;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractMachineBlock<T extends AbstractMachineEntity> extends BlockEio
    implements IResourceTooltipProvider {

    public static int renderId;

    protected final Random random;

    protected final ModObject modObject;

    protected AbstractMachineBlock(ModObject mo, Class<T> teClass, Material mat) {
        super(mo.unlocalisedName, teClass, mat);
        modObject = mo;
        setHardness(2.0F);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
        random = new Random();
    }

    protected AbstractMachineBlock(ModObject mo, Class<T> teClass) {
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
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return false;
    }

    @Override
    public boolean doNormalDrops(World world, int x, int y, int z) {
        return false;
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
        if (te != null) {
            ((AbstractMachineEntity) te).writeToItemStack(stack);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        AbstractMachineEntity te = (AbstractMachineEntity) world.getTileEntity(x, y, z);
        te.setFacing(getFacingForHeading(heading));
        te.readFromItemStack(stack);
        if (world.isRemote) {
            return;
        }
        world.markBlockForUpdate(x, y, z);
    }

    protected short getFacingForHeading(int heading) {
        switch (heading) {
            case 0:
                return 2;
            case 1:
                return 5;
            case 2:
                return 3;
            case 3:
            default:
                return 4;
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockId) {
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent instanceof AbstractMachineEntity) {
            AbstractMachineEntity te = (AbstractMachineEntity) ent;
            te.onNeighborBlockChange(blockId);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        // If active, randomly throw some smoke around
        if (isActive(world, x, y, z)) {
            float startX = x + 1.0F;
            float startY = y + 1.0F;
            float startZ = z + 1.0F;
            for (int i = 0; i < 4; i++) {
                float xOffset = -0.2F - rand.nextFloat() * 0.6F;
                float yOffset = -0.1F + rand.nextFloat() * 0.2F;
                float zOffset = -0.2F - rand.nextFloat() * 0.6F;
                world.spawnParticle("smoke", startX + xOffset, startY + yOffset, startZ + zOffset, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected boolean isActive(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te instanceof AbstractMachineEntity) {
            return ((AbstractMachineEntity) te).isActive();
        }
        return false;
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack stack) {
        return getUnlocalizedName();
    }
}
