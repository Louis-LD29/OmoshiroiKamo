package com.louis.test.common;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.core.lib.LibMisc;
import com.louis.test.common.item.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TestCreativeTab extends CreativeTabs {

    public static final CreativeTabs INSTANCE = new TestCreativeTab();
    private static final Random rand = new Random();
    public static List list;

    public TestCreativeTab() {
        super(LibMisc.MOD_ID);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModItems.itemMaterial);
    }

    @Override
    public Item getTabIconItem() {
        return getIconItemStack().getItem();
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    public void displayAllReleventItems(List list) {
        this.list = list;

        addItem(ModItems.itemMaterial);
        addItem(ModItems.itemWireCoil);
        addItem(ModItems.itemBucketMaterial);
        addBlock(ModBlocks.blockMaterial);
    }

    private void addItem(Item item) {
        item.getSubItems(item, this, list);
    }

    private void addBlock(Block block) {
        ItemStack stack = new ItemStack(block);
        block.getSubBlocks(stack.getItem(), this, list);
    }

    private void addStack(ItemStack stack) {
        list.add(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTabLabel() {
        return LibMisc.MOD_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return LibMisc.MOD_ID;
    }

    public List getList() {
        return list;
    }

    public void addExternalItem(Item item) {
        if (item != null && list != null) {
            item.getSubItems(item, this, list);
        }
    }

    public void addExternalBlock(Block block) {
        if (block != null && list != null) {
            block.getSubBlocks(Item.getItemFromBlock(block), this, list);
        }
    }
}
