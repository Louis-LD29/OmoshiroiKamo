package louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockNanoBotBeacon extends ItemBlockWithMetadata {

    public ItemBlockNanoBotBeacon() {
        super(ModBlocks.blockNanoBotBeacon, ModBlocks.blockNanoBotBeacon);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockNanoBotBeacon(Block block) {
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
