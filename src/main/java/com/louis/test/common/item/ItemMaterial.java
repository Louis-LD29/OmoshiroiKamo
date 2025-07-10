package com.louis.test.common.item;

import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.core.lib.LibResources;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.capitalize;

public class ItemMaterial extends Item {

    protected IIcon ingotIcon;
    protected IIcon nuggetIcon;
    protected IIcon plateIcon;

    public static ItemMaterial create() {
        ItemMaterial mat = new ItemMaterial();
        mat.init();
        return mat;
    }

    protected ItemMaterial() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(TestCreativeTab.INSTANCE);
        setUnlocalizedName(ModObject.itemMaterial.unlocalisedName);
    }

    private void init() {
        GameRegistry.registerItem(this, ModObject.itemMaterial.unlocalisedName);

        for (Material mat : Material.values()) {
            String matName = mat.name().toLowerCase(Locale.ROOT);
            int index = mat.ordinal();

            registerMaterialOreDict("ingot", matName, index);
            registerMaterialOreDict("nugget", matName, 100 + index);
            registerMaterialOreDict("plate", matName, 200 + index);

            // Special aliases
            switch (mat) {
                case CARBON_STEEL:
                    registerMaterialOreDict("ingot", "steel", index);
                    registerMaterialOreDict("nugget", "steel", 100 + index);
                    registerMaterialOreDict("plate", "steel", 200 + index);
                    break;
                default:
                    break;
            }
        }
    }


    private void registerMaterialOreDict(String type, String name, int meta) {
        OreDictionary.registerOre(type + capitalize(name), new ItemStack(this, 1, meta));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        int baseMeta = meta % 100;
        Material material = Material.fromMeta(baseMeta);
        String base = super.getUnlocalizedName(stack);

        String type;
        if (meta >= 200) {
            type = "plate";
        } else if (meta >= 100) {
            type = "nugget";
        } else {
            type = "ingot";
        }

        String mat = material.name().toLowerCase(Locale.ROOT);
        return base + "." + type + "." + mat;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        int count = Material.values().length;
        for (int i = 0; i < count; i++) {
            // ingot
            list.add(new ItemStack(this, 1, i));
            // nugget
            list.add(new ItemStack(this, 1, 100 + i));
            // plate
            list.add(new ItemStack(this, 1, 200 + i));
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage >= 200) return plateIcon;
        if (damage >= 100) return nuggetIcon;
        return ingotIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        Material mat = Material.fromMeta(stack.getItemDamage() % 100);
        return mat.getColor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg) {
        ingotIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_ingot");
        nuggetIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_nugget");
        plateIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_plate");
    }
}
