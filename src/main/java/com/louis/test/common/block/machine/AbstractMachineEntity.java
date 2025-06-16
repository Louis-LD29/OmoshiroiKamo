package com.louis.test.common.block.machine;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.InventoryWrapper;
import com.enderio.core.common.util.ItemUtil;
import com.louis.test.api.enums.IoMode;
import com.louis.test.api.enums.IoType;
import com.louis.test.api.interfaces.IIoConfigurable;
import com.louis.test.api.interfaces.redstone.IRedstoneConnectable;
import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.TileEntityEio;
import com.louis.test.common.gui.modularui2.MGuis;
import com.louis.test.lib.LibMisc;

import crazypants.enderio.machine.IMachine;
import crazypants.enderio.machine.IRedstoneModeControlable;
import crazypants.enderio.machine.RedstoneControlMode;

public abstract class AbstractMachineEntity extends TileEntityEio implements IGuiHolder<PosGuiData>, ISidedInventory,
    IMachine, IRedstoneModeControlable, IRedstoneConnectable, IIoConfigurable {

    public short facing;

    protected int ticksSinceSync = -1;
    protected boolean forceClientUpdate = true;
    protected boolean lastActive;
    protected int ticksSinceActiveChanged = 0;

    public ItemStackHandler inv;
    protected SmartTank[] fluidTanks;
    protected final SlotDefinition slotDefinition;

    protected RedstoneControlMode redstoneControlMode;

    public boolean redstoneCheckPassed;

    public boolean redstoneStateDirty = true;

    protected Map<ForgeDirection, IoMode> faceModes;

    protected Map<IoType, Map<ForgeDirection, IoMode>> ioConfigs;

    private final int[] allSlots;

    protected boolean notifyNeighbours = false;

    public boolean isDirty = false;

    protected AbstractMachineEntity(SlotDefinition slotDefinition) {
        this.slotDefinition = slotDefinition;
        facing = 3;

        inv = new ItemStackHandler(slotDefinition.getNumSlots());
        fluidTanks = new SmartTank[slotDefinition.getNumFluidSlots()];
        for (int i = 0; i < fluidTanks.length; i++) {
            fluidTanks[i] = new SmartTank(8000);
        }
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
    public IoMode toggleIoModeForFace(ForgeDirection faceHit, IoType type) {
        IoMode curMode = getIoMode(faceHit, type);
        IoMode mode = curMode.next();
        while (!supportsMode(faceHit, mode)) {
            mode = mode.next();
        }
        setIoMode(faceHit, mode, type);
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
    public void setIoMode(ForgeDirection faceHit, IoMode mode, IoType type) {
        if (mode == IoMode.NONE && ioConfigs == null) {
            return;
        }

        if (ioConfigs == null) {
            ioConfigs = new EnumMap<IoType, Map<ForgeDirection, IoMode>>(IoType.class);
        }

        Map<ForgeDirection, IoMode> faceMap = ioConfigs.get(type);
        if (faceMap == null) {
            faceMap = new EnumMap<ForgeDirection, IoMode>(ForgeDirection.class);
            ioConfigs.put(type, faceMap);
        }

        faceMap.put(faceHit, mode);

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
    public void clearAllIoModes(IoType type) {
        if (ioConfigs != null && ioConfigs.containsKey(type)) {
            ioConfigs.remove(type);
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

    @Override
    public IoMode getIoMode(ForgeDirection face, IoType type) {
        if (ioConfigs == null) {
            return IoMode.NONE;
        }

        Map<ForgeDirection, IoMode> faceMap = ioConfigs.get(type);
        if (faceMap == null) {
            return IoMode.NONE;
        }

        IoMode res = faceMap.get(face);
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
        if (ioConfigs == null || ioConfigs.isEmpty()) {
            return false;
        }

        if (faceModes == null) {
            faceModes = new EnumMap<>(ForgeDirection.class);
        }

        boolean res = false;

        for (Map<ForgeDirection, IoMode> faceMap : ioConfigs.values()) {
            if (faceMap == null || faceMap.isEmpty()) continue;

            for (Map.Entry<ForgeDirection, IoMode> entry : faceMap.entrySet()) {
                ForgeDirection side = entry.getKey();
                IoMode mode = entry.getValue();

                if (mode.inputs()) {
                    res |= doPull(side);
                }
                if (mode.outputs()) {
                    res |= doPush(side);
                }
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

        return doPush(dir, te, slotDefinition.minItemInputSlot, slotDefinition.maxItemInputSlot);
    }

    protected boolean doPush(ForgeDirection dir, TileEntity te, int minSlot, int maxSlot) {
        if (te == null) {
            return false;
        }

        for (int i = minSlot; i <= maxSlot; i++) {
            ItemStack item = inv.getStackInSlot(i);
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
        for (int slot = slotDefinition.minItemInputSlot; slot <= slotDefinition.maxItemInputSlot && !hasSpace; slot++) {
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

        for (int inputSlot = slotDefinition.minItemInputSlot; inputSlot
            <= slotDefinition.maxItemInputSlot; inputSlot++) {
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
        redstoneCheckPassed = nbtRoot.getBoolean("redstoneCheckPassed");
        forceClientUpdate = nbtRoot.getBoolean("forceClientUpdate");
        readCommon(nbtRoot);
    }

    public void readCommon(NBTTagCompound nbtRoot) {

        this.inv.deserializeNBT(nbtRoot.getCompoundTag("item_inv"));

        NBTTagCompound tanksTag = nbtRoot.getCompoundTag("FluidTanks");
        for (int i = 0; i < fluidTanks.length; i++) {
            String key = "Tank" + i;
            if (tanksTag.hasKey(key)) {
                fluidTanks[i].readFromNBT(tanksTag.getCompoundTag(key));
            }
        }

        int rsContr = nbtRoot.getInteger("redstoneControlMode");
        if (rsContr < 0 || rsContr >= RedstoneControlMode.values().length) {
            rsContr = 0;
        }
        redstoneControlMode = RedstoneControlMode.values()[rsContr];

        if (nbtRoot.hasKey("hasFaces")) {
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (nbtRoot.hasKey("face" + dir.ordinal())) {
                    setIoMode(dir, IoMode.values()[nbtRoot.getShort("face" + dir.ordinal())]);
                }
            }
        }

        if (nbtRoot.hasKey("ioConfigs")) {
            NBTTagCompound ioConfigsTag = nbtRoot.getCompoundTag("ioConfigs");
            for (IoType type : IoType.values()) {
                if (ioConfigsTag.hasKey(type.name())) {
                    NBTTagCompound faceTag = ioConfigsTag.getCompoundTag(type.name());
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        String key = "face" + dir.ordinal();
                        if (faceTag.hasKey(key)) {
                            short modeId = faceTag.getShort(key);
                            IoMode mode = IoMode.values()[modeId];
                            setIoMode(dir, mode, type);
                        }
                    }
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

        NBTTagCompound tanksTag = new NBTTagCompound();
        for (int i = 0; i < fluidTanks.length; i++) {
            NBTTagCompound tankTag = new NBTTagCompound();
            fluidTanks[i].writeToNBT(tankTag);
            tanksTag.setTag("Tank" + i, tankTag);
        }
        nbtRoot.setTag("FluidTanks", tanksTag);

        nbtRoot.setInteger("redstoneControlMode", redstoneControlMode.ordinal());

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

        NBTTagCompound ioConfigsTag = new NBTTagCompound();
        if (ioConfigs != null && !ioConfigs.isEmpty()) {

            for (Map.Entry<IoType, Map<ForgeDirection, IoMode>> entry : ioConfigs.entrySet()) {
                IoType type = entry.getKey();
                Map<ForgeDirection, IoMode> faceMap = entry.getValue();

                if (faceMap != null && !faceMap.isEmpty()) {
                    NBTTagCompound faceTag = new NBTTagCompound();

                    for (Map.Entry<ForgeDirection, IoMode> faceEntry : faceMap.entrySet()) {
                        faceTag.setShort(
                            "face" + faceEntry.getKey()
                                .ordinal(),
                            (short) faceEntry.getValue()
                                .ordinal());
                    }

                    ioConfigsTag.setTag(type.name(), faceTag);
                }
            }

            nbtRoot.setTag("ioConfigs", ioConfigsTag);
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
    public int[] getAccessibleSlotsFromSide(int side) {
        if (isSideDisabled(side, IoType.ITEM)) {
            return new int[0];
        }
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        if (isSideDisabled(side, IoType.ITEM) || !slotDefinition.isInputSlot(slot)) {
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
        if (isSideDisabled(side, IoType.ITEM)) {
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

    public boolean isSideDisabled(int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        IoMode mode = getIoMode(dir);
        if (mode == IoMode.DISABLED) {
            return true;
        }
        return false;
    }

    public boolean isSideDisabled(int side, IoType type) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        IoMode mode = getIoMode(dir, type);
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
        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .build();
    }

}
