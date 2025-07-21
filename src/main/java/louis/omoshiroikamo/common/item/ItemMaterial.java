package louis.omoshiroikamo.common.item;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.core.helper.OreDictUtils;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class ItemMaterial extends Item {

    protected IIcon ingotIcon;
    protected IIcon nuggetIcon;
    protected IIcon plateIcon;
    protected IIcon rodIcon;
    protected IIcon dustIcon;
    protected IIcon gearIcon;

    public static ItemMaterial create() {
        ItemMaterial mat = new ItemMaterial();
        mat.init();
        return mat;
    }

    protected ItemMaterial() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(OKCreativeTab.INSTANCE);
        setUnlocalizedName(ModObject.itemMaterial.unlocalisedName);
    }

    private void init() {
        GameRegistry.registerItem(this, ModObject.itemMaterial.unlocalisedName);

        for (MaterialEntry entry : MaterialRegistry.all()) {
            String matName = entry.getUnlocalizedName();
            int meta = entry.meta;

            registerMaterialOreDict(matName, meta);
            registerMaterialConversionRecipes(matName, meta);

            if ("Carbon Steel".equalsIgnoreCase(entry.getName())) {
                registerMaterialOreDict("Steel", meta);
                registerMaterialConversionRecipes("Steel", meta);
            }

        }
    }

    private void registerMaterialOreDict(String name, int meta) {
        OreDictionary.registerOre("ingot" + capitalize(name), new ItemStack(this,  1, LibResources.BASE +meta));
        OreDictionary.registerOre("nugget" + capitalize(name), new ItemStack(this, 1, LibResources.META1 + meta));
        OreDictionary.registerOre("plate" + capitalize(name), new ItemStack(this, 1, LibResources.META2 + meta));
        OreDictionary.registerOre("rod" + capitalize(name), new ItemStack(this, 1, LibResources.META3 + meta));
        OreDictionary.registerOre(uncapitalize(name) + "Rod", new ItemStack(this, 1, LibResources.META3 + meta));
        OreDictionary.registerOre("stick" + capitalize(name), new ItemStack(this, 1, LibResources.META3 + meta));
        OreDictionary.registerOre("dust" + capitalize(name), new ItemStack(this, 1, LibResources.META4 + meta));
        OreDictionary.registerOre("gear" + capitalize(name), new ItemStack(this, 1, LibResources.META5 + meta));
    }

    private void registerMaterialConversionRecipes(String oreBaseName, int meta) {
        ItemStack ingot = new ItemStack(this, 1, meta);
        ItemStack nugget = new ItemStack(this, 1, LibResources.META1 + meta);
        ItemStack nugget9 = nugget.copy();
        nugget9.stackSize = 9;
        ItemStack plate = new ItemStack(this, 1, LibResources.META2 + meta);
        ItemStack rod = new ItemStack(this, 1, LibResources.META3 + meta);
        ItemStack dust = new ItemStack(this, 1, LibResources.META4 + meta);
        ItemStack gear = new ItemStack(this, 1, LibResources.META5 + meta);
        ItemStack block = new ItemStack(ModBlocks.blockMaterial, 1, meta);

        String ingotOre = "ingot" + capitalize(oreBaseName);
        String nuggetOre = "nugget" + capitalize(oreBaseName);
        String plateOre = "plate" + capitalize(oreBaseName);
        String rodOre = "rod" + capitalize(oreBaseName);
        String[] altRodOres = { uncapitalize(oreBaseName) + "Rod", "stick" + capitalize(oreBaseName) };
        String dustOre = "dust" + capitalize(oreBaseName);
        String gearOre = "gear" + capitalize(oreBaseName);
        String blockOre = "block" + capitalize(oreBaseName);

        GameRegistry.addRecipe(new ShapedOreRecipe(ingot, "NNN", "NNN", "NNN", 'N', nugget));
        GameRegistry.addRecipe(new ShapedOreRecipe(block, "III", "III", "III", 'I', ingot));
        GameRegistry.addRecipe(new ShapelessOreRecipe(nugget9, ingot));

        OreDictUtils.registerOreDictConversionToOreDict(ingot, ingotOre);
        OreDictUtils.registerOreDictConversionToOreDict(nugget, nuggetOre);
        OreDictUtils.registerOreDictConversionToOreDict(plate, plateOre);
        OreDictUtils.registerOreDictConversionToOreDict(rod, rodOre);
        for (String altRod : altRodOres) {
            OreDictUtils.registerOreDictConversionToOreDict(rod, altRod);
        }
        OreDictUtils.registerOreDictConversionToOreDict(dust, dustOre);
        OreDictUtils.registerOreDictConversionToOreDict(gear, gearOre);
        OreDictUtils.registerOreDictConversionToOreDict(block, blockOre);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        int baseMeta = meta % LibResources.META1;
        MaterialEntry material = MaterialRegistry.fromMeta(baseMeta);
        String base = super.getUnlocalizedName(stack);

        String type;
        if (meta >= LibResources.META5) {
            type = "gear";
        } else if (meta >= LibResources.META4) {
            type = "dust";
        } else if (meta >= LibResources.META3) {
            type = "rod";
        } else if (meta >= LibResources.META2) {
            type = "plate";
        } else if (meta >= LibResources.META1) {
            type = "nugget";
        } else {
            type = "ingot";
        }

        String mat = material.getUnlocalizedName();
        return base + "." + type + "." + mat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            // ingot
            list.add(new ItemStack(this, 1, meta));
            // nugget
            list.add(new ItemStack(this, 1, LibResources.META1 + meta));
            // plate
            list.add(new ItemStack(this, 1, LibResources.META2 + meta));
            // rod
            list.add(new ItemStack(this, 1, LibResources.META3 + meta));
            // dust
            list.add(new ItemStack(this, 1, LibResources.META4 + meta));
            // gear
            list.add(new ItemStack(this, 1, LibResources.META5 + meta));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage >= LibResources.META5) return gearIcon;
        if (damage >= LibResources.META4) return dustIcon;
        if (damage >= LibResources.META3) return rodIcon;
        if (damage >= LibResources.META2) return plateIcon;
        if (damage >= LibResources.META1) return nuggetIcon;
        return ingotIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        int meta = stack.getItemDamage() % LibResources.META1;
        MaterialEntry mat = MaterialRegistry.fromMeta(meta);
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
        gearIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_gear");
    }

}
