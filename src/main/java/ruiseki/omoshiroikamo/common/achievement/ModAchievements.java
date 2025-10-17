package ruiseki.omoshiroikamo.common.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import cpw.mods.fml.common.FMLCommonHandler;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.item.ModItems;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;

public class ModAchievements {

    public static AchievementPage omoshiroikamoPage;

    public static Achievement craft_assembler;
    public static Achievement assemble_solar_array_t1;
    public static Achievement assemble_solar_array_t4;
    public static Achievement assemble_void_ore_miner_t1;
    public static Achievement assemble_void_ore_miner_t4;
    public static Achievement assemble_void_res_miner_t1;
    public static Achievement assemble_void_res_miner_t4;
    public static Achievement assemble_nano_bot_beacon_t1;
    public static Achievement assemble_nano_bot_beacon_t4;

    public static Achievement craft_modifier_core;
    public static Achievement craft_modifier_speed;
    public static Achievement craft_modifier_piezo;
    public static Achievement craft_modifier_accuracy;
    public static Achievement craft_modifier_jump_boost;
    public static Achievement craft_modifier_flight;
    public static Achievement craft_modifier_resistance;
    public static Achievement craft_modifier_fire_res;
    public static Achievement craft_modifier_haste;
    public static Achievement craft_modifier_strength;
    public static Achievement craft_modifier_night_vision;
    public static Achievement craft_modifier_water_breathing;
    public static Achievement craft_modifier_regen;
    public static Achievement craft_modifier_saturation;

    public static Achievement craft_colored_lens;

    public static void init() {

        craft_assembler = new AchievementEntry(
            "craft_assembler",
            0,
            0,
            new ItemStack(ModItems.itemAssembler, 1, 0),
            null);
        assemble_solar_array_t1 = new AchievementEntry(
            "assemble_solar_array_t1",
            1,
            2,
            new ItemStack(ModBlocks.blockSolarArray, 1, 0),
            craft_assembler);
        assemble_solar_array_t4 = new AchievementEntry(
            "assemble_solar_array_t4",
            3,
            2,
            new ItemStack(ModBlocks.blockSolarArray, 1, 3),
            assemble_solar_array_t1).setSpecial();
        assemble_void_ore_miner_t1 = new AchievementEntry(
            "assemble_void_ore_miner_t1",
            -1,
            2,
            new ItemStack(ModBlocks.blockVoidOreMiner, 1, 0),
            craft_assembler);
        assemble_void_ore_miner_t4 = new AchievementEntry(
            "assemble_void_ore_miner_t4",
            -3,
            2,
            new ItemStack(ModBlocks.blockVoidOreMiner, 1, 3),
            assemble_void_ore_miner_t1).setSpecial();
        assemble_void_res_miner_t1 = new AchievementEntry(
            "assemble_void_res_miner_t1",
            -1,
            4,
            new ItemStack(ModBlocks.blockVoidResMiner, 1, 0),
            craft_assembler);
        assemble_void_res_miner_t4 = new AchievementEntry(
            "assemble_void_res_miner_t4",
            -3,
            4,
            new ItemStack(ModBlocks.blockVoidResMiner, 1, 3),
            assemble_void_res_miner_t1).setSpecial();
        assemble_nano_bot_beacon_t1 = new AchievementEntry(
            "assemble_nano_bot_beacon_t1",
            -1,
            6,
            new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 0),
            craft_assembler);
        assemble_nano_bot_beacon_t4 = new AchievementEntry(
            "assemble_nano_bot_beacon_t4",
            -3,
            6,
            new ItemStack(ModBlocks.blockNanoBotBeacon, 1, 3),
            assemble_nano_bot_beacon_t1).setSpecial();

        craft_modifier_core = new AchievementEntry(
            "craft_modifier_core",
            7,
            0,
            new ItemStack(ModBlocks.blockModifierNull, 1, 0),
            null);
        craft_modifier_speed = new AchievementEntry(
            "craft_modifier_speed",
            6,
            2,
            new ItemStack(ModBlocks.blockModifierSpeed, 1, 0),
            craft_modifier_core);
        craft_modifier_accuracy = new AchievementEntry(
            "craft_modifier_accuracy",
            6,
            4,
            new ItemStack(ModBlocks.blockModifierAccuracy, 1, 0),
            craft_modifier_core);
        craft_modifier_flight = new AchievementEntry(
            "craft_modifier_flight",
            6,
            6,
            new ItemStack(ModBlocks.blockModifierFlight, 1, 0),
            craft_modifier_core);
        craft_modifier_resistance = new AchievementEntry(
            "craft_modifier_resistance",
            6,
            8,
            new ItemStack(ModBlocks.blockModifierResistance, 1, 0),
            craft_modifier_core);
        craft_modifier_haste = new AchievementEntry(
            "craft_modifier_haste",
            6,
            10,
            new ItemStack(ModBlocks.blockModifierHaste, 1, 0),
            craft_modifier_core);
        craft_modifier_night_vision = new AchievementEntry(
            "craft_modifier_night_vision",
            6,
            12,
            new ItemStack(ModBlocks.blockModifierNightVision, 1, 0),
            craft_modifier_core);
        craft_modifier_regen = new AchievementEntry(
            "craft_modifier_regen",
            6,
            14,
            new ItemStack(ModBlocks.blockModifierRegeneration, 1, 0),
            craft_modifier_core);

        craft_modifier_piezo = new AchievementEntry(
            "craft_modifier_piezo",
            8,
            2,
            new ItemStack(ModBlocks.blockModifierPiezo, 1, 0),
            craft_modifier_core);
        craft_modifier_jump_boost = new AchievementEntry(
            "craft_modifier_jump_boost",
            8,
            4,
            new ItemStack(ModBlocks.blockModifierJumpBoost, 1, 0),
            craft_modifier_core);
        craft_modifier_fire_res = new AchievementEntry(
            "craft_modifier_fire_res",
            8,
            8,
            new ItemStack(ModBlocks.blockModifierFireResistance, 1, 0),
            craft_modifier_core);
        craft_modifier_strength = new AchievementEntry(
            "craft_modifier_strength",
            8,
            10,
            new ItemStack(ModBlocks.blockModifierStrength, 1, 0),
            craft_modifier_core);
        craft_modifier_water_breathing = new AchievementEntry(
            "craft_modifier_water_breathing",
            8,
            12,
            new ItemStack(ModBlocks.blockModifierWaterBreathing, 1, 0),
            craft_modifier_core);
        craft_modifier_saturation = new AchievementEntry(
            "craft_modifier_saturation",
            8,
            14,
            new ItemStack(ModBlocks.blockModifierSaturation, 1, 0),
            craft_modifier_core);

        craft_colored_lens = new AchievementEntry(
            "craft_colored_lens",
            10,
            3,
            new ItemStack(ModBlocks.blockLaserLens, 1, 0),
            null);

        omoshiroikamoPage = new AchievementPage(
            LibMisc.MOD_NAME,
            AchievementEntry.achievements.toArray(new Achievement[0]));

        AchievementPage.registerAchievementPage(omoshiroikamoPage);

        AchievementsRegistry.init();
        FMLCommonHandler.instance()
            .bus()
            .register(new AchievementTrigger());
    }
}
