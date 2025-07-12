package com.louis.test.common.item;

import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.core.helper.IconHelper;
import com.louis.test.common.core.lib.LibMisc;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMod extends Item {

    public ItemMod() {
        super();
        setCreativeTab(TestCreativeTab.INSTANCE);
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
