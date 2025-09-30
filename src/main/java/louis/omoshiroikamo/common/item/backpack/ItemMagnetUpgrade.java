package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemMagnetUpgrade extends ItemUpgrade {

    public static ItemMagnetUpgrade create() {
        ItemMagnetUpgrade item = new ItemMagnetUpgrade();
        item.init();
        return item;
    }

    public ItemMagnetUpgrade() {
        super(ModObject.itemMagnetUpgrade.unlocalisedName);
        setNoRepair();
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibResources.PREFIX_MOD + "magnet_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }
}
