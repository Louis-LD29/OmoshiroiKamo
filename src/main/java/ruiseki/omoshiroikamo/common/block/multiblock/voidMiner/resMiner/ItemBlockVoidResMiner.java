package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockVoidResMiner extends ItemBlockWithMetadata {

    public ItemBlockVoidResMiner() {
        super(ModBlocks.blockSolarArray, ModBlocks.blockSolarArray);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockVoidResMiner(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int tier = stack.getItemDamage() + 1;
        return super.getUnlocalizedName() + ".tier_" + tier;
    }

}
