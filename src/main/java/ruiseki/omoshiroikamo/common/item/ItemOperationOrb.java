package ruiseki.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.enderio.core.common.util.ItemUtil;

import baubles.api.BaubleType;
import cofh.api.energy.IEnergyContainerItem;
import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.item.IAnvilUpgradeItem;
import ruiseki.omoshiroikamo.client.gui.modularui2.MGuiBuilder;
import ruiseki.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import ruiseki.omoshiroikamo.common.recipes.ManaAnvilRecipe;
import ruiseki.omoshiroikamo.common.util.ItemNBTHelper;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;
import ruiseki.omoshiroikamo.config.item.ItemConfig;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;

public class ItemOperationOrb extends ItemBauble implements IManaItem, IManaTooltipDisplay, IEnergyContainerItem,
    IGuiHolder<PlayerInventoryGuiData>, IAnvilUpgradeItem {

    protected static final int MAX_MANA = 500000;

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
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 1000 - (getMana(stack) / getMaxMana(stack) * 1000);
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
        return (float) getMana(stack) / getMaxMana(stack);
    }

    // Anvil
    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        ManaAnvilRecipe.addCommonTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        ManaAnvilRecipe.addBasicTooltipEntries(itemstack, entityplayer, list, flag);
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean flag) {
        if (!ItemConfig.addDurabilityTootip) {
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
        ManaAnvilRecipe.addAdvancedTooltipEntries(itemstack, entityplayer, list, flag);
    }

    // Energy
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

    // Bauble

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        super.onEquipped(stack, entity);

        if (!(entity instanceof EntityPlayer player)) {
            return;
        }
        if (player instanceof FakePlayer) {
            return;
        }

        player.capabilities.allowFlying = true;
        player.sendPlayerAbilities();
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        super.onUnequipped(stack, entity);

        if (!(entity instanceof EntityPlayer player)) {
            return;
        }
        if (player instanceof FakePlayer) {
            return;
        }

        if (!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    private ItemStackHandler inventoryHandler;
    private InvWrapper playerInventory;

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData guiData, PanelSyncManager guiSyncManager, UISettings settings) {
        final int panelHeight = 220;
        ModularPanel panel = ModularPanel.defaultPanel("orb", 176, panelHeight);
        final ItemStack usedItem = guiData.getUsedItemStack();
        final EntityPlayer player = guiData.getPlayer();

        this.inventoryHandler = new ItemStackHandler(54) {

            @Override
            protected void onContentsChanged(int slot) {
                ItemStack usedItem = guiData.getUsedItemStack();
                if (usedItem != null) {
                    usedItem.setTagCompound(this.serializeNBT());
                }
            }
        };

        this.playerInventory = new InvWrapper(player.inventory) {

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot == player.inventory.currentItem) {
                    return null;
                }

                return super.extractItem(slot, amount, simulate);
            }
        };

        if (!player.worldObj.isRemote) {
            if (usedItem.hasTagCompound()) {
                inventoryHandler.deserializeNBT(usedItem.getTagCompound());
            }
        }
        guiSyncManager.registerSlotGroup("orb_items", 54);

        panel.child(
            new Column().child(
                new ParentWidget<>().widthRel(1f)
                    .height(138)
                    .child(
                        IKey.str(StatCollector.translateToLocal("item.itemOperationOrb.name"))
                            .asWidget()
                            .margin(6, 0, 5, 0)
                            .align(Alignment.TopLeft))
                    .child(
                        buildBagSlotGroup().align(Alignment.Center)
                            .marginTop(1))
                    .child(
                        IKey.str("Inventory")
                            .asWidget()
                            .alignX(0.05f)
                            .alignY(0.99f)))
                .child(
                    MGuiBuilder.buildPlayerInventorySlotGroup(playerInventory)
                        .align(Alignment.TopLeft)
                        .marginLeft(8)
                        .marginTop(panelHeight - 82))
                .child(
                    MGuiBuilder.buildPlayerHotbarSlotGroup(playerInventory)
                        .align(Alignment.TopLeft)
                        .marginLeft(8)
                        .marginTop(panelHeight - 24)));

        return panel;
    }

    private SlotGroupWidget buildBagSlotGroup() {
        return SlotGroupWidget.builder()
            .row("IIIIIIIII")
            .row("IIIIIIIII")
            .row("IIIIIIIII")
            .row("IIIIIIIII")
            .row("IIIIIIIII")
            .row("IIIIIIIII")
            .key(
                'I',
                index -> new ItemSlot().slot(
                    new ModularSlot(inventoryHandler, index).slotGroup("orb_items")
                        .filter(stack -> !(stack.getItem() instanceof ItemOperationOrb))))
            .build();
    }
}
