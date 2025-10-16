package louis.omoshiroikamo.common.achievement;

import java.util.ArrayList;
import java.util.List;

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
        craftingList.add(new ItemStack(ModBlocks.blockModifierNull, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierSpeed, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierPiezo, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierAccuracy, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierJumpBoost, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierFlight, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierResistance, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierFireResistance, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierHaste, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierStrength, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierNightVision, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierWaterBreathing, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierRegeneration, 1, 0));
        craftingList.add(new ItemStack(ModBlocks.blockModifierSaturation, 1, 0));
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
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierJumpBoost)) {
            return ModAchievements.craft_modifier_jump_boost;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierFlight)) {
            return ModAchievements.craft_modifier_flight;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierResistance)) {
            return ModAchievements.craft_modifier_resistance;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierFireResistance)) {
            return ModAchievements.craft_modifier_fire_res;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierHaste)) {
            return ModAchievements.craft_modifier_haste;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierStrength)) {
            return ModAchievements.craft_modifier_strength;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierNightVision)) {
            return ModAchievements.craft_modifier_night_vision;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierWaterBreathing)) {
            return ModAchievements.craft_modifier_water_breathing;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierRegeneration)) {
            return ModAchievements.craft_modifier_regen;
        }
        if (item == Item.getItemFromBlock(ModBlocks.blockModifierSaturation)) {
            return ModAchievements.craft_modifier_saturation;
        }

        return null;
    }
}
