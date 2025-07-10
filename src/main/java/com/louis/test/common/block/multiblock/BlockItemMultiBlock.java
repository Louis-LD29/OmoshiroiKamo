package com.louis.test.common.block.multiblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockItemMultiBlock extends ItemBlockWithMetadata {

    public BlockItemMultiBlock() {
        super(ModBlocks.blockMultiBlock, ModBlocks.blockMultiBlock);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public BlockItemMultiBlock(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }
}
