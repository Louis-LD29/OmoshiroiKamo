package louis.omoshiroikamo.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

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
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import louis.omoshiroikamo.client.gui.modularui2.MGuiBuilder;
import louis.omoshiroikamo.client.gui.modularui2.container.BackPackContainer;
import louis.omoshiroikamo.client.gui.modularui2.container.BackpackCraftingModularContainer;
import louis.omoshiroikamo.client.gui.modularui2.handler.BackpackItemStackHandler;
import louis.omoshiroikamo.client.gui.modularui2.handler.UpgradeItemStackHandler;
import louis.omoshiroikamo.client.gui.modularui2.slot.ModularBackpackSlot;
import louis.omoshiroikamo.client.gui.modularui2.slot.ModularCraftingSlotAdv;
import louis.omoshiroikamo.client.gui.modularui2.slot.ModularUpgradeSlot;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.item.backpack.BackpackMagnetController;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;
import louis.omoshiroikamo.common.item.backpack.ItemCraftingUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemMagnetUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemStackUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemUpgrade;

public class BackpackGui extends ModularPanel {

    static BackpackMagnetController controller = new BackpackMagnetController();

    public final UpgradeItemStackHandler upgradeHandler;
    public final BackpackItemStackHandler backpackHandler;
    public final ItemStackHandler craftingHandler;
    private final InvWrapper playerInventory;
    private final EntityPlayer player;
    private final PlayerInventoryGuiData data;
    private final PanelSyncManager syncManager;
    private final UISettings settings;
    private final ItemBackpack item;

    public static final String BACKPACKUPGRADE = "BackpackUpgrade";
    public static final String BACKPACKINV = "BackpackInv";
    public static final String CRAFTINGINV = "CraftingInv";
    private static final int[] panelWidth = { 176, 176, 176, 176, 230, 230 };
    private static final int[] panelHeight = { 166, 184, 220, 274, 274, 292 };
    public static int slot = 120;
    public static int upgradeSlot = 7;
    private static final int[] invMargin = { 8, 8, 8, 8, 35, 35 };
    private static final int[] backpackHeight = { 84, 102, 138, 192, 192, 210 };
    private static IWidget craftingTabPage;
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
            }
        }

        syncManager.registerSlotGroup("backpack_items", slot);
        syncManager.registerSlotGroup("upgrade_items", upgradeSlot, 101);
        syncManager.registerSlotGroup("player_inventory", 36, 99);

        settings.customContainer(() -> new BackPackContainer(this));
        settings.customContainer(() -> new BackpackCraftingModularContainer(3, 3, this.craftingHandler));

        this.height(panelHeight[meta])
            .width(panelWidth[meta]);

        this.child(buildUpgradePage());

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
                        buildBagSlotGroup(meta).align(Alignment.Center)
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
    }

    private SlotGroupWidget buildBagSlotGroup(int tier) {
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
            ItemSlot slotWidget = new ItemSlot().slot(new ModularBackpackSlot(backpackHandler, index, this) {

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

        return builder.build();

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
            ItemSlot slotWidget = new ItemSlot().slot(new ModularUpgradeSlot(upgradeHandler, index, this) {

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
        boolean craftingEnable = false;

        for (int slotIndex = 0; slotIndex < upgradeHandler.getSlots(); slotIndex++) {
            ItemStack stack = upgradeHandler.getStackInSlot(slotIndex);
            if (stack == null) {
                continue;
            }
            if (!(stack.getItem() instanceof ItemUpgrade itemUpgrade)) {
                continue;
            }

            if (!itemUpgrade.hasTab()) {
                continue;
            }

            if (itemUpgrade instanceof ItemCraftingUpgrade) {
                craftingEnable = true;
            }
        }
        craftingTabPage.setEnabled(craftingEnable);
        for (TabBinding binding : upgradeTabs) {
            if (binding.page == craftingTabPage) {
                binding.button.setEnabled(craftingEnable);
            }
        }
    }

    private IWidget buildUpgradePage() {
        ParentWidget<?> widget = new ParentWidget<>().leftRelOffset(1, 1)
            .coverChildren();
        upgradeController = new PagedWidget.Controller();
        upgradePage = new PagedWidget<>().debugName("root parent")
            .controller(upgradeController)
            .rightRel(0f, 4, 1f);
        upgradeTabRow = new Column().debugName("Tab col")
            .coverChildren()
            .topRel(0f, 4, 1f);

        craftingTabPage = buildCraftingSlotGroup();
        craftingTabPage.setEnabled(false);
        upgradePage.addPage(craftingTabPage);

        List<TabEntry> tabs = Arrays.asList(new TabEntry(ModItems.itemCraftingUpgrade, craftingTabPage));

        int pageIndex = 0;
        for (TabEntry tab : tabs) {
            PageButton button = new PageButton(pageIndex, upgradeController)
                .tab(GuiTextures.TAB_TOP, pageIndex == 0 ? -1 : 0)
                .size(22)
                .excludeAreaInNEI()
                .overlay(
                    tab.getDrawable()
                        .asIcon());

            upgradeTabRow.child(button);
            button.setEnabled(false);

            upgradeTabs.add(new TabBinding(button, tab.page));

            pageIndex++;
        }

        this.child(upgradeTabRow);
        widget.child(upgradePage);
        return widget;
    }

    private static class TabBinding {

        final PageButton button;
        final IWidget page;

        TabBinding(PageButton button, IWidget page) {
            this.button = button;
            this.page = page;
        }
    }

    private static class TabEntry {

        final Item itemIcon;
        final Block blockIcon;
        final IWidget page;

        TabEntry(Item icon, IWidget page) {
            this.itemIcon = icon;
            this.blockIcon = null;
            this.page = page;
        }

        public ItemDrawable getDrawable() {
            if (blockIcon != null) {
                return new ItemDrawable(blockIcon);
            }
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
                    .key('|', new Widget().overlay(GuiTextures.ARROW_DOWN))
                    .key('O', new ItemSlot().slot(new ModularCraftingSlotAdv(craftingHandler, 9) {

                        @Override
                        public boolean canTakeStack(EntityPlayer player) {
                            boolean allEmpty = true;

                            for (int i = 0; i < 9; i++) {
                                ItemStack stack = craftingHandler.getStackInSlot(i);
                                if (stack != null) {
                                    allEmpty = false;
                                    break;
                                }
                            }

                            if (allEmpty) {
                                return false;
                            }
                            return super.canTakeStack(player);
                        }
                    }))
                    .build()
                    .margin(0, 0, 18, 5));
        widget.setEnabled(false);
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

    // Getter
    public PlayerInventoryGuiData getData() {
        return data;
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
}
