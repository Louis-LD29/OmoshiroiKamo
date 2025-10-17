package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.lens;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockLaserLens extends ItemBlockWithMetadata {

    public ItemBlockLaserLens() {
        super(ModBlocks.blockLaserLens, ModBlocks.blockLaserLens);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockLaserLens(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);

        if (meta >= 0 && meta < BlockLaserLens.blocks.length) {
            return base + "." + BlockLaserLens.blocks[meta];
        } else {
            return base;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < BlockLaserLens.blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

}
