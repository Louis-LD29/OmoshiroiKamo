package louis.omoshiroikamo.common.block.multiblock.structureFrame;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockStructureFrame extends ItemBlockWithMetadata {

    public ItemBlockStructureFrame() {
        super(ModBlocks.blockStructureFrame, ModBlocks.blockStructureFrame);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockStructureFrame(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);

        if (meta >= 0 && meta < BlockStructureFrame.blocks.length) {
            return base + "." + BlockStructureFrame.blocks[meta];
        } else {
            return base;
        }
    }

}
