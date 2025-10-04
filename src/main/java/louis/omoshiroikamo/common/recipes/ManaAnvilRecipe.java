package louis.omoshiroikamo.common.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.AnvilUpdateEvent;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import louis.omoshiroikamo.api.mana.IManaItemUpgrade;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class ManaAnvilRecipe {

    public static ManaAnvilRecipe INSTANCE = new ManaAnvilRecipe();

    private List<IManaItemUpgrade> UPGRADES = new ArrayList<IManaItemUpgrade>();

    public ManaAnvilRecipe() {
        UPGRADES.add(EnergyUpgrade.EMPOWERED);
    }

    @SubscribeEvent
    public void handleAnvilEvent(AnvilUpdateEvent event) {
        if (event.left == null || event.right == null) {
            return;
        }
        handleUpgrade(event);
    }

    private void handleUpgrade(AnvilUpdateEvent event) {
        for (IManaItemUpgrade upgrade : UPGRADES) {
            if (upgrade.isUpgradeItem(event.right) && upgrade.canAddToItem(event.left)) {
                ItemStack res = new ItemStack(event.left.getItem(), 1, event.left.getItemDamage());
                if (event.left.stackTagCompound != null) {
                    res.stackTagCompound = (NBTTagCompound) event.left.stackTagCompound.copy();
                }
                upgrade.writeToItem(res);
                event.output = res;
                event.cost = upgrade.getLevelCost();
                return;
            }
        }
    }

    public List<IManaItemUpgrade> getUPGRADES() {
        return UPGRADES;
    }

    public void addCommonTooltipEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        for (IManaItemUpgrade upgrade : UPGRADES) {
            if (upgrade.hasUpgrade(itemstack)) {
                upgrade.addCommonEntries(itemstack, entityplayer, list, flag);
            }
        }
    }

    public void addBasicTooltipEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        for (IManaItemUpgrade upgrade : UPGRADES) {
            if (upgrade.hasUpgrade(itemstack)) {
                upgrade.addBasicEntries(itemstack, entityplayer, list, flag);
            }
        }
    }

    public void addAdvancedTooltipEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {

        List<IManaItemUpgrade> applyableUPGRADES = new ArrayList<IManaItemUpgrade>();
        for (IManaItemUpgrade upgrade : UPGRADES) {
            if (upgrade.hasUpgrade(itemstack)) {
                upgrade.addDetailedEntries(itemstack, entityplayer, list, flag);
            } else if (upgrade.canAddToItem(itemstack)) {
                applyableUPGRADES.add(upgrade);
            }
        }
        if (!applyableUPGRADES.isEmpty()) {
            list.add(EnumChatFormatting.YELLOW + LibMisc.lang.localize("tooltip.anvilUPGRADES") + " ");
            for (IManaItemUpgrade up : applyableUPGRADES) {
                list.add(
                    EnumChatFormatting.DARK_AQUA + ""
                        + ""
                        + LibMisc.lang.localizeExact(up.getUnlocalizedName() + ".name")
                        + ": ");
                list.add(
                    EnumChatFormatting.DARK_AQUA + ""
                        + EnumChatFormatting.ITALIC
                        + "  "
                        + up.getUpgradeItemName()
                        + " + "
                        + up.getLevelCost()
                        + " "
                        + LibMisc.lang.localize("item.mana.tooltip.lvs"));
            }
        }
    }

    public Iterator<IManaItemUpgrade> recipeIterator() {
        return ImmutableList.copyOf(UPGRADES)
            .iterator();
    }
}
