package louis.omoshiroikamo.common.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import cpw.mods.fml.common.FMLCommonHandler;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class ModAchievements {

    public static AchievementPage omoshiroikamoPage;

    public static Achievement craft_assembler;
    public static Achievement craft_solar_array_t1;
    public static Achievement craft_solar_array_t4;
    public static Achievement craft_void_ore_t1;
    public static Achievement craft_void_ore_t4;

    public static Achievement craft_modifier_core;
    public static Achievement craft_modifier_speed;
    public static Achievement craft_modifier_piezo;
    public static Achievement craft_modifier_accuracy;

    public static Achievement craft_colored_lens;

    public static void init() {

        craft_assembler = new AchievementEntry(
            "craft_assembler",
            0,
            0,
            new ItemStack(ModItems.itemAssembler, 1, 0),
            null);
        craft_solar_array_t1 = new AchievementEntry(
            "craft_solar_array_t1",
            1,
            2,
            new ItemStack(ModBlocks.blockSolarArray, 1, 0),
            craft_assembler);
        craft_solar_array_t4 = new AchievementEntry(
            "craft_solar_array_t4",
            3,
            2,
            new ItemStack(ModBlocks.blockSolarArray, 1, 3),
            craft_solar_array_t1).setSpecial();
        craft_void_ore_t1 = new AchievementEntry(
            "craft_void_ore_t1",
            -1,
            2,
            new ItemStack(ModBlocks.blockVoidOreMiner, 1, 0),
            craft_assembler);
        craft_void_ore_t4 = new AchievementEntry(
            "craft_void_ore_t4",
            -3,
            2,
            new ItemStack(ModBlocks.blockVoidOreMiner, 1, 3),
            craft_void_ore_t1).setSpecial();

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
        craft_modifier_piezo = new AchievementEntry(
            "craft_modifier_piezo",
            8,
            2,
            new ItemStack(ModBlocks.blockModifierPiezo, 1, 0),
            craft_modifier_core);
        craft_modifier_accuracy = new AchievementEntry(
            "craft_modifier_accuracy",
            6,
            4,
            new ItemStack(ModBlocks.blockModifierAccuracy, 1, 0),
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
