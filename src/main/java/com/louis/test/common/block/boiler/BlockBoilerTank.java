package com.louis.test.common.block.boiler;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.cleanroommc.modularui.factory.GuiFactories;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.common.block.machine.AbstractMachineBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBoilerTank extends AbstractMachineBlock<TileBoilerTank> implements IAdvancedTooltipProvider {

    public static BlockBoilerTank create() {
        BlockBoilerTank res = new BlockBoilerTank();
        res.init();
        return res;
    }

    protected BlockBoilerTank() {
        super(ModObject.blockBoilerTank, TileBoilerTank.class);
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, BlockItemBoilerTank.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer playerIn, int side, float hitX,
        float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            GuiFactories.tileEntity()
                .open(playerIn, x, y, z);
        }
        return true;
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs p_149666_2_, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new TileBoilerTank();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        int meta = world.getBlockMetadata(x, y, z);
        meta = MathHelper.clamp_int(meta, 0, 1);
        if (meta == 1) {
            return 2000;
        } else {
            return super.getExplosionResistance(par1Entity);
        }
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack stack) {
        System.out.println("BlockBoilerTank.getUnlocalizedNameForTooltip: ");
        return stack.getUnlocalizedName();
    }

    // @Override
    // public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
    // ((TileSolarPanel) world.getTileEntity(x, y, z)).renderHUD(mc, res);
    // }
    //
    // @Override
    // public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
    // ((TileSolarPanel) world.getTileEntity(x, y, z)).onWanded(player, stack);
    // return true;
    // }

}
