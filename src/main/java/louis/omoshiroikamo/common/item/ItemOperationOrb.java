package louis.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

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

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleExpandedSlots;
import cofh.api.energy.IEnergyContainerItem;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.client.IFlightEnablerItem;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.mana.IManaItem;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.recipes.ManaAnvilRecipe;
import louis.omoshiroikamo.common.util.helper.ItemNBTHelper;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;
import vazkii.botania.api.mana.IManaTooltipDisplay;

public class ItemOperationOrb extends ItemBauble implements IManaItem, IManaTooltipDisplay, IAdvancedTooltipProvider,
    IEnergyContainerItem, IFlightEnablerItem, IGuiHolder<PlayerInventoryGuiData>, ISimpleBauble {

    protected static final int MAX_MANA = 500000;
    protected static final float CONVERSION_RATE = 625f;

    public static ItemOperationOrb create() {
        ItemOperationOrb item = new ItemOperationOrb();
        item.init();
        return item;
    }

    public ItemOperationOrb() {
        super(ModObject.itemOperationOrb.unlocalisedName);
        this.disableRightClickEquip();
        setMaxDamage(1000);
        setNoRepair();
    }

    public static void setMana(ItemStack stack, int mana) {
        ItemNBTHelper.setInt(stack, LibResources.KEY_MANA, mana);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemStack empty = new ItemStack(item);
        setMana(empty, 0);
        list.add(empty);

        ItemStack full = new ItemStack(item);
        setMana(full, MAX_MANA);
        list.add(full);
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
        return ItemNBTHelper.getInt(stack, LibResources.KEY_MANA, 0);
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
        ManaAnvilRecipe.INSTANCE.addCommonTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        ManaAnvilRecipe.INSTANCE.addBasicTooltipEntries(itemstack, entityplayer, list, flag);
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
                    + LibMisc.lang
                        .localize("item." + ModObject.itemOperationOrb.unlocalisedName + ".tooltip.effPowered"));
        }
        ManaAnvilRecipe.INSTANCE.addAdvancedTooltipEntries(itemstack, entityplayer, list, flag);
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
        if (!(stack.getItem() instanceof IManaItem manaItem)) return;

        if (!player.capabilities.allowFlying) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        }

        if (player.capabilities.isFlying) manaItem.addMana(stack, -625);

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
        return new String[] { BaubleExpandedSlots.ringType, BaubleExpandedSlots.amuletType };
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData guiData, PanelSyncManager guiSyncManager, UISettings settings) {
        IItemHandlerModifiable itemHandler = new ItemStackItemHandler(guiData, 4);

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
