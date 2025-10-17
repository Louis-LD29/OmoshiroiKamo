package ruiseki.omoshiroikamo.common.block.material;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.api.material.MaterialEntry;
import ruiseki.omoshiroikamo.api.material.MaterialRegistry;
import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockMaterial extends ItemBlockWithMetadata {

    public ItemBlockMaterial() {
        super(ModBlocks.blockMaterial, ModBlocks.blockMaterial);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockMaterial(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        MaterialEntry mat = MaterialRegistry.fromMeta(meta);
        String matName = mat.getUnlocalizedName();
        return super.getUnlocalizedName() + "." + matName;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            list.add(new ItemStack(this, 1, meta));
        }
    }

}
