package com.louis.test.common.block.material;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.api.MaterialEntry;
import com.louis.test.api.MaterialRegistry;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockMaterial extends ItemBlockWithMetadata {

    public ItemBlockMaterial() {
        super(ModBlocks.blockMeta, ModBlocks.blockMeta);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public ItemBlockMaterial(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        MaterialEntry mat = MaterialRegistry.fromMeta(meta);
        String matName = mat.name.toLowerCase(Locale.ROOT)
            .replace(' ', '_');
        return super.getUnlocalizedName() + "." + matName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        int count = MaterialRegistry.all()
            .size();
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

}
