package louis.omoshiroikamo.common.recipes;

import static com.enderio.core.common.util.DyeColor.DYE_ORE_NAMES;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.item.ModItems;

public class BlockRecipes {

    public static void init() {

        // Hardened Stone
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockHardenedStone, 1, 0),
                "SCS",
                "CSC",
                "SCS",
                'S',
                "stone",
                'C',
                "cobblestone"));

        // Hardened Stone
        GameRegistry
            .addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockHardenedStone, 1, 0), "stoneHardened"));

        // Basalt
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockBasalt, 1, 0), "stoneBasalt"));

        // Alabaster
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockAlabaster, 1, 0), "stoneAlabaster"));

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

        // Void Ore Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 0),
                "GQG",
                "GLG",
                "ICD",
                'G',
                "blockGold",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                "oreQuartz",
                'I',
                "oreDiamond",
                'D',
                "oreQuartz",
                'C',
                new ItemStack(ModBlocks.blockLaserCore, 1, 0)));

        // Void Ore Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 1),
                "GQG",
                "GLG",
                "QCQ",
                'G',
                "blockDiamond",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockLaserCore, 1, 0)));

        // Void Ore Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 2),
                "EQE",
                "ELE",
                "MQM",
                'E',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 1),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0)));

        // Void Ore Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 3),
                "EQE",
                "ELE",
                "MQM",
                'E',
                "itemNetherStar",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidOreMiner, 1, 2),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0)));

        // Void Res Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 0),
                "GQG",
                "GLG",
                "ICD",
                'G',
                "blockGold",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(Blocks.end_stone, 1, 0),
                'I',
                "stoneMossy",
                'D',
                "netherrack",
                'C',
                new ItemStack(ModBlocks.blockLaserCore, 1, 0)));

        // Void Res Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 1),
                "GQG",
                "GLG",
                "QCQ",
                'G',
                "blockDiamond",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockLaserCore, 1, 0)));

        // Void Res Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 2),
                "EQE",
                "ELE",
                "MQM",
                'E',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 1),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0)));

        // Void Res Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 3),
                "EQE",
                "ELE",
                "MQM",
                'E',
                "itemNetherStar",
                'L',
                new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                'Q',
                new ItemStack(ModBlocks.blockVoidResMiner, 1, 2),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0)));

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
                "stoneBasalt"));

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
                "stoneHardened"));

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
                "stoneAlabaster"));

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

        // Clear Lens
        GameRegistry.addRecipe(
            new ShapedOreRecipe(new ItemStack(ModBlocks.blockLaserLens, 1, 0), "G G", "GGG", "G G", 'G', "blockGlass"));

        // Color Lens
        for (int i = 0; i < DYE_ORE_NAMES.length; i++) {
            GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                    new ItemStack(ModBlocks.blockLaserLens, 1, i + 1),
                    ModBlocks.blockLaserLens,
                    DYE_ORE_NAMES[i]));
            GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                    new ItemStack(ModBlocks.blockLaserLens, 1, 0),
                    new ItemStack(ModBlocks.blockLaserLens, 1, i + 1)));
        }

        // Laser Core
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockLaserCore, 1, 0),
                "GRG",
                "I I",
                "GRG",
                'G',
                "blockGlass",
                'R',
                "dustRedstone",
                'I',
                "ingotIron"));

        // Null Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
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
                new ItemStack(ModBlocks.blockModifierPiezo, 1, 0),
                "RMR",
                "MNM",
                "IMI",
                'R',
                "blockRedstone",
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'I',
                "blockIron"));

        // Speed Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierSpeed, 1, 0),
                "GRG",
                "MNM",
                "GRG",
                'R',
                "blockRedstone",
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'G',
                "blockGold"));

        // Accuracy Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierAccuracy, 1, 0),
                "RMR",
                "MNM",
                "MDM",
                'R',
                "blockRedstone",
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'D',
                "blockDiamond"));

        // Machine Base Basalt
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockMachineBase, 1, 0),
                "RBR",
                "BQB",
                "NBN",
                'R',
                "dustRedstone",
                'B',
                "barsIron",
                'N',
                "nuggetGold",
                'Q',
                "stoneBasalt"));

        // Machine Base Hardened Stone
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockMachineBase, 1, 1),
                "RBR",
                "BQB",
                "NBN",
                'R',
                "dustRedstone",
                'B',
                "barsIron",
                'N',
                "nuggetGold",
                'Q',
                "stoneHardened"));

        // Machine Base Alabaster
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockMachineBase, 1, 2),
                "RBR",
                "BQB",
                "NBN",
                'R',
                "dustRedstone",
                'B',
                "barsIron",
                'N',
                "nuggetGold",
                'Q',
                "stoneAlabaster"));
    }
}
