package ruiseki.omoshiroikamo.common.init;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import cpw.mods.fml.common.FMLCommonHandler;
import ruiseki.omoshiroikamo.common.achievement.AchievementEntry;
import ruiseki.omoshiroikamo.common.achievement.AchievementTrigger;
import ruiseki.omoshiroikamo.common.achievement.AchievementsRegistry;
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
            ModBlocks.SOLAR_ARRAY.newItemStack(1, 0),
            craft_assembler);
        assemble_solar_array_t4 = new AchievementEntry(
            "assemble_solar_array_t4",
            3,
            2,
            ModBlocks.SOLAR_ARRAY.newItemStack(1, 3),
            assemble_solar_array_t1).setSpecial();
        assemble_void_ore_miner_t1 = new AchievementEntry(
            "assemble_void_ore_miner_t1",
            -1,
            2,
            ModBlocks.VOID_ORE_MINER.newItemStack(1, 0),
            craft_assembler);
        assemble_void_ore_miner_t4 = new AchievementEntry(
            "assemble_void_ore_miner_t4",
            -3,
            2,
            ModBlocks.VOID_ORE_MINER.newItemStack(1, 3),
            assemble_void_ore_miner_t1).setSpecial();
        assemble_void_res_miner_t1 = new AchievementEntry(
            "assemble_void_res_miner_t1",
            -1,
            4,
            ModBlocks.VOID_RES_MINER.newItemStack(1, 0),
            craft_assembler);
        assemble_void_res_miner_t4 = new AchievementEntry(
            "assemble_void_res_miner_t4",
            -3,
            4,
            ModBlocks.VOID_RES_MINER.newItemStack(1, 3),
            assemble_void_res_miner_t1).setSpecial();
        assemble_nano_bot_beacon_t1 = new AchievementEntry(
            "assemble_nano_bot_beacon_t1",
            -1,
            6,
            ModBlocks.NANO_BOT_BEACON.newItemStack(1, 0),
            craft_assembler);
        assemble_nano_bot_beacon_t4 = new AchievementEntry(
            "assemble_nano_bot_beacon_t4",
            -3,
            6,
            ModBlocks.NANO_BOT_BEACON.newItemStack(1, 3),
            assemble_nano_bot_beacon_t1).setSpecial();

        craft_modifier_core = new AchievementEntry("craft_modifier_core", 7, 0, ModBlocks.MODIFIER_NULL.get(), null);
        craft_modifier_speed = new AchievementEntry(
            "craft_modifier_speed",
            6,
            2,
            ModBlocks.MODIFIER_SPEED.get(),
            craft_modifier_core);
        craft_modifier_accuracy = new AchievementEntry(
            "craft_modifier_accuracy",
            6,
            4,
            ModBlocks.MODIFIER_ACCURACY.get(),
            craft_modifier_core);
        craft_modifier_flight = new AchievementEntry(
            "craft_modifier_flight",
            6,
            6,
            ModBlocks.MODIFIER_FLIGHT.get(),
            craft_modifier_core);
        craft_modifier_resistance = new AchievementEntry(
            "craft_modifier_resistance",
            6,
            8,
            ModBlocks.MODIFIER_RESISTANCE.get(),
            craft_modifier_core);
        craft_modifier_haste = new AchievementEntry(
            "craft_modifier_haste",
            6,
            10,
            ModBlocks.MODIFIER_HASTE.get(),
            craft_modifier_core);
        craft_modifier_night_vision = new AchievementEntry(
            "craft_modifier_night_vision",
            6,
            12,
            ModBlocks.MODIFIER_NIGHT_VISION.get(),
            craft_modifier_core);
        craft_modifier_regen = new AchievementEntry(
            "craft_modifier_regen",
            6,
            14,
            ModBlocks.MODIFIER_REGENERATION.get(),
            craft_modifier_core);

        craft_modifier_piezo = new AchievementEntry(
            "craft_modifier_piezo",
            8,
            2,
            ModBlocks.MODIFIER_PIEZO.get(),
            craft_modifier_core);
        craft_modifier_jump_boost = new AchievementEntry(
            "craft_modifier_jump_boost",
            8,
            4,
            ModBlocks.MODIFIER_JUMP_BOOST.get(),
            craft_modifier_core);
        craft_modifier_fire_res = new AchievementEntry(
            "craft_modifier_fire_res",
            8,
            8,
            ModBlocks.MODIFIER_FIRE_RESISTANCE.get(),
            craft_modifier_core);
        craft_modifier_strength = new AchievementEntry(
            "craft_modifier_strength",
            8,
            10,
            ModBlocks.MODIFIER_STRENGTH.get(),
            craft_modifier_core);
        craft_modifier_water_breathing = new AchievementEntry(
            "craft_modifier_water_breathing",
            8,
            12,
            ModBlocks.MODIFIER_WATER_BREATHING.get(),
            craft_modifier_core);
        craft_modifier_saturation = new AchievementEntry(
            "craft_modifier_saturation",
            8,
            14,
            ModBlocks.MODIFIER_SATURATION.get(),
            craft_modifier_core);

        craft_colored_lens = new AchievementEntry("craft_colored_lens", 10, 3, ModBlocks.LASER_LENS.get(), null);

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
