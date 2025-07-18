package louis.omoshiroikamo.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.core.helper.IconHelper;
import louis.omoshiroikamo.common.core.lib.LibMisc;

public class ItemMod extends Item {

    public ItemMod() {
        super();
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemMod(String name) {
        super();
        setUnlocalizedName(name);
    }

    @Override
    public Item setUnlocalizedName(String s) {
        GameRegistry.registerItem(this, s);
        return super.setUnlocalizedName(s);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
        return super.getUnlocalizedNameInefficiently(par1ItemStack)
            .replaceAll("item\\.", "item." + LibMisc.MOD_ID + ":");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        itemIcon = IconHelper.forItem(par1IconRegister, this);
    }
}
