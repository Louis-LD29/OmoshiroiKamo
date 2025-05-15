package com.louis.test.common.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import cofh.api.energy.IEnergyContainerItem;
import com.enderio.core.common.util.ItemUtil;

import com.louis.test.Test;
import com.louis.test.common.item.upgrade.EnergyUpgrade;
import com.louis.test.common.recipes.ManaAnvilRecipe;
import com.louis.test.config.Config;
import com.louis.test.core.interfaces.*;
import com.louis.test.core.helper.ItemNBTHelper;
import com.louis.test.lib.LibItemNames;
import cpw.mods.fml.common.Optional;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

@Optional.Interface(iface = "vazkii.botania.api.mana.IManaItem", modid = "Botania")
public class ItemOperationOrb extends ItemBauble implements
    IManaItem, vazkii.botania.api.mana.IManaItem,
    IManaTooltipDisplay, IAdvancedTooltipProvider, IEnergyContainerItem, IFlightEnablerItem {

    protected static final int MAX_MANA = 500000;
    protected static final float CONVERSION_RATE  = 625f;
    private static final String TAG_MANA = "mana";

    public ItemOperationOrb() {
        this(LibItemNames.OPERATIONORB);
        setMaxDamage(1000);
        setNoRepair();
    }

    public ItemOperationOrb(String name) {
        super(name);
        setMaxDamage(1000);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(par1, 1, 10000));
    }

    @Override
    public int getDamage(ItemStack stack) {
        float mana = getMana(stack);
        return 1000 - (int) (mana / getMaxMana(stack) * 1000);
    }

    @Override
    public int getDisplayDamage(ItemStack stack) {
        return getDamage(stack);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    public static void setMana(ItemStack stack, int mana) {
        ItemNBTHelper.setInt(stack, TAG_MANA, mana);
    }

    @Override
    public int getMana(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return MAX_MANA;
    }

    @Override
    public void addMana(ItemStack stack, int mana) {
        setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
        stack.setItemDamage(getDamage(stack));
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
        return true;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return true;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
        return false;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
        return true;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean isNoExport(ItemStack stack) {
        return false;
    }

    @Override
    public float getManaFractionForDisplay(ItemStack stack) {
        return (float) getMana(stack) / (float) getMaxMana(stack);
    }

    @Override
    public float getConversionRate(ItemStack stack) {
        return CONVERSION_RATE;
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        ManaAnvilRecipe.instance.addCommonTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        ManaAnvilRecipe.instance.addBasicTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        if (!Config.addDurabilityTootip) {
            list.add(ItemUtil.getDurabilityString(itemstack));
        }
        String str = EnergyUpgrade.getStoredEnergyString(itemstack);
        if (str != null) {
            list.add(str);
        }
        if (EnergyUpgrade.itemHasAnyPowerUpgrade(itemstack)) {
            list.add(
                EnumChatFormatting.WHITE + "+"
                    + " "
                    + Test.lang.localize("item." + LibItemNames.OPERATIONORB + ".tooltip.effPowered"));
        }
        ManaAnvilRecipe.instance.addAdvancedTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return EnergyUpgrade.receiveEnergy(container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return EnergyUpgrade.extractEnergy(container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return EnergyUpgrade.getEnergyStored(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return EnergyUpgrade.getMaxEnergyStored(container);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        super.onWornTick(stack, entity);
        if (!(entity instanceof EntityPlayer player)) return;

        IManaItem manaItem = (IManaItem) stack.getItem();
        if (manaItem == null) return;

        if (!player.capabilities.allowFlying) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        }

        if (player.capabilities.isFlying)
            manaItem.addMana(stack, - 625);

        if (manaItem.getMana(stack) < 50000) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        super.onUnequipped(stack, entity);

        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        IInventory baubles = BaublesApi.getBaubles(player);
        boolean hasFlightItem = false;
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack otherStack = baubles.getStackInSlot(i);
            if (otherStack != null && otherStack.getItem() instanceof IFlightEnablerItem) {
                hasFlightItem = true;
                break;
            }
        }

        if (!hasFlightItem) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

}

