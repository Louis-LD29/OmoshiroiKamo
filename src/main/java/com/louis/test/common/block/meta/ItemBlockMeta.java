package com.louis.test.common.block.meta;

import com.louis.test.api.mte.MetaTileEntity;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockMeta extends ItemBlockWithMetadata {

    public ItemBlockMeta() {
        super(ModBlocks.blockMeta, ModBlocks.blockMeta);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public ItemBlockMeta(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        MetaTileEntity mte = MetaTileEntity.fromMeta(meta);
        return super.getUnlocalizedName(stack) + "."
            + mte.name()
            .toLowerCase();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int meta : MetaTileEntity.getAllBaseMetas()) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);
    }
}
