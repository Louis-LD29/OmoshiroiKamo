package ruiseki.omoshiroikamo.common.recipes.voidMiner;

import java.io.File;

import com.enderio.core.common.util.DyeColor;

import ruiseki.omoshiroikamo.api.item.IFocusableRegistry;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;

public class VoidMinerRecipes {

    public static IFocusableRegistry voidOreMinerRegistry;
    public static IFocusableRegistry voidResMinerRegistry;

    public static void init() {
        voidOreMinerRegistry = VoidOreMinerRegistry.getInstance();
        voidResMinerRegistry = VoidResMinerRegistry.getInstance();
        addVoidOreMinerRecipes();
        addVoidResMinerRecipes();
    }

    public static void addVoidOreMinerRecipes() {
        IFocusableRegistry reg = voidOreMinerRegistry;
        File file = new File("config/" + LibMisc.MOD_ID + "/void_ore_miner/minable.json");
        if (file.exists()) {
            FocusableHandler.loadRegistryFromJson(file, reg);
        } else {
            FocusableHandler.FocusableList defaults = new FocusableHandler.FocusableList();
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCoal", DyeColor.BLACK, 40));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreIron", DyeColor.WHITE, 32));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreQuartz", DyeColor.WHITE, 20));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreRedstone", DyeColor.RED, 20));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreLapis", DyeColor.BLUE, 16));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreGold", DyeColor.YELLOW, 14));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreDiamond", DyeColor.CYAN, 11));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreEmerald", DyeColor.LIME, 5));
            defaults.addEntry(new FocusableHandler.FocusableOre("glowstone", DyeColor.YELLOW, 7));
            defaults.addEntry(new FocusableHandler.FocusableOre("shardLonsdaleite", DyeColor.BLACK, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCopper", DyeColor.ORANGE, 26));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreTin", DyeColor.GRAY, 26));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSilver", DyeColor.SILVER, 20));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreLead", DyeColor.PURPLE, 22));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAluminum", DyeColor.WHITE, 20));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreUranium", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreYellorite", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCertusQuartz", DyeColor.LIGHT_BLUE, 8));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreChargedCertusQuartz", DyeColor.LIGHT_BLUE, 4));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCinnabar", DyeColor.BROWN, 10));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAmber", DyeColor.ORANGE, 9));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreNickel", DyeColor.GRAY, 9));
            defaults.addEntry(new FocusableHandler.FocusableOre("orePlatinum", DyeColor.LIGHT_BLUE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreMithril", DyeColor.LIGHT_BLUE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSulfur", DyeColor.YELLOW, 10));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreNiter", DyeColor.WHITE, 10));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSaltpeter", DyeColor.WHITE, 8));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreFirestone", DyeColor.RED, 4));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCobalt", DyeColor.BLUE, 7));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreArdite", DyeColor.ORANGE, 7));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreApatite", DyeColor.LIGHT_BLUE, 10));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreDraconium", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreGraphite", DyeColor.BLACK, 12));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreQuartzBlack", DyeColor.BLACK, 14));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreRuby", DyeColor.RED, 8));
            defaults.addEntry(new FocusableHandler.FocusableOre("orePeridot", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreTopaz", DyeColor.ORANGE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreTanzanite", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreMalachite", DyeColor.CYAN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSapphire", DyeColor.BLUE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreGarnet", DyeColor.ORANGE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreHeliodore", DyeColor.YELLOW, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreBeryl", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreIndicolite", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAquamarine", DyeColor.BLUE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreIolite", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAmethyst", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAgate", DyeColor.PINK, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreMorganite", DyeColor.PINK, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreOnyx", DyeColor.BLACK, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreOpal", DyeColor.WHITE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCarnelian", DyeColor.RED, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSpinel", DyeColor.BROWN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreCitrine", DyeColor.BROWN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreJasper", DyeColor.YELLOW, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreGoldenBeryl", DyeColor.YELLOW, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreMoldavite", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreTurquoise", DyeColor.CYAN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreMoonstone", DyeColor.CYAN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreBlueTopaz", DyeColor.BLUE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreVioletSapphire", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreLepidolite", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAmetrine", DyeColor.PURPLE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreBlackDiamond", DyeColor.BLACK, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreAlexandrite", DyeColor.WHITE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreChaos", DyeColor.WHITE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreEnderEssence", DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreResonating", DyeColor.BROWN, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreZinc", DyeColor.PINK, 9));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreIridium", DyeColor.WHITE, 6));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreTungsten", DyeColor.BLACK, 8));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreOsmium", DyeColor.SILVER, 8));
            defaults.addEntry(new FocusableHandler.FocusableOre("oreSalt", DyeColor.WHITE, 3));
            defaults
                .addEntry(new FocusableHandler.FocusableBlock("rftools:dimensional_shard_ore", 0, DyeColor.WHITE, 4));
            defaults.addEntry(
                new FocusableHandler.FocusableBlock("mysticalagriculture:inferium_ore", 0, DyeColor.GREEN, 6));
            defaults.addEntry(
                new FocusableHandler.FocusableBlock("mysticalagriculture:prosperity_ore", 0, DyeColor.SILVER, 4));
            FocusableHandler.saveRegistryDefaultsToJson(file, defaults);
            FocusableHandler.loadIntoRegistry(defaults, reg);
        }
    }

    public static void addVoidResMinerRecipes() {
        IFocusableRegistry reg = voidResMinerRegistry;
        File file = new File("config/" + LibMisc.MOD_ID + "/void_resource_miner/minable.json");
        if (file.exists()) {
            FocusableHandler.loadRegistryFromJson(file, reg);
        } else {
            FocusableHandler.FocusableList defaults = new FocusableHandler.FocusableList();
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:stone", 0, DyeColor.GRAY, 16));
            defaults.addEntry(new FocusableHandler.FocusableBlock("etfuturum:stone", 1, DyeColor.PINK, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("etfuturum:stone", 3, DyeColor.WHITE, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("etfuturum:stone", 5, DyeColor.GRAY, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:gravel", 0, DyeColor.SILVER, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:grass", 0, DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:dirt", 0, DyeColor.BROWN, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("etfuturum:coarse_dirt", 0, DyeColor.BROWN, 6));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:sand", 0, DyeColor.YELLOW, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:sand", 1, DyeColor.YELLOW, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:cobblestone", 0, DyeColor.GRAY, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:mossy_cobblestone", 0, DyeColor.GREEN, 6));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:obsidian", 0, DyeColor.PURPLE, 5));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:clay", 0, DyeColor.SILVER, 7));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:netherrack", 0, DyeColor.RED, 14));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:soul_sand", 0, DyeColor.BROWN, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:mycelium", 0, DyeColor.PURPLE, 4));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:end_stone", 0, DyeColor.WHITE, 7));
            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:hardened_clay", 0, DyeColor.RED, 6));

            for (DyeColor dye : DyeColor.values()) {
                defaults.addEntry(
                    new FocusableHandler.FocusableBlock("minecraft:stained_hardened_clay", dye.getColor(), dye, 1));
            }

            defaults.addEntry(new FocusableHandler.FocusableBlock("minecraft:sponge", 0, DyeColor.YELLOW, 2));
            defaults
                .addEntry(new FocusableHandler.FocusableBlock(LibMisc.MOD_ID + ":alabaster", 0, DyeColor.WHITE, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock(LibMisc.MOD_ID + ":basalt", 0, DyeColor.BLACK, 10));
            defaults.addEntry(
                new FocusableHandler.FocusableBlock(LibMisc.MOD_ID + ":hardened_stone", 0, DyeColor.GRAY, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock(LibMisc.MOD_ID + ":mica", 0, DyeColor.WHITE, 2));
            defaults.addEntry(new FocusableHandler.FocusableBlock("chisel:limestone", 0, DyeColor.LIME, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock("chisel:marble", 0, DyeColor.WHITE, 10));
            defaults.addEntry(new FocusableHandler.FocusableBlock("TConstruct:CraftedSoil", 5, DyeColor.GREEN, 2));
            defaults.addEntry(new FocusableHandler.FocusableBlock("TConstruct:slime.grass", 0, DyeColor.GREEN, 1));
            defaults.addEntry(
                new FocusableHandler.FocusableBlock("appliedenergistics2:tile.BlockSkyStone", 0, DyeColor.BLACK, 2));
            FocusableHandler.saveRegistryDefaultsToJson(file, defaults);
            FocusableHandler.loadIntoRegistry(defaults, reg);
        }

    }
}
