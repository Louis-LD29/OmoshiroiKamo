package ruiseki.omoshiroikamo.common.achievement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import ruiseki.omoshiroikamo.common.init.ModAchievements;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.init.ModItems;

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
        craftingList.add(ModBlocks.MODIFIER_NULL.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_SPEED.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_PIEZO.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_ACCURACY.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_JUMP_BOOST.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_FLIGHT.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_RESISTANCE.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_FIRE_RESISTANCE.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_HASTE.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_STRENGTH.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_NIGHT_VISION.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_WATER_BREATHING.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_REGENERATION.newItemStack());
        craftingList.add(ModBlocks.MODIFIER_SATURATION.newItemStack());
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

        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_NULL.get())) {
            return ModAchievements.craft_modifier_core;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_SPEED.get())) {
            return ModAchievements.craft_modifier_speed;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_PIEZO.get())) {
            return ModAchievements.craft_modifier_piezo;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_ACCURACY.get())) {
            return ModAchievements.craft_modifier_accuracy;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_JUMP_BOOST.get())) {
            return ModAchievements.craft_modifier_jump_boost;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_FLIGHT.get())) {
            return ModAchievements.craft_modifier_flight;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_RESISTANCE.get())) {
            return ModAchievements.craft_modifier_resistance;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_FIRE_RESISTANCE.get())) {
            return ModAchievements.craft_modifier_fire_res;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_HASTE.get())) {
            return ModAchievements.craft_modifier_haste;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_STRENGTH.get())) {
            return ModAchievements.craft_modifier_strength;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_NIGHT_VISION.get())) {
            return ModAchievements.craft_modifier_night_vision;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_WATER_BREATHING.get())) {
            return ModAchievements.craft_modifier_water_breathing;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_REGENERATION.get())) {
            return ModAchievements.craft_modifier_regen;
        }
        if (item == Item.getItemFromBlock(ModBlocks.MODIFIER_SATURATION.get())) {
            return ModAchievements.craft_modifier_saturation;
        }

        return null;
    }
}
