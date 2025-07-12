package com.louis.test.common.item;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleExpandedSlots;
import cofh.api.energy.IEnergyContainerItem;
import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.ISimpleBauble;
import com.cleanroommc.modularui.utils.ItemStackItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.enderio.core.common.util.ItemUtil;
import com.louis.test.api.ModObject;
import com.louis.test.api.client.IAdvancedTooltipProvider;
import com.louis.test.api.client.IFlightEnablerItem;
import com.louis.test.api.mana.IManaItem;
import com.louis.test.common.config.Config;
import com.louis.test.common.core.helper.ItemNBTHelper;
import com.louis.test.common.core.lib.LibMisc;
import com.louis.test.common.item.upgrade.EnergyUpgrade;
import com.louis.test.common.recipes.ManaAnvilRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaTooltipDisplay;

import java.util.List;

public class ItemOperationOrb extends ItemBauble implements IManaItem, IManaTooltipDisplay, IAdvancedTooltipProvider,
    IEnergyContainerItem, IFlightEnablerItem, IGuiHolder<PlayerInventoryGuiData>, ISimpleBauble {

    protected static final int MAX_MANA = 500000;
    protected static final float CONVERSION_RATE = 625f;
    private static final String TAG_MANA = "mana";

    public ItemOperationOrb() {
        this(ModObject.itemOperationOrb.unlocalisedName);
        setMaxDamage(1000);
        this.disableRightClickEquip();
        setNoRepair();
    }

    public ItemOperationOrb(String name) {
        super(name);
    }

    public static void setMana(ItemStack stack, int mana) {
        ItemNBTHelper.setInt(stack, TAG_MANA, mana);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(par1));
    }

    @Override
    public int getDamage(ItemStack stack) {
        float mana = getMana(stack);
        return 1000 - (int) (mana / getMaxMana(stack) * 1000);
    }

    @Override
    @Deprecated
    public int getDisplayDamage(ItemStack stack) {
        return getDamage(stack);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
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

    @Override
    public void addMana(ItemStack stack, int mana, EntityPlayer player) {
        setMana(stack, Math.min(getMana(stack) + mana, getMaxMana(stack)));
        stack.setItemDamage(getDamage(stack));
    }

    @Override
    public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
        return true;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return this.getMana(stack) < MAX_MANA * 0.75f;
    }

    @Override
    public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
        return false;
    }

    @Override
    public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
        return true;
    }

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
                    + LibMisc.lang.localize("item." + ModObject.itemOperationOrb.unlocalisedName + ".tooltip.effPowered"));
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
    public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase entity) {
        super.onEquippedOrLoadedIntoWorld(stack, entity);
        if (!(entity instanceof EntityPlayer player)) return;

        IManaItem manaItem = (IManaItem) stack.getItem();
        if (manaItem == null) return;

        if (!player.capabilities.allowFlying) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        }

        if (player.capabilities.isFlying) manaItem.addMana(stack, -625, player);

        if (manaItem.getMana(stack) < 50000) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    @Override
    public String[] getBaubleTypes(ItemStack itemstack) {
        return new String[]{BaubleExpandedSlots.ringType};
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData guiData, PanelSyncManager guiSyncManager, UISettings settings) {
        IItemHandlerModifiable itemHandler = new ItemStackItemHandler(guiData, 4);
        // guiSyncManager.registerSlotGroup("mixer_items", 2);

        guiSyncManager.addOpenListener(player -> {

        });
        ModularPanel panel = ModularPanel.defaultPanel("knapping_gui");
        panel.child(
            new Column().margin(7)
                .child(
                    new ParentWidget<>().widthRel(1f)
                        .expanded()
                        .child(
                            SlotGroupWidget.builder()
                                .row("II")
                                .row("II")
                                .key('I', index -> new ItemSlot().slot(SyncHandlers.itemSlot(itemHandler, index)))
                                .build()
                                .align(Alignment.Center)))
                .child(SlotGroupWidget.playerInventory(false)));

        return panel;
    }

}
