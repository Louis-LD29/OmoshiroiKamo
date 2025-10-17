package ruiseki.omoshiroikamo.common.block.material.machineBase;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockMachineBase extends ItemBlockWithMetadata {

    public ItemBlockMachineBase() {
        super(ModBlocks.blockMachineBase, ModBlocks.blockMachineBase);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockMachineBase(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);

        if (meta >= 0 && meta < BlockMachineBase.blocks.length) {
            return base + "." + BlockMachineBase.blocks[meta];
        } else {
            return base;
        }
    }

}
