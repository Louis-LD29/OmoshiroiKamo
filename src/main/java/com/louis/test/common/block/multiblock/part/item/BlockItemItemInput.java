package com.louis.test.common.block.multiblock.part.item;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.api.enums.Material;
import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockItemItemInput extends ItemBlockWithMetadata implements IAdvancedTooltipProvider {

    public BlockItemItemInput() {
        super(ModBlocks.blockItemInput, ModBlocks.blockItemInput);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public BlockItemItemInput(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        Material material = Material.fromMeta(meta);
        return super.getUnlocalizedName(stack) + "."
            + material.name()
                .toLowerCase(Locale.ROOT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> list) {
        for (int i = 0; i < Material.values().length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        int meta = itemstack.getItemDamage();
        Material material = Material.fromMeta(meta);
        list.add("§7Material:§f " + material.getDisplayName());
        list.add("§7Slot:§f " + material.getItemSlotCount());
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

}
