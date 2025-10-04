package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.item.ItemOK;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemUpgrade extends ItemOK {

    public static ItemUpgrade create() {
        ItemUpgrade item = new ItemUpgrade();
        item.init();
        return item;
    }

    public ItemUpgrade(String name) {
        super(name);
    }

    public ItemUpgrade() {
        this(ModObject.itemUpgrade.unlocalisedName);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibResources.PREFIX_MOD + "upgrade_base");
    }

    public boolean hasTab() {
        return false;
    }
}
