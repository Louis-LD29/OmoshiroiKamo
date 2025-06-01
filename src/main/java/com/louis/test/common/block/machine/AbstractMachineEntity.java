package com.louis.test.common.block.machine;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.Dialog;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.InventoryWrapper;
import com.enderio.core.common.util.ItemUtil;
import com.louis.test.api.enums.IoMode;
import com.louis.test.api.interfaces.IIoConfigurable;
import com.louis.test.api.interfaces.redstone.IRedstoneConnectable;
import com.louis.test.common.block.TileEntityEio;
import com.louis.test.common.gui.MGuiTextures;
import com.louis.test.lib.LibMisc;

import crazypants.enderio.machine.IMachine;
import crazypants.enderio.machine.IRedstoneModeControlable;
import crazypants.enderio.machine.RedstoneControlMode;
import crazypants.enderio.machine.SlotDefinition;

public abstract class AbstractMachineEntity extends TileEntityEio implements IGuiHolder<PosGuiData>, ISidedInventory,
    IMachine, IRedstoneModeControlable, IRedstoneConnectable, IIoConfigurable {

    public short facing;

    protected int ticksSinceSync = -1;
    protected boolean forceClientUpdate = true;
    protected boolean lastActive;
    protected int ticksSinceActiveChanged = 0;

    public final ItemStackHandler inv;
    protected final SlotDefinition slotDefinition;

    protected RedstoneControlMode redstoneControlMode;

    protected boolean redstoneCheckPassed;

    private boolean redstoneStateDirty = true;

    protected Map<ForgeDirection, IoMode> faceModes;

    private final int[] allSlots;

    protected boolean notifyNeighbours = false;

    public boolean isDirty = false;

    protected boolean inverted = false;

    protected AbstractMachineEntity(SlotDefinition slotDefinition) {
        this.slotDefinition = slotDefinition;
        facing = 3;

        inv = new ItemStackHandler(slotDefinition.getNumSlots());
        redstoneControlMode = RedstoneControlMode.IGNORE;

        allSlots = new int[slotDefinition.getNumSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    @Override
    public IoMode toggleIoModeForFace(ForgeDirection faceHit) {
        IoMode curMode = getIoMode(faceHit);
        IoMode mode = curMode.next();
        while (!supportsMode(faceHit, mode)) {
            mode = mode.next();
        }
        setIoMode(faceHit, mode);
        return mode;
    }

    @Override
    public boolean supportsMode(ForgeDirection faceHit, IoMode mode) {
        return true;
    }

    @Override
    public void setIoMode(ForgeDirection faceHit, IoMode mode) {
        if (mode == IoMode.NONE && faceModes == null) {
            return;
        }
        if (faceModes == null) {
            faceModes = new EnumMap<ForgeDirection, IoMode>(ForgeDirection.class);
        }
        faceModes.put(faceHit, mode);
        forceClientUpdate = true;
        notifyNeighbours = true;

        updateBlock();
    }

    @Override
    public void clearAllIoModes() {
        if (faceModes != null) {
            faceModes = null;
            forceClientUpdate = true;
            notifyNeighbours = true;
            updateBlock();
        }
    }

    @Override
    public IoMode getIoMode(ForgeDirection face) {
        if (faceModes == null) {
            return IoMode.NONE;
        }
        IoMode res = faceModes.get(face);
        if (res == null) {
            return IoMode.NONE;
        }
        return res;
    }

    public SlotDefinition getSlotDefinition() {
        return slotDefinition;
    }

    public boolean isValidUpgrade(ItemStack itemstack) {
        for (int i = slotDefinition.getMinUpgradeSlot(); i <= slotDefinition.getMaxUpgradeSlot(); i++) {
            if (isItemValidForSlot(i, itemstack)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidInput(ItemStack itemstack) {
        for (int i = slotDefinition.getMinInputSlot(); i <= slotDefinition.getMaxInputSlot(); i++) {
            if (isItemValidForSlot(i, itemstack)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidOutput(ItemStack itemstack) {
        for (int i = slotDefinition.getMinOutputSlot(); i <= slotDefinition.getMaxOutputSlot(); i++) {
            if (isItemValidForSlot(i, itemstack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean isItemValidForSlot(int i, ItemStack itemstack) {
        // if (slotDefinition.isUpgradeSlot(i)) {
        // return itemstack != null && itemstack.getItem() == EnderIO.itemBasicCapacitor
        // && itemstack.getItemDamage() > 0
        // && itemstack.getItemDamage() != 7;
        // }
        return isMachineItemValidForSlot(i, itemstack);
    }

    protected abstract boolean isMachineItemValidForSlot(int i, ItemStack itemstack);

    @Override
    public RedstoneControlMode getRedstoneControlMode() {
        return redstoneControlMode;
    }

    @Override
    public void setRedstoneControlMode(RedstoneControlMode redstoneControlMode) {
        this.redstoneControlMode = redstoneControlMode;
        redstoneStateDirty = true;
        updateBlock();
    }

    public short getFacing() {
        return facing;
    }

    public ForgeDirection getFacingDir() {
        return ForgeDirection.getOrientation(facing);
    }

    public void setFacing(short facing) {
        this.facing = facing;
    }

    public abstract boolean isActive();

    // --- Process Loop
    // --------------------------------------------------------------------------

    @Override
    public void doUpdate() {
        if (worldObj.isRemote) {
            updateEntityClient();
            return;
        } // else is server, do all logic only on the server

        boolean requiresClientSync = forceClientUpdate;
        boolean prevRedCheck = redstoneCheckPassed;
        if (redstoneStateDirty) {
            redstoneCheckPassed = RedstoneControlMode.isConditionMet(redstoneControlMode, this);
            redstoneStateDirty = false;
        }

        if (shouldDoWorkThisTick(5)) {
            requiresClientSync |= doSideIo();
        }

        requiresClientSync |= prevRedCheck != redstoneCheckPassed;

        requiresClientSync |= processTasks(redstoneCheckPassed);

        if (requiresClientSync) {

            // this will cause 'getPacketDescription()' to be called and its result
            // will be sent to the PacketHandler on the other end of
            // client/server connection
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            // And this will make sure our current tile entity state is saved
            markDirty();
        }

        if (notifyNeighbours) {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
            notifyNeighbours = false;
        }
    }

    protected void updateEntityClient() {
        if (isActive() != lastActive) {
            ticksSinceActiveChanged++;
            if (ticksSinceActiveChanged > 20 || isActive()) {
                ticksSinceActiveChanged = 0;
                lastActive = isActive();
                forceClientUpdate = true;
            }
        }

        if (forceClientUpdate) {
            if (worldObj.rand.nextInt(1024) <= (isDirty ? 256 : 0)) {
                isDirty = !isDirty;
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            forceClientUpdate = false;
        }
    }

    protected boolean doSideIo() {
        if (faceModes == null) {
            return false;
        }

        boolean res = false;
        Set<Entry<ForgeDirection, IoMode>> ents = faceModes.entrySet();
        for (Entry<ForgeDirection, IoMode> ent : ents) {
            IoMode mode = ent.getValue();
            if (mode.pulls()) {
                res = res | doPull(ent.getKey());
            }
            if (mode.pushes()) {
                res = res | doPush(ent.getKey());
            }
        }
        return res;
    }

    protected boolean doPush(ForgeDirection dir) {

        if (slotDefinition.getNumOutputSlots() <= 0) {
            return false;
        }
        if (!shouldDoWorkThisTick(20)) {
            return false;
        }

        BlockCoord loc = getLocation().getLocation(dir);
        TileEntity te = worldObj.getTileEntity(loc.x, loc.y, loc.z);

        return doPush(dir, te, slotDefinition.minOutputSlot, slotDefinition.maxOutputSlot);
    }

    protected boolean doPush(ForgeDirection dir, TileEntity te, int minSlot, int maxSlot) {
        if (te == null) {
            return false;
        }

        for (int i = minSlot; i <= maxSlot; i++) {
            ItemStack item = inv.getStackInSlot(i);
            System.out.println(item);
            System.out.println(i);
            if (item != null) {
                int num = ItemUtil.doInsertItem(te, item, dir.getOpposite());
                if (num > 0) {
                    item.stackSize -= num;
                    if (item.stackSize <= 0) {
                        item = null;
                    }
                    inv.setStackInSlot(i, item);
                    markDirty();
                }
            }
        }
        return false;
    }

    protected boolean doPull(ForgeDirection dir) {
        if (slotDefinition.getNumInputSlots() <= 0) {
            return false;
        }
        if (!shouldDoWorkThisTick(20)) {
            return false;
        }

        boolean hasSpace = false;
        for (int slot = slotDefinition.minInputSlot; slot <= slotDefinition.maxInputSlot && !hasSpace; slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            hasSpace = (stack == null
                || stack.stackSize < Math.min(stack.getMaxStackSize(), getInventoryStackLimit(slot)));
        }

        if (!hasSpace) {
            return false;
        }

        BlockCoord loc = getLocation().getLocation(dir);
        TileEntity te = worldObj.getTileEntity(loc.x, loc.y, loc.z);
        if (te == null) {
            return false;
        }

        if (!(te instanceof IInventory)) {
            return false;
        }

        ISidedInventory target = (te instanceof ISidedInventory) ? (ISidedInventory) te
            : new InventoryWrapper((IInventory) te);

        int[] targetSlots = target.getAccessibleSlotsFromSide(
            dir.getOpposite()
                .ordinal());
        if (targetSlots == null) {
            return false;
        }

        for (int inputSlot = slotDefinition.minInputSlot; inputSlot <= slotDefinition.maxInputSlot; inputSlot++) {
            if (doPull(inputSlot, target, targetSlots, dir)) {
                return true;
            }
        }

        return false;
    }

    protected boolean doPull(int inputSlot, ISidedInventory target, int[] targetSlots, ForgeDirection side) {
        for (int i = 0; i < targetSlots.length; i++) {
            int tSlot = targetSlots[i];
            ItemStack targetStack = target.getStackInSlot(tSlot);

            if (targetStack != null && target.canExtractItem(
                tSlot,
                targetStack,
                side.getOpposite()
                    .ordinal())) {
                ItemStack copy = targetStack.copy();
                int inserted = ItemUtil.doInsertItem(this, copy, side);

                if (inserted > 0) {
                    targetStack.stackSize -= inserted;
                    if (targetStack.stackSize <= 0) {
                        target.setInventorySlotContents(tSlot, null);
                    } else {
                        target.setInventorySlotContents(tSlot, targetStack);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract boolean processTasks(boolean redstoneCheckPassed);

    // ---- Tile Entity
    // ------------------------------------------------------------------------------

    @Override
    public void invalidate() {
        super.invalidate();
        if (worldObj.isRemote) {
            // updateSound();
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtRoot) {
        setFacing(nbtRoot.getShort("facing"));
        forceClientUpdate = nbtRoot.getBoolean("forceClientUpdate");
        readCommon(nbtRoot);
    }

    public void readCommon(NBTTagCompound nbtRoot) {

        this.inv.deserializeNBT(nbtRoot.getCompoundTag("item_inv"));

        int rsContr = nbtRoot.getInteger("redstoneControlMode");
        if (rsContr < 0 || rsContr >= RedstoneControlMode.values().length) {
            rsContr = 0;
        }
        redstoneControlMode = RedstoneControlMode.values()[rsContr];

        inverted = nbtRoot.getBoolean("mInverted");
        if (nbtRoot.hasKey("hasFaces")) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (nbtRoot.hasKey("face" + dir.ordinal())) {
                    setIoMode(dir, IoMode.values()[nbtRoot.getShort("face" + dir.ordinal())]);
                }
            }
        }
    }

    public void readFromItemStack(ItemStack stack) {
        if (stack == null || stack.stackTagCompound == null) {
            return;
        }
        readCommon(stack.stackTagCompound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtRoot) {

        nbtRoot.setShort("facing", facing);
        nbtRoot.setBoolean("redstoneCheckPassed", redstoneCheckPassed);
        nbtRoot.setBoolean("forceClientUpdate", forceClientUpdate);
        forceClientUpdate = false;

        writeCommon(nbtRoot);
    }

    public void writeCommon(NBTTagCompound nbtRoot) {

        nbtRoot.setTag("item_inv", this.inv.serializeNBT());

        nbtRoot.setInteger("redstoneControlMode", redstoneControlMode.ordinal());

        nbtRoot.setBoolean("mInverted", inverted);
        if (faceModes != null) {
            nbtRoot.setByte("hasFaces", (byte) 1);
            for (Entry<ForgeDirection, IoMode> e : faceModes.entrySet()) {
                nbtRoot.setShort(
                    "face" + e.getKey()
                        .ordinal(),
                    (short) e.getValue()
                        .ordinal());
            }
        }
    }

    public void writeToItemStack(ItemStack stack) {
        if (stack == null) {
            return;
        }
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }

        NBTTagCompound root = stack.stackTagCompound;
        root.setBoolean("eio.abstractMachine", true);
        writeCommon(root);

        String name;
        if (stack.hasDisplayName()) {
            name = stack.getDisplayName();
        } else {
            name = LibMisc.lang.localizeExact(stack.getUnlocalizedName() + ".name");
        }
        name += " " + LibMisc.lang.localize("machine.tooltip.configured");
        stack.setStackDisplayName(name);
    }

    // ---- Inventory

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return canPlayerAccess(player);
    }

    @Override
    public int getSizeInventory() {
        return slotDefinition.getNumSlots();
    }

    public int getInventoryStackLimit(int slot) {
        return getInventoryStackLimit();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= inv.getSlots()) {
            return null;
        }
        return inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int fromSlot, int amount) {
        ItemStack fromStack = inv.getStackInSlot(fromSlot);
        if (fromStack == null) {
            return null;
        }
        if (fromStack.stackSize <= amount) {
            inv.setStackInSlot(fromSlot, null);
            return fromStack;
        }
        ItemStack result = fromStack.splitStack(amount);
        inv.setStackInSlot(fromSlot, fromStack.stackSize > 0 ? fromStack : null);
        return result;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack contents) {
        if (contents == null) {
            inv.setStackInSlot(slot, null);
        } else {
            inv.setStackInSlot(slot, contents.copy());
        }

        ItemStack stack = inv.getStackInSlot(slot);
        if (stack != null && stack.stackSize > getInventoryStackLimit(slot)) {
            stack.stackSize = getInventoryStackLimit(slot);
            inv.setStackInSlot(slot, stack);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public String getInventoryName() {
        return getMachineName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if (isSideDisabled(var1)) {
            return new int[0];
        }
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        if (isSideDisabled(side) || !slotDefinition.isInputSlot(slot)) {
            return false;
        }
        ItemStack existing = inv.getStackInSlot(slot);
        if (existing != null) {
            // no point in checking the recipes if an item is already in the slot
            // worst case we get more of the wrong item - but that doesn't change anything
            return existing.isStackable() && existing.isItemEqual(itemstack);
        }
        // no need to call isItemValidForSlot as upgrade slots are not input slots
        return isMachineItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        if (isSideDisabled(side)) {
            return false;
        }
        if (!slotDefinition.isOutputSlot(slot)) {
            return false;
        }
        return canExtractItem(slot, itemstack);
    }

    protected boolean canExtractItem(int slot, ItemStack itemstack) {
        ItemStack existing = inv.getStackInSlot(slot);
        if (existing == null || existing.stackSize < itemstack.stackSize) {
            return false;
        }
        return itemstack.getItem() == existing.getItem();
    }

    public boolean isSideDisabled(int var1) {
        ForgeDirection dir = ForgeDirection.getOrientation(var1);
        IoMode mode = getIoMode(dir);
        if (mode == IoMode.DISABLED) {
            return true;
        }
        return false;
    }

    public void onNeighborBlockChange(Block blockId) {
        redstoneStateDirty = true;
    }

    /* IRedstoneConnectable */

    @Override
    public boolean shouldRedstoneConduitConnect(World world, int x, int y, int z, ForgeDirection from) {
        return true;
    }

    // ---- ModularUI2

    public static int[] createSlotRange(int minSlot, int maxSlot) {
        if (minSlot < 0 || maxSlot < minSlot) {
            return new int[0]; // không có slot hợp lệ
        }
        int[] result = new int[maxSlot - minSlot + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = minSlot + i;
        }
        return result;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        ModularPanel panel = ModularPanel.defaultPanel("test_tile");
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(() -> inverted, val -> inverted = val);
        IPanelHandler panelSyncHandler = syncManager.panel("other_panel", this::configPanel, true);

        panel.flex()
            .align(Alignment.Center);

        panel.child(
            new Column().debugName("Settings")
                .coverChildren()
                .leftRel(0.96f)
                .topRel(0.05f)
                .childPadding(2)
                .padding(1)
                .child(
                    new ToggleButton().size(16, 16)
                        .value(invertedSyncer)
                        .overlay(true, MGuiTextures.BUTTON_REDSTONE_ON)
                        .overlay(false, MGuiTextures.BUTTON_REDSTONE_OFF)
                        .selectedBackground(GuiTextures.MC_BUTTON)
                        .selectedHoverBackground(GuiTextures.MC_BUTTON_HOVERED)
                        .tooltip(richTooltip -> {
                            richTooltip.showUpTimer(2);
                            richTooltip.addLine(IKey.str("Redstone Mode"));
                        }))
                .child(
                    new ButtonWidget<>().size(16, 16)
                        .overlay(GuiTextures.GEAR)
                        .tooltip(richTooltip -> {
                            richTooltip.showUpTimer(2);
                            richTooltip.addLine(IKey.str("Configure"));
                        })
                        .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                this.clearAllIoModes();
                            }
                        }))
                        .onMousePressed(mouseButton -> {
                            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                if (panelSyncHandler.isPanelOpen()) {
                                    panelSyncHandler.closePanel();
                                } else {
                                    panelSyncHandler.openPanel();
                                }
                                return true;
                            }
                            return false;
                        })));

        panel.bindPlayerInventory();

        return panel;
    }

    public ModularPanel configPanel(PanelSyncManager syncManager, IPanelHandler syncHandler) {
        ModularPanel panel = new Dialog<>("second_window", null).setDisablePanelsBelow(false)
            .setCloseOnOutOfBoundsClick(false)
            .setDraggable(true)
            .size(64, 64);

        Flow column = new Column().debugName("Side Configs")
            .padding(5)
            .coverChildren();

        SlotGroupWidget.Builder group = SlotGroupWidget.builder()
            .matrix(" U ", "ENW", " DS");

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            char key = dir.name()
                .charAt(0); // U, D, N, S, E, W
            group.key(
                key,
                index -> new ButtonWidget<>().syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        this.setIoMode(dir, IoMode.NONE);
                    } else {
                        this.toggleIoModeForFace(dir);
                    }
                }))
                    .overlay(IKey.dynamic(() -> getIoMode(dir).getUnlocalisedName())));
        }

        column.child(group.build());
        panel.child(column);
        panel.child(ButtonWidget.panelCloseButton());
        return panel;
    }

}
