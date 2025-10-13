package louis.omoshiroikamo.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.item.ModItems;

public class BlockRecipes {

    public static void init() {

        // Solar Cell
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockSolarCell, 1, 0),
                "GGG",
                "CCC",
                "RIR",
                'G',
                "blockGlass",
                'I',
                "ingotIron",
                'R',
                "dustRedstone",
                'C',
                new ItemStack(ModItems.itemPhotovoltaicCell, 1, 0)));

        // Solar Array Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockSolarArray, 1, 0),
                "GLG",
                "LCL",
                "GLG",
                'G',
                "blockGold",
                'L',
                "blockLapis",
                'C',
                new ItemStack(ModBlocks.blockSolarCell, 1, 0)));

        // Solar Array Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockSolarArray, 1, 1),
                "GAG",
                "LCL",
                "GAG",
                'G',
                "blockDiamond",
                'L',
                "blockLapis",
                'A',
                new ItemStack(ModBlocks.blockSolarArray, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockSolarCell, 1, 0)));

        // Solar Array Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockSolarArray, 1, 2),
                "GAG",
                "LCL",
                "GAG",
                'G',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'A',
                new ItemStack(ModBlocks.blockSolarArray, 1, 1),
                'C',
                new ItemStack(ModBlocks.blockSolarCell, 1, 0)));

        // Solar Array Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockSolarArray, 1, 3),
                "GAG",
                "LCL",
                "GAG",
                'G',
                "itemNetherStar",
                'L',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'A',
                new ItemStack(ModBlocks.blockSolarArray, 1, 2),
                'C',
                new ItemStack(ModBlocks.blockSolarCell, 1, 0)));

        // Basalt Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 0),
                " G ",
                "RBR",
                " G ",
                'G',
                "ingotGold",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockBasalt, 1, 0)));

        // Basalt Structure Frame Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 1),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 0)));

        // Basalt Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 2),
                " E ",
                "RBR",
                " G ",
                'E',
                "pearlEnder",
                'G',
                "dustGlowstone",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 1)));

        // Basalt Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 3),
                " E ",
                "RBR",
                " G ",
                'E',
                "itemNetherStar",
                'G',
                "dustRedstone",
                'R',
                "dustGlowstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 2)));

        // Hardened Stone Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 4),
                " G ",
                "RBR",
                " G ",
                'G',
                "ingotGold",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockHardenedStone, 1, 0)));

        // Hardened Stone Structure Frame Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 5),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 4)));

        // Hardened Stone Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 6),
                " E ",
                "RBR",
                " G ",
                'E',
                "pearlEnder",
                'G',
                "dustGlowstone",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 5)));

        // Hardened Stone Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 7),
                " E ",
                "RBR",
                " G ",
                'E',
                "itemNetherStar",
                'G',
                "dustRedstone",
                'R',
                "dustGlowstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 6)));

        // Alabaster Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 8),
                " G ",
                "RBR",
                " G ",
                'G',
                "ingotGold",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockAlabaster, 1, 0)));

        // Alabaster Structure Frame Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 9),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 8)));

        // Alabaster Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 10),
                " E ",
                "RBR",
                " G ",
                'E',
                "pearlEnder",
                'G',
                "dustGlowstone",
                'R',
                "dustRedstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 9)));

        // Alabaster Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockStructureFrame, 1, 11),
                " E ",
                "RBR",
                " G ",
                'E',
                "itemNetherStar",
                'G',
                "dustRedstone",
                'R',
                "dustGlowstone",
                'B',
                new ItemStack(ModBlocks.blockStructureFrame, 1, 10)));

        // Null Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifier, 1, 0),
                "SGS",
                "GIG",
                "SGS",
                'S',
                "stone",
                'G',
                "blockGlass",
                'I',
                "blockIron"));

        // Piezo Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifier, 1, 1),
                "RMR",
                "MNM",
                "IMI",
                'R',
                "blockRedstone",
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifier, 1, 0),
                'I',
                "blockIron"));
    }
}
