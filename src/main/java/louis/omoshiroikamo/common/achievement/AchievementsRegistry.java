package louis.omoshiroikamo.common.achievement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.item.ModItems;

public class AchievementsRegistry {

    public static final List<ItemStack> craftingList = new ArrayList<>();
    public static final List<ItemStack> pickupList = new ArrayList<>();

    public static void init() {
        addItemsToCraftingList();
        addBlocksToCraftingList();
        addItemsToPickupList();
        addBlocksToPickupList();
    }

    public static void addItemsToCraftingList() {
        craftingList.add(new ItemStack(ModItems.itemAssembler, 1, 0));
    }

    public static void addBlocksToCraftingList() {
        craftingList.add(new ItemStack(ModBlocks.blockSolarArray, 1, 0)); // Tier 1
        craftingList.add(new ItemStack(ModBlocks.blockSolarArray, 1, 3)); // Tier 4
        craftingList.add(new ItemStack(ModBlocks.blockVoidOreMiner, 1, 0)); // Tier 1
        craftingList.add(new ItemStack(ModBlocks.blockVoidOreMiner, 1, 3)); // Tier 4
        craftingList.add(new ItemStack(ModBlocks.blockModifierNull, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierSpeed, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierPiezo, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierAccuracy, 1, 0));
    }

    public static void addItemsToPickupList() {
        // add pickups if needed
    }

    public static void addBlocksToPickupList() {}

    public static Achievement getAchievementForItem(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getItemDamage();

        if (item == ModItems.itemAssembler) {
            return ModAchievements.craft_assembler;
        }

        if (item == Item.getItemFromBlock(ModBlocks.blockLaserLens)) {
            return ModAchievements.craft_colored_lens;
        }

        if (item == Item.getItemFromBlock(ModBlocks.blockModifierNull)) {
            return ModAchievements.craft_modifier_core;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierSpeed)) {
            return ModAchievements.craft_modifier_speed;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierPiezo)) {
            return ModAchievements.craft_modifier_piezo;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierAccuracy)) {
            return ModAchievements.craft_modifier_accuracy;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockSolarArray)) {
            if (meta == 0) {
                return ModAchievements.craft_solar_array_t1;
            }
            if (meta == 3) {
                return ModAchievements.craft_solar_array_t4;
            }
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockVoidOreMiner)) {
            if (meta == 0) {
                return ModAchievements.craft_void_ore_t1;
            }
            if (meta == 3) {
                return ModAchievements.craft_void_ore_t4;
            }
        }

        return null;
    }

    public static Achievement getAchievementForBlock(Block block, int meta) {
        if (block == ModBlocks.blockSolarArray) {
            if (meta == 0) {
                return ModAchievements.craft_solar_array_t1;
            }
            if (meta == 3) {
                return ModAchievements.craft_solar_array_t4;
            }
        }
        if (block == ModBlocks.blockVoidOreMiner) {
            if (meta == 0) {
                return ModAchievements.craft_void_ore_t1;
            }
            if (meta == 3) {
                return ModAchievements.craft_void_ore_t4;
            }
        }

        if (block == ModBlocks.blockLaserLens) {
            return ModAchievements.craft_colored_lens;
        }

        if (block == ModBlocks.blockModifierNull) {
            return ModAchievements.craft_modifier_core;
        }
        if (block == ModBlocks.blockModifierSpeed) {
            return ModAchievements.craft_modifier_speed;
        }
        if (block == ModBlocks.blockModifierPiezo) {
            return ModAchievements.craft_modifier_piezo;
        }
        if (block == ModBlocks.blockModifierAccuracy) {
            return ModAchievements.craft_modifier_accuracy;
        }
        return null;
    }
}
