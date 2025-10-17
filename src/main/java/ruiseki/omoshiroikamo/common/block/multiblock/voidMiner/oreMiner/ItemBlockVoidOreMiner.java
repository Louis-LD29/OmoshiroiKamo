package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockVoidOreMiner extends ItemBlockWithMetadata {

    public ItemBlockVoidOreMiner() {
        super(ModBlocks.blockSolarArray, ModBlocks.blockSolarArray);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockVoidOreMiner(Block block) {
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
