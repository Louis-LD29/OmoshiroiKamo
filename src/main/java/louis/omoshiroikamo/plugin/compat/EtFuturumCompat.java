package louis.omoshiroikamo.plugin.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.recipes.SmithingTableRecipes;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.util.lib.LibMods;

public class EtFuturumCompat {

    public static void init() {
        if (!LibMods.EtFuturum.isLoaded()) {
            return;
        }

        addRecipes();

        Logger.info("Loaded EtFuturumCompat");
    }

    public static void addRecipes() {
        Logger.info("Loaded EtFuturum Compat Recipes");

        // Crafting

        // Stack Upgrade Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemStackUpgrade, 1, 3),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockNetherite",
                'U',
                new ItemStack(ModItems.itemStackUpgrade, 1, 2)));

        // Everlasting Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemEverlastingUpgrade, 1, 0),
                "GRG",
                "RUR",
                "GRG",
                'G',
                ganymedes01.etfuturum.ModItems.END_CRYSTAL.get(),
                'R',
                "itemNetherStar",
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // SmithingTable

        // Netherite Backpack
        SmithingTableRecipes.getInstance()
            .addRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 5),
                "ingotNetherite",
                new ItemStack(ModItems.itemBackPack, 1, 4));

    }

}
