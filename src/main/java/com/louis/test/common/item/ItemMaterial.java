package com.louis.test.common.item;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;
import com.louis.test.core.lib.LibResources;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMaterial extends Item {

    protected IIcon ingotIcon;
    protected IIcon nuggetIcon;
    protected IIcon plateIcon;
    protected IIcon rodIcon;
    protected IIcon dustIcon;

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
            String matName = mat.name()
                .toLowerCase(Locale.ROOT);
            int index = mat.ordinal();

            registerMaterialOreDict(matName, index);

            switch (mat) {
                case CARBON_STEEL:
                    registerMaterialOreDict("Steel", index);
                    break;
                default:
                    break;
            }

            registerMaterialConversionRecipes(index);
        }
    }

    private void registerMaterialOreDict(String name, int meta) {
        OreDictionary.registerOre("ingot" + capitalize(name), new ItemStack(this, 1, meta));
        OreDictionary.registerOre("nugget" + capitalize(name), new ItemStack(this, 1, 100 + meta));
        OreDictionary.registerOre("plate" + capitalize(name), new ItemStack(this, 1, 200 + meta));
        OreDictionary.registerOre("rod" + capitalize(name), new ItemStack(this, 1, 300 + meta));
        OreDictionary.registerOre(uncapitalize(name) + "Rod", new ItemStack(this, 1, 300 + meta));
        OreDictionary.registerOre("stick" + capitalize(name), new ItemStack(this, 1, 300 + meta));
        OreDictionary.registerOre("dust" + capitalize(name), new ItemStack(this, 1, 400 + meta));
    }

    private void registerMaterialConversionRecipes(int meta) {
        ItemStack ingot = new ItemStack(this, 1, meta);
        ItemStack nugget = new ItemStack(this, 1, 100 + meta);
        ItemStack block = new ItemStack(Item.getItemFromBlock(ModBlocks.blockMaterial), 1, meta);

        // Ingot → Nugget x9
        GameRegistry.addShapelessRecipe(new ItemStack(nugget.getItem(), 9, nugget.getItemDamage()), ingot);

        // Nugget x9 → Ingot
        GameRegistry.addShapedRecipe(ingot, "NNN", "NNN", "NNN", 'N', nugget);

        // Ingot x9 → Block
        GameRegistry.addShapedRecipe(block, "III", "III", "III", 'I', ingot);

        // Block → Ingot x9
        GameRegistry.addShapelessRecipe(new ItemStack(ingot.getItem(), 9, ingot.getItemDamage()), block);

    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        int baseMeta = meta % 100;
        Material material = Material.fromMeta(baseMeta);
        String base = super.getUnlocalizedName(stack);

        String type;
        if (meta >= 400) {
            type = "dust";
        } else if (meta >= 300) {
            type = "rod";
        } else if (meta >= 200) {
            type = "plate";
        } else if (meta >= 100) {
            type = "nugget";
        } else {
            type = "ingot";
        }

        String mat = material.name()
            .toLowerCase(Locale.ROOT);
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
            // rod
            list.add(new ItemStack(this, 1, 300 + i));
            // dust
            list.add(new ItemStack(this, 1, 400 + i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage >= 400) return dustIcon;
        if (damage >= 300) return rodIcon;
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
        rodIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_rod");
        dustIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_dust");
    }
}
