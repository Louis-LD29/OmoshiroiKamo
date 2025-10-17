package ruiseki.omoshiroikamo.common.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class AchievementTrigger {

    @SubscribeEvent
    public void onItemPickedUp(PlayerEvent.ItemPickupEvent event) {
        ItemStack stack = event.pickedUp.getEntityItem();
        if (stack == null) {
            return;
        }

        for (ItemStack entry : AchievementsRegistry.pickupList) {
            if (ItemStack.areItemStacksEqual(stack, entry)) {
                Achievement achievement = AchievementsRegistry.getAchievementForItem(stack);
                if (achievement != null) {
                    event.player.addStat(achievement, 1);
                }
                break;
            }
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.crafting;
        if (result == null) {
            return;
        }

        for (ItemStack entry : AchievementsRegistry.craftingList) {
            if (ItemStack.areItemStacksEqual(result, entry)) {
                Achievement achievement = AchievementsRegistry.getAchievementForItem(result);
                if (achievement != null) {
                    event.player.addStat(achievement, 1);
                }
                break;
            }
        }
    }
}
