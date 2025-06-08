package com.louis.test.common.block.multiblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockItemFluidInput extends ItemBlockWithMetadata implements IAdvancedTooltipProvider {

    public BlockItemFluidInput() {
        super(ModBlocks.blockFluidInput, ModBlocks.blockFluidInput);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public BlockItemFluidInput(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int meta = par1ItemStack.getItemDamage();
        String result = super.getUnlocalizedName(par1ItemStack);
        if (meta == 1) {
            result += ".advanced";
        }
        return result;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
        ItemStack stack = new ItemStack(this, 1, 0);
        par3List.add(stack);
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }
}
