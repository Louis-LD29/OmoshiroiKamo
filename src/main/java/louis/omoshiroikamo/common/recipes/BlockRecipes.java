package louis.omoshiroikamo.common.recipes;

import static com.enderio.core.common.util.DyeColor.DYE_ORE_NAMES;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.util.lib.LibMods;

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

        // Nano Bot Beacon Tier 1
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 0),
                    "GPG",
                    "GNG",
                    "BCB",
                    'P',
                    Items.potionitem,
                    'G',
                    "blockGold",
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                    'C',
                    new ItemStack(Blocks.beacon, 1, 0),
                    'B',
                    new ItemStack(Blocks.brewing_stand, 1, 0)));
        }

        // Nano Bot Beacon Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 1),
                "GCG",
                "GNG",
                "BCB",
                'G',
                "blockDiamond",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 0),
                'B',
                new ItemStack(Blocks.brewing_stand, 1, 0)));

        // Nano Bot Beacon Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 2),
                "GCG",
                "GNG",
                "BCB",
                'G',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 1),
                'B',
                new ItemStack(ModBlocks.blockMica, 1, 0)));

        // Nano Bot Beacon Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 3),
                "GCG",
                "GNG",
                "BCB",
                'G',
                "itemNetherStar",
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0),
                'C',
                new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 2),
                'B',
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

        // Flight Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.blockMica, 1, 0),
                    'F',
                    new ItemStack(ModBlocks.blockStructureFrame, 1, 3),
                    'L',
                    "itemLeather",
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.blockMica, 1, 0),
                    'F',
                    new ItemStack(ModBlocks.blockStructureFrame, 1, 7),
                    'L',
                    "itemLeather",
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.blockMica, 1, 0),
                    'F',
                    new ItemStack(ModBlocks.blockStructureFrame, 1, 11),
                    'L',
                    "itemLeather",
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
        }

        // Night Vision Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierNightVision, 1, 0),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8198),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Strength Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierStrength, 1, 0),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8201),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Water Breathing Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierWaterBreathing, 1, 0),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8205),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Regeneration Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierRegeneration, 1, 0),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8193),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Fire Resistance Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierFireResistance, 1, 0),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8195),
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Jump Boost Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockModifierJumpBoost, 1, 0),
                    "MRM",
                    "PNP",
                    "MRM",
                    'P',
                    "slimeball",
                    'R',
                    new ItemStack(Blocks.piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.blockMica, 1, 0),
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
        }

        // Resistance Modifier
        ItemStack prot4Book = new ItemStack(Items.enchanted_book);
        prot4Book.addEnchantment(Enchantment.protection, 4);
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierResistance, 1, 0),
                "MPM",
                "ONO",
                "MPM",
                'O',
                "blockObsidian",
                'P',
                prot4Book,
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Haste Modifier
        ItemStack eff5Book = new ItemStack(Items.enchanted_book);
        eff5Book.addEnchantment(Enchantment.efficiency, 5);
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(ModBlocks.blockModifierHaste, 1, 0),
                "MPM",
                "RNR",
                "MGM",
                'R',
                "blockRedstone",
                'G',
                "ingotGold",
                'P',
                eff5Book,
                'M',
                new ItemStack(ModBlocks.blockMica, 1, 0),
                'N',
                new ItemStack(ModBlocks.blockModifierNull, 1, 0)));

        // Saturation Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(ModBlocks.blockModifierSaturation, 1, 0),
                    "MAM",
                    "CNC",
                    "MAM",
                    'A',
                    new ItemStack(Items.golden_apple, 1, 0),
                    'C',
                    new ItemStack(Items.golden_carrot, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.blockMica, 1, 0),
                    'N',
                    new ItemStack(ModBlocks.blockModifierNull, 1, 0)));
        }

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
