package com.louis.test.common.block.multiblock;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.common.block.machine.AbstractMachineBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidOutput extends AbstractMachineBlock<TileFluidOutput> implements IAdvancedTooltipProvider {

    public static BlockFluidOutput create() {
        BlockFluidOutput res = new BlockFluidOutput();
        res.init();
        return res;
    }

    protected BlockFluidOutput() {
        super(ModObject.blockFluidOutput, TileFluidOutput.class);
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, BlockItemFluidOutput.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
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
        return new TileFluidOutput();
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
        System.out.println("BlockFluidOutput.getUnlocalizedNameForTooltip:  ");
        return stack.getUnlocalizedName();
    }

}
