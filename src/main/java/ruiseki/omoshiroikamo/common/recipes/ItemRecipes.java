package ruiseki.omoshiroikamo.common.recipes;

import static com.enderio.core.common.util.DyeColor.DYE_ORE_NAMES;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.enderio.core.common.util.DyeColor;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.item.ModItems;
import ruiseki.omoshiroikamo.common.util.lib.LibMods;

public class ItemRecipes {

    public static void init() {
        // Hammer
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemHammer, 1, 0),
                "  C",
                "  S",
                "   ",
                'C',
                "cobblestone",
                'S',
                "stickWood"));

        // Starter Backpack
        GameRegistry.addRecipe(
            new NBTShapedOreRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 0),
                "SLS",
                "SCS",
                "LLL",
                'S',
                new ItemStack(Items.string, 1, 1),
                'L',
                "itemLeather",
                'C',
                new ItemStack(Blocks.chest, 1, 1)).withInt("BackpackColor", DyeColor.BROWN.getColor()));

        // Copper Backpack
        GameRegistry.addRecipe(
            new NBTShapedOreRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 1),
                "CCC",
                "CBC",
                "CCC",
                'C',
                "ingotCopper",
                'B',
                new ItemStack(ModItems.itemBackPack, 1, 0)).allowNBTFrom(new ItemStack(ModItems.itemBackPack, 1, 0))
                    .allowAllTags());

        // Iron Backpack
        GameRegistry.addRecipe(
            new NBTShapedOreRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 2),
                "CCC",
                "CBC",
                "CCC",
                'C',
                "ingotIron",
                'B',
                new ItemStack(ModItems.itemBackPack, 1, 1)).allowNBTFrom(new ItemStack(ModItems.itemBackPack, 1, 1))
                    .allowAllTags());

        // Gold Backpack
        GameRegistry.addRecipe(
            new NBTShapedOreRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 3),
                "CCC",
                "CBC",
                "CCC",
                'C',
                "ingotGold",
                'B',
                new ItemStack(ModItems.itemBackPack, 1, 2)).allowNBTFrom(new ItemStack(ModItems.itemBackPack, 1, 2))
                    .allowAllTags());

        // Diamond Backpack
        GameRegistry.addRecipe(
            new NBTShapedOreRecipe(
                new ItemStack(ModItems.itemBackPack, 1, 4),
                "CCC",
                "CBC",
                "CCC",
                'C',
                "gemDiamond",
                'B',
                new ItemStack(ModItems.itemBackPack, 1, 3)).allowNBTFrom(new ItemStack(ModItems.itemBackPack, 1, 3))
                    .allowAllTags());

        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new NBTShapedOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 5),
                    "CSC",
                    "SBS",
                    "CSC",
                    'S',
                    "itemNetherStar",
                    'C',
                    "blockObsidian",
                    'B',
                    new ItemStack(ModItems.itemBackPack, 1, 4)).allowNBTFrom(new ItemStack(ModItems.itemBackPack, 1, 3))
                        .allowAllTags());
        }

        for (int i = 0; i < DYE_ORE_NAMES.length; i++) {
            String dyeOreName = DYE_ORE_NAMES[i];
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 0),
                    new ItemStack(ModItems.itemBackPack, 1, 0),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 1),
                    new ItemStack(ModItems.itemBackPack, 1, 1),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 2),
                    new ItemStack(ModItems.itemBackPack, 1, 2),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 3),
                    new ItemStack(ModItems.itemBackPack, 1, 3),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 4),
                    new ItemStack(ModItems.itemBackPack, 1, 4),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
            GameRegistry.addRecipe(
                new NBTShapelessOreRecipe(
                    new ItemStack(ModItems.itemBackPack, 1, 5),
                    new ItemStack(ModItems.itemBackPack, 1, 5),
                    dyeOreName).withInt(
                        "BackpackColor",
                        DyeColor.fromIndex(i)
                            .getColor()));
        }

        // Upgrade Base
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemUpgrade, 1, 0),
                "SIS",
                "ILI",
                "SIS",
                'S',
                new ItemStack(Items.string, 1, 0),
                'I',
                "ingotIron",
                'L',
                new ItemStack(Items.leather, 1, 0)));

        // Stack Upgrade Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemStackUpgrade, 1, 0),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockIron",
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // Stack Upgrade Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemStackUpgrade, 1, 1),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockGold",
                'U',
                new ItemStack(ModItems.itemStackUpgrade, 1, 0)));

        // Stack Upgrade Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemStackUpgrade, 1, 2),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockDiamond",
                'U',
                new ItemStack(ModItems.itemStackUpgrade, 1, 1)));

        // Stack Upgrade Tier 4
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModItems.itemStackUpgrade, 1, 3),
                    "BBB",
                    "BUB",
                    "BBB",
                    'B',
                    "itemNetherStar",
                    'U',
                    new ItemStack(ModItems.itemStackUpgrade, 1, 2)));
        }

        // Crafting Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemCraftingUpgrade, 1, 0),
                " c ",
                "IUI",
                " C ",
                'c',
                new ItemStack(Blocks.crafting_table, 1, 0),
                'C',
                new ItemStack(Blocks.chest, 1, 0),
                'I',
                "ingotIron",
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // Magnet Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemMagnetUpgrade, 1, 0),
                "EIE",
                "IUI",
                "R L",
                'E',
                "pearlEnder",
                'R',
                "dustRedstone",
                'L',
                "gemLapis",
                'I',
                "ingotIron",
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // Feeding Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemFeedingUpgrade, 1, 0),
                " C ",
                "AUM",
                " E ",
                'E',
                "pearlEnder",
                'C',
                new ItemStack(Items.golden_carrot, 1, 0),
                'A',
                new ItemStack(Items.golden_apple, 1, 0),
                'M',
                new ItemStack(Items.speckled_melon, 1, 0),
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // Battery Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemBatteryUpgrade, 1, 0),
                "GRG",
                "RUR",
                "GRG",
                'G',
                "ingotGold",
                'R',
                "blockRedstone",
                'U',
                new ItemStack(ModItems.itemUpgrade, 1, 0)));

        // Everlasting Upgrade
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModItems.itemEverlastingUpgrade, 1, 0),
                    "GRG",
                    "RUR",
                    "GRG",
                    'G',
                    "itemGhastTear",
                    'R',
                    "itemNetherStar",
                    'U',
                    new ItemStack(ModItems.itemUpgrade, 1, 0)));
        }

        // Photovoltaic Cell
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemPhotovoltaicCell, 1, 0),
                " L ",
                "LQL",
                " L ",
                'L',
                "gemLapis",
                'Q',
                "gemQuartz"));

        // Stabilized Ender Pear
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                " P ",
                "PIP",
                " P ",
                'P',
                "pearlEnder",
                'I',
                "blockIron"));

        // Assembler
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemAssembler, 1, 0),
                "  B",
                " O ",
                "O  ",
                'O',
                "blockObsidian",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 1)));

        // Assembler
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemAssembler, 1, 0),
                "  B",
                " O ",
                "O  ",
                'O',
                "blockObsidian",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 5)));

        // Assembler
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModItems.itemAssembler, 1, 0),
                "  B",
                " O ",
                "O  ",
                'O',
                "blockObsidian",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 9)));

    }

}
