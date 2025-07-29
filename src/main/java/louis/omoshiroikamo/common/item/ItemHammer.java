package louis.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import louis.omoshiroikamo.api.enums.ModObject;

public class ItemHammer extends ItemOK {

    public static ItemHammer create() {
        ItemHammer item = new ItemHammer();
        item.init();
        return item;
    }

    protected ItemHammer() {
        super(ModObject.itemHammer.unlocalisedName);
        setMaxDamage(131);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }
}
