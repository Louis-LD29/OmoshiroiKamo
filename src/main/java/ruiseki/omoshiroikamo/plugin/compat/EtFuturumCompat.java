package ruiseki.omoshiroikamo.plugin.compat;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.recipes.SmithingTableRecipes;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.item.ModItems;
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
                new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 3),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'L',
                "itemLeather",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 7),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'L',
                "itemLeather",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                "EFE",
                "MNM",
                "LFL",
                'E',
                ganymedes01.etfuturum.ModItems.ELYTRA.get(),
                'F',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 11),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'L',
                "itemLeather",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Saturation Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierSaturation, 1, 0),
                "MAM",
                "CNC",
                "MAM",
                'A',
                new ItemStack(Items.golden_apple, 1, 0),
                'C',
                ganymedes01.etfuturum.ModItems.CHORUS_FRUIT.get(),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Jump Boost Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierJumpBoost, 1, 0),
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
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Nano Bot Beacon Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 0),
                "GPG",
                "GNG",
                "BCB",
                'P',
                ganymedes01.etfuturum.ModItems.DRAGON_BREATH.get(),
                'G',
                "blockGold",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
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
