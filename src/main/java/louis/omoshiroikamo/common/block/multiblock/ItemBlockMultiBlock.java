package louis.omoshiroikamo.common.block.multiblock;

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

public class ItemBlockMultiBlock extends ItemBlockWithMetadata {

    public ItemBlockMultiBlock() {
        super(ModBlocks.blockMultiBlock, ModBlocks.blockMultiBlock);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockMultiBlock(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
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
