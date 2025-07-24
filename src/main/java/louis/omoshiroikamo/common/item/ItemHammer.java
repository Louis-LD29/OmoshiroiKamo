package louis.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.OKCreativeTab;

public class ItemHammer extends Item {

    public static ItemHammer create() {
        ItemHammer item = new ItemHammer();
        item.init();
        return item;
    }

    protected ItemHammer() {
        setMaxDamage(131);
        setCreativeTab(OKCreativeTab.INSTANCE);
        setUnlocalizedName(ModObject.itemHammer.unlocalisedName);
    }

    private void init() {
        GameRegistry.registerItem(this, ModObject.itemHammer.unlocalisedName);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }
}
