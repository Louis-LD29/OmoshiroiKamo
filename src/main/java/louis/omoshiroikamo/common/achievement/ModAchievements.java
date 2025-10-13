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
