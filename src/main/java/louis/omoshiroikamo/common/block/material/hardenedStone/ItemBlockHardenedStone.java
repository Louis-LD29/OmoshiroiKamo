package louis.omoshiroikamo.common.block.material.hardenedStone;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockHardenedStone extends ItemBlockWithMetadata {

    public ItemBlockHardenedStone() {
        super(ModBlocks.blockBasalt, ModBlocks.blockBasalt);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockHardenedStone(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);

        if (meta >= 0 && meta < BlockHardenedStone.blocks.length) {
            return base + "." + BlockHardenedStone.blocks[meta];
        } else {
            return base;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < BlockHardenedStone.blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

}
