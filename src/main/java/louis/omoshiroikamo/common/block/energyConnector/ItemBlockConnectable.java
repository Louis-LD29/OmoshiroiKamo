package louis.omoshiroikamo.common.block.energyConnector;

import java.util.List;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;

public class ItemBlockConnectable extends ItemBlockWithMetadata implements IAdvancedTooltipProvider {

    public ItemBlockConnectable() {
        super(ModBlocks.blockEnergyInOut, ModBlocks.blockEnergyInOut);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockConnectable(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);

        if (meta >= 0 && meta < BlockConnectable.blocks.length) {
            return base + "." + BlockConnectable.blocks[meta];
        } else {
            return base;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < BlockConnectable.blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer player, List<String> list, boolean flag) {
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
    }
}
