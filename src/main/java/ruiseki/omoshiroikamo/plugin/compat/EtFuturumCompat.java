package ruiseki.omoshiroikamo.plugin.compat;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.recipes.SmithingTableRecipes;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.init.ModItems;
import ruiseki.omoshiroikamo.common.util.Logger;
import ruiseki.omoshiroikamo.common.util.lib.LibMods;

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

        // Flight Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_FLIGHT.get(),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 3),
                'M',
                ruiseki.omoshiroikamo.common.init.ModBlocks.BLOCK_MICA.get(),
                'L',
                "itemLeather",
                'N',
                ModBlocks.MODIFIER_NULL.get()));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_FLIGHT.get(),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 7),
                'M',
                ruiseki.omoshiroikamo.common.init.ModBlocks.BLOCK_MICA.get(),
                'L',
                "itemLeather",
                'N',
                ModBlocks.MODIFIER_NULL.get()));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_FLIGHT.get(),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 11),
                'M',
                ruiseki.omoshiroikamo.common.init.ModBlocks.BLOCK_MICA.get(),
                'L',
                "itemLeather",
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Saturation Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_SATURATION.get(),
                "MAM",
                "CNC",
                "MAM",
                'A',
                new ItemStack(Items.golden_apple, 1, 0),
                'C',
                ganymedes01.etfuturum.ModItems.CHORUS_FRUIT.get(),
                'M',
                ruiseki.omoshiroikamo.common.init.ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Jump Boost Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_JUMP_BOOST.get(),
                "MRM",
                "SNS",
                "MPM",
                'R',
                ganymedes01.etfuturum.ModItems.RABBIT_FOOT.get(),
                'S',
                ganymedes01.etfuturum.ModBlocks.SLIME.get(),
                'P',
                new ItemStack(Blocks.piston, 1, 0),
                'M',
                ruiseki.omoshiroikamo.common.init.ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Nano Bot Beacon Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 0),
                "GPG",
                "GNG",
                "BCB",
                'P',
                ganymedes01.etfuturum.ModItems.DRAGON_BREATH.get(),
                'G',
                "blockGold",
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'C',
                new ItemStack(Blocks.beacon, 1, 0),
                'B',
                new ItemStack(Blocks.brewing_stand, 1, 0)));

        // SmithingTable

        // Netherite Backpack
        SmithingTableRecipes.getInstance()
            .addRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 5),
                "ingotNetherite",
                new ItemStack(ModItems.itemBackPack, 1, 4));

    }

}
