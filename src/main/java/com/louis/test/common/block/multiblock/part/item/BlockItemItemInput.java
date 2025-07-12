package com.louis.test.common.block.multiblock.part.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.api.client.IAdvancedTooltipProvider;
import com.louis.test.api.material.MaterialEntry;
import com.louis.test.api.material.MaterialRegistry;
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
        MaterialEntry material = MaterialRegistry.fromMeta(meta);
        return super.getUnlocalizedName(stack) + "." + material.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> list) {
        for (int i = 0; i < MaterialRegistry.all()
            .size(); i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        int meta = itemstack.getItemDamage();
        MaterialEntry material = MaterialRegistry.fromMeta(meta);
        list.add("§7Material:§f " + material.getName());
        list.add("§7Slot:§f " + material.getItemSlotCount());
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

}
