package louis.omoshiroikamo.common.item;

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
import com.cleanroommc.modularui.utils.ISimpleBauble;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.enderio.core.common.util.ItemUtil;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleExpandedSlots;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.client.IFlightEnablerItem;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.mana.IManaItem;
import louis.omoshiroikamo.client.gui.modularui2.MGuiBuilder;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.recipes.ManaAnvilRecipe;
import louis.omoshiroikamo.common.util.helper.ItemNBTHelper;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibMods;
import louis.omoshiroikamo.common.util.lib.LibResources;
import louis.omoshiroikamo.config.Config;
import vazkii.botania.api.mana.IManaTooltipDisplay;

@EventBusSubscriber()
public class ItemOperationOrb extends ItemBauble implements IManaItem, IManaTooltipDisplay, IAdvancedTooltipProvider,
    IEnergyContainerItem, IFlightEnablerItem, ISimpleBauble, IGuiHolder<PlayerInventoryGuiData> {

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

    // Anvil
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

    @EventBusSubscriber.Condition
    public static boolean shouldEventBusSubscribe() {
        return LibMods.Baubles.isLoaded();
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public String[] getBaubleTypes(ItemStack itemstack) {
        return new String[] { BaubleExpandedSlots.ringType, BaubleExpandedSlots.amuletType };
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }
        EntityPlayer player = event.player;

        boolean hasRing = false;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() != null && stack.getItem() instanceof ItemOperationOrb) {
                hasRing = true;
                break;
            }
        }

        if (player.capabilities.allowFlying == hasRing) {
            return;
        }

        if (hasRing) {
            player.capabilities.allowFlying = true;
        } else {
            if (!player.capabilities.isCreativeMode) {
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
            }
        }
        player.sendPlayerAbilities();
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
                        // Don't allow placing the bag inside itself
                        .filter(stack -> !(stack.getItem() instanceof ItemOperationOrb))))
            .build();
    }
}
