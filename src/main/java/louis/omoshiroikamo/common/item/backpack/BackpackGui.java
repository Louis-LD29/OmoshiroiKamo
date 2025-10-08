package louis.omoshiroikamo.common.item.backpack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.client.gui.modularui2.MGuiBuilder;
import louis.omoshiroikamo.client.gui.modularui2.MGuiTextures;
import louis.omoshiroikamo.client.gui.modularui2.container.BackpackCraftingModularContainer;
import louis.omoshiroikamo.client.gui.modularui2.handler.BackpackItemStackHandler;
import louis.omoshiroikamo.client.gui.modularui2.handler.UpgradeItemStackHandler;
import louis.omoshiroikamo.client.gui.modularui2.slot.BackpackFilterSlot;
import louis.omoshiroikamo.client.gui.modularui2.slot.BackpackSlot;
import louis.omoshiroikamo.client.gui.modularui2.slot.BackpackUpgradeSlot;
import louis.omoshiroikamo.client.gui.modularui2.slot.ModularCraftingSlotAdv;
import louis.omoshiroikamo.client.gui.modularui2.widgets.BackPackPageButton;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.util.ItemNBTHelper;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BackpackGui extends ModularPanel {

    public final UpgradeItemStackHandler upgradeHandler;
    public final BackpackItemStackHandler backpackHandler;
    public final ItemStackHandler craftingHandler;
    public final ItemStackHandler magnetHandler;
    public final ItemStackHandler feedingHandler;
    private final InvWrapper playerInventory;
    private final EntityPlayer player;
    private final PlayerInventoryGuiData data;
    private final PanelSyncManager syncManager;
    private final UISettings settings;
    private final ItemBackpack item;

    private static final String TAG_PAGE = "UpgradePage";
    public static final String MAGNET_FILTER = "MagnetFilter";
    public static final String MAGNET_MODE = "MagnetMode";
    public static final String FEEDING_FILTER = "FeedingFilter";
    public static final String FEEDING_MODE = "FeedingMode";
    public static final String FEEDING_TYPE = "FeedingType";
    public static final String BACKPACKUPGRADE = "BackpackUpgrade";
    public static final String BACKPACKINV = "BackpackInv";
    public static final String CRAFTINGINV = "CraftingInv";
    public static final String MAGNETINV = "MagnetInv";
    public static final String FEEDINGINV = "FeedingInv";
    private static final int[] panelWidth = { 176, 176, 176, 176, 230, 230 };
    private static final int[] panelHeight = { 166, 184, 220, 274, 274, 292 };
    public static int slot = 120;
    public static int upgradeSlot = 7;
    private static final int[] invMargin = { 8, 8, 8, 8, 35, 35 };
    private static final int[] backpackHeight = { 84, 102, 138, 192, 192, 210 };

    private static PagedWidget<?> upgradePage;
    private static PagedWidget.Controller upgradeController;
    private static Flow upgradeTabRow;
    private final List<TabBinding> upgradeTabs = new ArrayList<>();

    public BackpackGui(EntityPlayer player, PlayerInventoryGuiData data, PanelSyncManager syncManager,
        UISettings settings, ItemBackpack item) {
        super("backpack_gui");
        this.player = player;
        this.data = data;
        this.syncManager = syncManager;
        this.settings = settings;
        this.item = item;

        final ItemStack usedItem = data.getUsedItemStack();
        final int meta = usedItem.getItemDamage();

        settings.getNEISettings()
            .enableNEI();

        this.upgradeHandler = new UpgradeItemStackHandler(upgradeSlot) {

            @Override
            protected void onContentsChanged(int slot) {
                if (!player.worldObj.isRemote) {
                    ItemStack usedItem = data.getUsedItemStack();
                    if (usedItem != null) {
                        NBTTagCompound root = usedItem.getTagCompound();
                        if (root == null) {
                            root = new NBTTagCompound();
                        }
                        root.setTag(BACKPACKUPGRADE, this.serializeNBT());
                        usedItem.setTagCompound(root);
                    }
                }
            }
        };

        this.backpackHandler = new BackpackItemStackHandler(slot, this) {

            @Override
            protected void onContentsChanged(int slot) {
                ItemStack usedItem = data.getUsedItemStack();
                if (usedItem != null) {
                    NBTTagCompound root = usedItem.getTagCompound();
                    if (root == null) {
                        root = new NBTTagCompound();
                    }
                    root.setTag(BACKPACKINV, this.serializeNBT());
                    usedItem.setTagCompound(root);
                }
            }
        };

        this.craftingHandler = new ItemStackHandler(10) {

            @Override
            public void onContentsChanged(int slot) {
                ItemStack usedItem = data.getUsedItemStack();
                if (usedItem != null) {
                    NBTTagCompound root = usedItem.getTagCompound();
                    if (root == null) {
                        root = new NBTTagCompound();
                    }
                    root.setTag(CRAFTINGINV, this.serializeNBT());
                    usedItem.setTagCompound(root);
                }
            }
        };

        this.magnetHandler = new ItemStackHandler(9) {

            @Override
            public void onContentsChanged(int slot) {
                ItemStack usedItem = data.getUsedItemStack();
                if (usedItem != null) {
                    NBTTagCompound root = usedItem.getTagCompound();
                    if (root == null) {
                        root = new NBTTagCompound();
                    }
                    root.setTag(MAGNETINV, this.serializeNBT());
                    root.setTag(MAGNET_FILTER, createFilterList(magnetHandler));
                    usedItem.setTagCompound(root);
                }
            }
        };

        this.feedingHandler = new ItemStackHandler(9) {

            @Override
            public void onContentsChanged(int slot) {
                ItemStack usedItem = data.getUsedItemStack();
                if (usedItem != null) {
                    NBTTagCompound root = usedItem.getTagCompound();
                    if (root == null) {
                        root = new NBTTagCompound();
                    }
                    root.setTag(FEEDINGINV, this.serializeNBT());
                    root.setTag(FEEDING_FILTER, createFilterList(feedingHandler));
                    usedItem.setTagCompound(root);
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
                NBTTagCompound root = usedItem.getTagCompound();
                if (root.hasKey(BACKPACKINV)) {
                    backpackHandler.deserializeNBT(root.getCompoundTag(BACKPACKINV));
                }
                if (root.hasKey(BACKPACKUPGRADE)) {
                    upgradeHandler.deserializeNBT(root.getCompoundTag(BACKPACKUPGRADE));
                }
                if (root.hasKey(CRAFTINGINV)) {
                    craftingHandler.deserializeNBT(root.getCompoundTag(CRAFTINGINV));
                }
                if (root.hasKey(MAGNETINV)) {
                    magnetHandler.deserializeNBT(root.getCompoundTag(MAGNETINV));
                }
                if (root.hasKey(FEEDINGINV)) {
                    feedingHandler.deserializeNBT(root.getCompoundTag(FEEDINGINV));
                }
            }
        }

        syncManager.registerSlotGroup("backpack_items", slot);
        syncManager.registerSlotGroup("upgrade_items", upgradeSlot);
        syncManager.registerSlotGroup("player_inventory", 36);

        settings.customContainer(() -> new BackpackCraftingModularContainer(3, 3, this.craftingHandler));

        this.height(panelHeight[meta])
            .width(panelWidth[meta]);

        this.child(
            new Column().child(
                new ParentWidget<>().widthRel(1f)
                    .height(backpackHeight[meta])
                    .child(
                        IKey.str(StatCollector.translateToLocal(item.getUnlocalizedName(usedItem) + ".name"))
                            .asWidget()
                            .margin(6, 0, 5, 0)
                            .align(Alignment.TopLeft))
                    .child(
                        buildBackpackSlotGroup(meta).align(Alignment.Center)
                            .marginTop(1))
                    .child(
                        IKey.str("Inventory")
                            .asWidget()
                            .marginLeft(invMargin[meta])
                            .alignY(0.99f)))
                .child(
                    MGuiBuilder.buildPlayerInventorySlotGroup(playerInventory)
                        .align(Alignment.TopLeft)
                        .marginLeft(invMargin[meta])
                        .marginTop(panelHeight[meta] - 82))
                .child(
                    MGuiBuilder.buildPlayerHotbarSlotGroup(playerInventory)
                        .align(Alignment.TopLeft)
                        .marginLeft(invMargin[meta])
                        .marginTop(panelHeight[meta] - 24)));

        this.child(
            Flow.column()
                .rightRelOffset(1f, 1)
                .background(GuiTextures.MC_BACKGROUND)
                .excludeAreaInNEI()
                .coverChildren()
                .padding(4)
                .childPadding(4)
                .child(buildUpgradeSlotGroup(meta)));

        this.child(buildUpgradePage());
    }

    private SlotGroupWidget buildBackpackSlotGroup(int tier) {
        SlotGroupWidget.Builder builder = SlotGroupWidget.builder();

        int rows = this.getItem()
            .getBackpackRow(tier);
        int cols = this.getItem()
            .getBackpackCol(tier);
        int size = rows * cols;

        char[] chars = new char[cols];
        Arrays.fill(chars, 'I');
        String rowPattern = new String(chars);

        String[] matrix = new String[rows];
        Arrays.fill(matrix, rowPattern);
        builder.matrix(matrix);

        builder.key('I', index -> {
            ItemSlot slotWidget = new ItemSlot().slot(new BackpackSlot(backpackHandler, index, this) {

                @Override
                public void onSlotChanged() {
                    super.onSlotChanged();
                    ItemStack usedItem = gui.getData()
                        .getUsedItemStack();
                    if (usedItem != null) {
                        NBTTagCompound root = usedItem.getTagCompound();
                        if (root == null) {
                            root = new NBTTagCompound();
                        }
                        root.setTag("BackpackInv", gui.backpackHandler.serializeNBT());
                        usedItem.setTagCompound(root);
                    }
                }
            }.slotGroup("backpack_items")
                .filter(stack -> !(stack.getItem() instanceof ItemBackpack)));

            if (index >= size) {
                slotWidget.setEnabled(false);
            }
            return slotWidget;
        });

        SlotGroupWidget widget = builder.build();

        return widget;
    }

    private SlotGroupWidget buildUpgradeSlotGroup(int tier) {
        SlotGroupWidget.Builder builder = SlotGroupWidget.builder();

        int rows = this.getItem()
            .getUpgradeRow(tier);
        int cols = 1;

        int size = rows * cols;

        char[] chars = new char[cols];
        Arrays.fill(chars, 'I');
        String rowPattern = new String(chars);

        String[] matrix = new String[rows];
        Arrays.fill(matrix, rowPattern);
        builder.matrix(matrix);
        builder.key('I', index -> {
            ItemSlot slotWidget = new ItemSlot().slot(new BackpackUpgradeSlot(upgradeHandler, index, this) {

                @Override
                public void onSlotChanged() {
                    updateUpgradeWidgets();
                }
            }.slotGroup("upgrade_items"));

            if (index >= size) {
                slotWidget.setEnabled(false);
            }

            return slotWidget;
        });

        return builder.build();

    }

    private void updateUpgradeWidgets() {
        Map<Class<? extends ItemUpgrade>, Boolean> enableMap = new HashMap<>();

        for (TabBinding binding : upgradeTabs) {
            enableMap.put(binding.upgradeClass, false);
        }

        boolean hasBatteryUpgrade = false;

        for (int slotIndex = 0; slotIndex < upgradeHandler.getSlots(); slotIndex++) {
            ItemStack stack = upgradeHandler.getStackInSlot(slotIndex);
            if (stack == null) {
                continue;
            }
            if (!(stack.getItem() instanceof ItemUpgrade itemUpgrade)) {
                continue;
            }

            if (itemUpgrade instanceof ItemBatteryUpgrade) {
                hasBatteryUpgrade = true;
            }

            if (!itemUpgrade.hasTab()) {
                continue;
            }

            enableMap.put(itemUpgrade.getClass(), true);
        }

        batteryUpgradeToItem(hasBatteryUpgrade);

        for (TabBinding binding : upgradeTabs) {
            boolean enable = enableMap.getOrDefault(binding.upgradeClass, false);
            binding.button.setEnabled(enable);
        }

        List<Integer> enabledIndices = new ArrayList<>();
        for (int i = 0; i < upgradeTabs.size(); i++) {
            if (enableMap.getOrDefault(upgradeTabs.get(i).upgradeClass, false)) {
                enabledIndices.add(i + 1);
            }
        }

        if (upgradeController.isInitialised()) {
            int savedIndex = ItemNBTHelper.getInt(getUsedItemStack(), TAG_PAGE, 0);

            if (enabledIndices.isEmpty()) {
                upgradeController.setPage(0);
                upgradePage.setEnabled(true);
                upgradeTabRow.setEnabled(false);
            } else {
                upgradePage.setEnabled(true);
                upgradeTabRow.setEnabled(true);

                if (enabledIndices.contains(savedIndex)) {
                    upgradeController.setPage(savedIndex);
                } else {
                    int fallback = enabledIndices.get(0);
                    upgradeController.setPage(fallback);
                }
            }
        }
    }

    private IWidget buildUpgradePage() {
        ParentWidget<?> widget = new ParentWidget<>().leftRelOffset(1, 1)
            .coverChildren();

        upgradeController = new PagedWidget.Controller();
        upgradePage = new PagedWidget<>().controller(upgradeController)
            .debugName("root parent")
            .rightRelOffset(0f, 1);

        upgradeTabRow = new Row().debugName("Tab col")
            .coverChildren()
            .topRel(0f, 4, 1f);

        List<TabEntry> tabs = Arrays.asList(
            new TabEntry(ModItems.itemCraftingUpgrade, buildCraftingSlotGroup(), ItemCraftingUpgrade.class),
            new TabEntry(ModItems.itemMagnetUpgrade, buildMagnetSlotGroup(), ItemMagnetUpgrade.class),
            new TabEntry(ModItems.itemFeedingUpgrade, buildFeedingSlotGroup(), ItemFeedingUpgrade.class));

        ParentWidget<?> emptyPage = new ParentWidget<>().debugName("Empty Page");
        upgradePage.addPage(emptyPage);

        for (int i = 0; i < tabs.size(); i++) {
            TabEntry tab = tabs.get(i);
            int index = i + 1;

            BackPackPageButton button = new BackPackPageButton(index, upgradeController)
                .tab(GuiTextures.TAB_TOP, i == 0 ? -1 : 0)
                .size(22)
                .excludeAreaInNEI()
                .syncHandler(
                    new InteractionSyncHandler()
                        .setOnMousePressed(mouseData -> ItemNBTHelper.setInt(getUsedItemStack(), TAG_PAGE, index)))
                .overlay(
                    tab.getDrawable()
                        .asIcon());

            upgradeTabRow.child(button);
            upgradeTabs.add(new TabBinding(button, tab.page, tab.upgradeClass));
            upgradePage.addPage(tab.page);
        }

        this.child(upgradeTabRow);
        widget.child(upgradePage);

        upgradePage.setEnabled(true);
        upgradeTabRow.setEnabled(false);

        updateUpgradeWidgets();
        return widget;
    }

    private static class TabBinding {

        final BackPackPageButton button;
        final IWidget page;
        final Class<? extends ItemUpgrade> upgradeClass;

        TabBinding(BackPackPageButton button, IWidget page, Class<? extends ItemUpgrade> upgradeClass) {
            this.button = button;
            this.page = page;
            this.upgradeClass = upgradeClass;
        }
    }

    private static class TabEntry {

        private final Item itemIcon;
        private final IWidget page;
        private final Class<? extends ItemUpgrade> upgradeClass;

        TabEntry(Item icon, IWidget page, Class<? extends ItemUpgrade> upgradeClass) {
            this.itemIcon = icon;
            this.page = page;
            this.upgradeClass = upgradeClass;
        }

        public ItemDrawable getDrawable() {
            if (itemIcon != null) {
                return new ItemDrawable(itemIcon);
            }
            return new ItemDrawable(Blocks.air);
        }
    }

    private IWidget buildCraftingSlotGroup() {
        ParentWidget<?> widget = new ParentWidget<>();
        widget.padding(7)
            .background(GuiTextures.MC_BACKGROUND)
            .coverChildren()
            .excludeAreaInNEI()
            .child(
                new ItemDrawable(ModItems.itemCraftingUpgrade).asIcon()
                    .asWidget()
                    .size(18)
                    .pos(5, 5))
            .child(
                IKey.str("Craft")
                    .asWidget()
                    .pos(23, 10))
            .child(
                SlotGroupWidget.builder()
                    .row("III")
                    .row("III")
                    .row("III")
                    .row(" | ")
                    .row(" O ")
                    .key('I', i -> new ItemSlot().slot(new ModularSlot(craftingHandler, i)))
                    .key('|', new Widget<>().overlay(GuiTextures.ARROW_DOWN))
                    .key('O', new ItemSlot().slot(new ModularCraftingSlotAdv(craftingHandler, 9)))
                    .build()
                    .margin(0, 7, 18, 5));
        return widget;
    }

    private IWidget buildMagnetSlotGroup() {
        ParentWidget<?> widget = new ParentWidget<>();
        widget.padding(7)
            .background(GuiTextures.MC_BACKGROUND)
            .coverChildren()
            .excludeAreaInNEI()
            .child(
                new ItemDrawable(ModItems.itemMagnetUpgrade).asIcon()
                    .asWidget()
                    .size(18)
                    .pos(5, 5))
            .child(
                IKey.str("Magnet")
                    .asWidget()
                    .pos(23, 10))
            .child(
                SlotGroupWidget.builder()
                    .row("F  ")
                    .key(
                        'F',
                        new ToggleButton().selectedBackground(GuiTextures.MC_BUTTON)
                            .tooltipBuilder(
                                richTooltip -> richTooltip
                                    .add(LibMisc.lang.localize(LibResources.TOOLTIP + "filter_mode")))
                            .overlay(true, MGuiTextures.WHITELIST)
                            .overlay(false, MGuiTextures.BLACKLIST)
                            .value(
                                new BooleanSyncValue(
                                    () -> ItemNBTHelper.getBoolean(getUsedItemStack(), MAGNET_MODE, false),
                                    value -> ItemNBTHelper.setBoolean(getUsedItemStack(), MAGNET_MODE, value))))
                    .build()
                    .margin(0, 7, 18, 5))
            .child(
                SlotGroupWidget.builder()
                    .row("III")
                    .row("III")
                    .row("III")
                    .key('I', i -> new PhantomItemSlot().slot(new BackpackFilterSlot(magnetHandler, i)))
                    .build()
                    .margin(0, 7, 40, 5));
        return widget;
    }

    private IWidget buildFeedingSlotGroup() {
        ParentWidget<?> widget = new ParentWidget<>();
        widget.padding(7)
            .background(GuiTextures.MC_BACKGROUND)
            .coverChildren()
            .excludeAreaInNEI()
            .child(
                new ItemDrawable(ModItems.itemFeedingUpgrade).asIcon()
                    .asWidget()
                    .size(18)
                    .pos(5, 5))
            .child(
                IKey.str("Feeding")
                    .asWidget()
                    .pos(23, 10))
            .child(
                SlotGroupWidget.builder()
                    .row("FM ")
                    .key(
                        'F',
                        new ToggleButton().selectedBackground(GuiTextures.MC_BUTTON)
                            .tooltipBuilder(
                                richTooltip -> richTooltip
                                    .add(LibMisc.lang.localize(LibResources.TOOLTIP + "filter_mode")))
                            .overlay(false, MGuiTextures.WHITELIST)
                            .overlay(true, MGuiTextures.BLACKLIST)
                            .value(
                                new BooleanSyncValue(
                                    () -> ItemNBTHelper.getBoolean(getUsedItemStack(), FEEDING_MODE, false),
                                    value -> ItemNBTHelper.setBoolean(getUsedItemStack(), FEEDING_MODE, value))))
                    .key(
                        'M',
                        new ToggleButton().selectedBackground(GuiTextures.MC_BUTTON)
                            .tooltipBuilder(
                                richTooltip -> richTooltip
                                    .add(LibMisc.lang.localize(LibResources.TOOLTIP + "feeding_type")))
                            .overlay(false, MGuiTextures.FULL_HUNGER)
                            .overlay(true, MGuiTextures.EXACT_HUNGER)
                            .value(
                                new BooleanSyncValue(
                                    () -> ItemNBTHelper.getBoolean(getUsedItemStack(), FEEDING_TYPE, false),
                                    value -> ItemNBTHelper.setBoolean(getUsedItemStack(), FEEDING_TYPE, value))))
                    .build()
                    .margin(0, 7, 18, 5))
            .child(
                SlotGroupWidget.builder()
                    .row("III")
                    .row("III")
                    .row("III")
                    .key('I', i -> new PhantomItemSlot() {}.slot(new BackpackFilterSlot(feedingHandler, i) {

                        @Override
                        public boolean isItemValid(@Nullable ItemStack stack) {
                            if (stack == null) {
                                return false;
                            }
                            return stack.getItem() instanceof ItemFood;
                        }
                    }))
                    .build()
                    .margin(0, 7, 40, 5));
        return widget;
    }

    public int getTotalStackMultiplier() {
        List<ItemStack> stacks = upgradeHandler.getStacks();
        int result = 0;

        if (stacks.isEmpty()) {
            return 1;
        }

        for (ItemStack stack : stacks) {
            if (stack == null) {
                continue;
            }
            if (stack.getItem() instanceof ItemStackUpgrade upgrade) {
                result += upgrade.multiplier(stack);
            }
        }
        return result == 0 ? 1 : result;
    }

    public boolean canAddStackUpgrade(int newMultiplier) {
        int currentMultiplier = getTotalStackMultiplier() * 64;

        try {
            Math.multiplyExact(currentMultiplier, newMultiplier);
            return true;
        } catch (ArithmeticException e) {
            return false;
        }
    }

    public boolean canRemoveStackUpgrade(int originalMultiplier) {
        return canReplaceStackUpgrade(originalMultiplier, 1);
    }

    public boolean canReplaceStackUpgrade(int oldMultiplier, int newMultiplier) {
        int newStackMultiplier = getTotalStackMultiplier() / oldMultiplier * newMultiplier;

        for (ItemStack stack : backpackHandler.getStacks()) {
            if (stack == null) {
                continue;
            }

            if (stack.stackSize > stack.getMaxStackSize() * newStackMultiplier) {
                return false;
            }
        }

        return true;
    }

    public boolean canAddCraftingUpgrade() {
        for (int i = 0; i < upgradeHandler.getSlots(); i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemCraftingUpgrade) {
                return false;
            }
        }
        return true;
    }

    public boolean canRemoveCraftingUpgrade() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = craftingHandler.getStackInSlot(i);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    public boolean canAddMagnetUpgrade() {
        for (int i = 0; i < upgradeHandler.getSlots(); i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemMagnetUpgrade) {
                return false;
            }
        }
        return true;
    }

    public boolean canAddFeedingUpgrade() {
        for (int i = 0; i < upgradeHandler.getSlots(); i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFeedingUpgrade) {
                return false;
            }
        }
        return true;
    }

    public boolean canAddBatteryUpgrade() {
        for (int i = 0; i < upgradeHandler.getSlots(); i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBatteryUpgrade) {
                return false;
            }
        }
        return true;
    }

    // Getter
    public PlayerInventoryGuiData getData() {
        return data;
    }

    public ItemStack getUsedItemStack() {
        return getData().getUsedItemStack();
    }

    public ItemBackpack getItem() {
        return item;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public PanelSyncManager getSyncManager() {
        return syncManager;
    }

    public UISettings getSettings() {
        return settings;
    }

    private NBTTagList createFilterList(ItemStackHandler handler) {
        NBTTagList filterList = new NBTTagList();

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() != null) {
                NBTTagCompound itemTag = new NBTTagCompound();

                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(stackInSlot.getItem());
                if (uid != null) {
                    String id = uid.modId + ":" + uid.name;
                    itemTag.setString("id", id);
                    itemTag.setInteger("meta", stackInSlot.getItemDamage());
                    filterList.appendTag(itemTag);
                }
            }
        }

        return filterList;
    }

    private void batteryUpgradeToItem(boolean active) {
        ItemStack stack = getUsedItemStack();
        NBTTagCompound root = stack.getTagCompound();
        EnergyUpgrade eu = EnergyUpgrade.loadFromItem(stack);

        if (active) {
            int storedEnergy = 0;

            if (root.hasKey("StoredEnergyBackup")) {
                storedEnergy = root.getInteger("StoredEnergyBackup");
                root.removeTag("StoredEnergyBackup");
            }

            if (eu == null) {
                EnergyUpgrade.ENERGY_TIER_THREE.writeToItem(stack);
            } else {
                eu.writeToItem(stack);
            }

            if (storedEnergy > 0) {
                NBTTagCompound upgradeTag = root.getCompoundTag(EnergyUpgrade.ENERGY_TIER_THREE.id);
                if (upgradeTag != null) {
                    upgradeTag.setInteger(EnergyUpgrade.KEY_ENERGY, storedEnergy);
                }
            }

        } else if (eu != null) {
            NBTTagCompound upgradeTag = root.getCompoundTag(EnergyUpgrade.ENERGY_TIER_THREE.id);
            int storedEnergy = 0;

            if (upgradeTag != null && upgradeTag.hasKey(EnergyUpgrade.KEY_ENERGY)) {
                storedEnergy = upgradeTag.getInteger(EnergyUpgrade.KEY_ENERGY);
            }

            eu.removeFromItem(stack);

            if (storedEnergy > 0) {
                root.setInteger("StoredEnergyBackup", storedEnergy);
            }
        }
    }

}
