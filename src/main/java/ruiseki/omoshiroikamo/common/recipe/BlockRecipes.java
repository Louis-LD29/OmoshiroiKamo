package ruiseki.omoshiroikamo.common.recipe;

import static com.enderio.core.common.util.DyeColor.DYE_ORE_NAMES;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.init.ModItems;
import ruiseki.omoshiroikamo.common.util.lib.LibMods;

public class BlockRecipes {

    public static void init() {

        // Hardened Stone
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.BLOCK_HARDENED_STONE.get(),
                "SCS",
                "CSC",
                "SCS",
                'S',
                "stone",
                'C',
                "cobblestone"));

        // Hardened Stone
        GameRegistry.addRecipe(new ShapelessOreRecipe(ModBlocks.BLOCK_HARDENED_STONE.get(), "stoneHardened"));

        // Basalt
        GameRegistry.addRecipe(new ShapelessOreRecipe(ModBlocks.BLOCK_BASALT.get(), "stoneBasalt"));

        // Alabaster
        GameRegistry.addRecipe(new ShapelessOreRecipe(ModBlocks.BLOCK_ALABASTER.get(), "stoneAlabaster"));

        // Solar Cell
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.SOLAR_CELL.get(),
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
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 0),
                "GLG",
                "LCL",
                "GLG",
                'G',
                "blockGold",
                'L',
                "blockLapis",
                'C',
                ModBlocks.SOLAR_CELL.get()));

        // Solar Array Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 1),
                "GAG",
                "LCL",
                "GAG",
                'G',
                "blockDiamond",
                'L',
                "blockLapis",
                'A',
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 0),
                'C',
                ModBlocks.SOLAR_CELL.get()));

        // Solar Array Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 2),
                "GAG",
                "LCL",
                "GAG",
                'G',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',
                ModBlocks.BLOCK_MICA.get(),
                'A',
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 1),
                'C',
                ModBlocks.SOLAR_CELL.get()));

        // Solar Array Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 3),
                "GAG",
                "LCL",
                "GAG",
                'G',
                "itemNetherStar",
                'L',
                ModBlocks.BLOCK_MICA.get(),
                'A',
                ModBlocks.SOLAR_ARRAY.newItemStack(1, 2),
                'C',
                ModBlocks.SOLAR_CELL.get()));

        // Void Ore Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 0),
                "GQG",
                "GLG",
                "ICD",
                'G',
                "blockGold",
                'L',
                ModBlocks.LASER_LENS.get(),
                'Q',
                "oreQuartz",
                'I',
                "oreDiamond",
                'D',
                "oreQuartz",
                'C',
                ModBlocks.LASER_CORE.get()));

        // Void Ore Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 1),
                "GQG",
                "GLG",
                "QCQ",
                'G',
                "blockDiamond",
                'L',
                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 0),
                'C',
                ModBlocks.LASER_CORE.get()));

        // Void Ore Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 2),
                "EQE",
                "ELE",
                "MQM",
                'E',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',
                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 1),
                'M',
                ModBlocks.BLOCK_MICA.get()));

        // Void Ore Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 3),
                "EQE",
                "ELE",
                "MQM",
                'E',
                "itemNetherStar",
                'L',
                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_ORE_MINER.newItemStack(1, 2),
                'M',
                ModBlocks.BLOCK_MICA.get()));

        // Void Res Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_RES_MINER.newItemStack(1, 0),
                "GQG",
                "GLG",
                "ICD",
                'G',
                "blockGold",
                'L',

                ModBlocks.LASER_LENS.get(),
                'Q',
                new ItemStack(Blocks.end_stone, 1, 0),
                'I',
                "stoneMossy",
                'D',
                "netherrack",
                'C',
                ModBlocks.LASER_CORE.get()));

        // Void Res Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_RES_MINER.newItemStack(1, 1),
                "GQG",
                "GLG",
                "QCQ",
                'G',
                "blockDiamond",
                'L',

                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_RES_MINER.newItemStack(1, 0),
                'C',
                ModBlocks.LASER_CORE.get()));

        // Void Res Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_RES_MINER.newItemStack(1, 2),
                "EQE",
                "ELE",
                "MQM",
                'E',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'L',

                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_RES_MINER.newItemStack(1, 1),
                'M',
                ModBlocks.BLOCK_MICA.get()));

        // Void Res Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.VOID_RES_MINER.newItemStack(1, 3),
                "EQE",
                "ELE",
                "MQM",
                'E',
                "itemNetherStar",
                'L',

                ModBlocks.LASER_LENS.get(),
                'Q',
                ModBlocks.VOID_RES_MINER.newItemStack(1, 2),
                'M',
                ModBlocks.BLOCK_MICA.get()));

        // Nano Bot Beacon Tier 1
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.NANO_BOT_BEACON.newItemStack(1, 0),
                    "GPG",
                    "GNG",
                    "BCB",
                    'P',
                    Items.potionitem,
                    'G',
                    "blockGold",
                    'N',
                    ModBlocks.MODIFIER_NULL.get(),
                    'C',
                    new ItemStack(Blocks.beacon, 1, 0),
                    'B',
                    new ItemStack(Blocks.brewing_stand, 1, 0)));
        }

        // Nano Bot Beacon Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 1),
                "GCG",
                "GNG",
                "BCB",
                'G',
                "blockDiamond",
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'C',
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 0),
                'B',
                new ItemStack(Blocks.brewing_stand, 1, 0)));

        // Nano Bot Beacon Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 2),
                "GCG",
                "GNG",
                "BCB",
                'G',
                new ItemStack(ModItems.itemStabilizedEnderPear, 1, 0),
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'C',
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 1),
                'B',
                ModBlocks.BLOCK_MICA.get()));

        // Nano Bot Beacon Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 3),
                "GCG",
                "GNG",
                "BCB",
                'G',
                "itemNetherStar",
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'C',
                ModBlocks.NANO_BOT_BEACON.newItemStack(1, 2),
                'B',
                ModBlocks.BLOCK_MICA.get()));

        // Basalt Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 0),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 1),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 0)));

        // Basalt Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 2),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 1)));

        // Basalt Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 3),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 2)));

        // Hardened Stone Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 4),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 5),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 4)));

        // Hardened Stone Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 6),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 5)));

        // Hardened Stone Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 7),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 6)));

        // Alabaster Structure Frame Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 8),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 9),
                " G ",
                "RBR",
                " G ",
                'G',
                "gemDiamond",
                'R',
                "dustRedstone",
                'B',
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 8)));

        // Alabaster Structure Frame Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 10),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 9)));

        // Alabaster Structure Frame Tier 4
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 11),
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
                ModBlocks.STRUCTURE_FRAME.newItemStack(1, 10)));

        // Clear Lens
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.LASER_LENS.get(), "G G", "GGG", "G G", 'G', "blockGlass"));

        // Color Lens
        for (int i = 0; i < DYE_ORE_NAMES.length; i++) {
            GameRegistry.addRecipe(
                new ShapelessOreRecipe(
                    ModBlocks.LASER_LENS.newItemStack(1, i + 1),
                    ModBlocks.LASER_LENS.get(),
                    DYE_ORE_NAMES[i]));
            GameRegistry.addRecipe(
                new ShapelessOreRecipe(

                    ModBlocks.LASER_LENS.get(),
                    ModBlocks.LASER_LENS.newItemStack(1, i + 1)));
        }

        // Laser Core
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.LASER_CORE.get(),
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
                ModBlocks.MODIFIER_NULL.get(),
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
                ModBlocks.MODIFIER_PIEZO.get(),
                "RMR",
                "MNM",
                "IMI",
                'R',
                "blockRedstone",
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'I',
                "blockIron"));

        // Speed Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_SPEED.get(),
                "GRG",
                "MNM",
                "GRG",
                'R',
                "blockRedstone",
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'G',
                "blockGold"));

        // Accuracy Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_ACCURACY.get(),
                "RMR",
                "MNM",
                "MDM",
                'R',
                "blockRedstone",
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get(),
                'D',
                "blockDiamond"));

        // Flight Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.MODIFIER_FLIGHT.get(),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.BLOCK_MICA.get(), 1, 0),
                    'F',
                    ModBlocks.STRUCTURE_FRAME.newItemStack(1, 3),
                    'L',
                    "itemLeather",
                    'N',
                    ModBlocks.MODIFIER_NULL.get()));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.MODIFIER_FLIGHT.get(),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.BLOCK_MICA.get(), 1, 0),
                    'F',
                    ModBlocks.STRUCTURE_FRAME.newItemStack(1, 7),
                    'L',
                    "itemLeather",
                    'N',
                    ModBlocks.MODIFIER_NULL.get()));
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.MODIFIER_FLIGHT.get(),
                    "PFP",
                    "MNM",
                    "LFL",
                    'P',
                    new ItemStack(Blocks.sticky_piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.BLOCK_MICA.get(), 1, 0),
                    'F',
                    ModBlocks.STRUCTURE_FRAME.newItemStack(1, 11),
                    'L',
                    "itemLeather",
                    'N',
                    ModBlocks.MODIFIER_NULL.get()));
        }

        // Night Vision Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_NIGHT_VISION.get(),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8198),
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Strength Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_STRENGTH.get(),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8201),
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Water Breathing Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_WATER_BREATHING.get(),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8205),
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Regeneration Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_REGENERATION.get(),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8193),
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Fire Resistance Modifier
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_FIRE_RESISTANCE.get(),
                "MPM",
                "PNP",
                "MPM",
                'P',
                new ItemStack(Items.potionitem, 1, 8195),
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Jump Boost Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.MODIFIER_JUMP_BOOST.get(),
                    "MRM",
                    "PNP",
                    "MRM",
                    'P',
                    "slimeball",
                    'R',
                    new ItemStack(Blocks.piston, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.BLOCK_MICA.get(), 1, 0),
                    'N',
                    ModBlocks.MODIFIER_NULL.get()));
        }

        // Resistance Modifier
        ItemStack prot4Book = new ItemStack(Items.enchanted_book);
        prot4Book.addEnchantment(Enchantment.protection, 4);
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_FIRE_RESISTANCE.get(),
                "MPM",
                "ONO",
                "MPM",
                'O',
                "blockObsidian",
                'P',
                prot4Book,
                'M',
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Haste Modifier
        ItemStack eff5Book = new ItemStack(Items.enchanted_book);
        eff5Book.addEnchantment(Enchantment.efficiency, 5);
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MODIFIER_HASTE.get(),
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
                ModBlocks.BLOCK_MICA.get(),
                'N',
                ModBlocks.MODIFIER_NULL.get()));

        // Saturation Modifier
        if (!LibMods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModBlocks.MODIFIER_SATURATION.get(),
                    "MAM",
                    "CNC",
                    "MAM",
                    'A',
                    new ItemStack(Items.golden_apple, 1, 0),
                    'C',
                    new ItemStack(Items.golden_carrot, 1, 0),
                    'M',
                    new ItemStack(ModBlocks.BLOCK_MICA.get(), 1, 0),
                    'N',
                    ModBlocks.MODIFIER_NULL.get()));
        }

        // Machine Base Basalt
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.MACHINE_BASE.newItemStack(1, 0),
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
                ModBlocks.MACHINE_BASE.newItemStack(1, 1),
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
                ModBlocks.MACHINE_BASE.newItemStack(1, 2),
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
