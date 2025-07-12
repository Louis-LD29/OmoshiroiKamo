package com.louis.test.common.block.machine;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.InventoryWrapper;
import com.enderio.core.common.util.ItemUtil;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.api.io.IIoConfigurable;
import com.louis.test.api.io.IoMode;
import com.louis.test.api.io.IoType;
import com.louis.test.api.material.MaterialEntry;
import com.louis.test.api.redstone.IRedstoneConnectable;
import com.louis.test.client.gui.modularui2.MGuis;
import com.louis.test.common.block.AbstractTE;
import crazypants.enderio.machine.IRedstoneModeControlable;
import crazypants.enderio.machine.RedstoneControlMode;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractMachineEntity extends AbstractTE implements IGuiHolder<PosGuiData>, ISidedInventory,
    IRedstoneModeControlable, IRedstoneConnectable, IIoConfigurable {

    protected final SlotDefinition slotDefinition;
    private final int[] allSlots;
    public ItemStackHandler inv;
    public MaterialEntry material;
    public boolean redstoneStateDirty = true;
    public boolean isDirty = false;
    protected int ticksSinceSync = -1;
    protected boolean forceClientUpdate = true;
    protected boolean lastActive;
    protected int ticksSinceActiveChanged = 0;
    protected boolean notifyNeighbours = false;
    protected SmartTank[] fluidTanks;
    protected RedstoneControlMode redstoneControlMode;
    protected Map<IoType, Map<ForgeDirection, IoMode>> ioConfigs;

    protected AbstractMachineEntity(SlotDefinition slotDefinition, MaterialEntry material) {
        this.slotDefinition = slotDefinition;
        this.material = material;

        inv = new ItemStackHandler(slotDefinition.getNumSlots());
        fluidTanks = new SmartTank[slotDefinition.getNumFluidSlots()];
        for (int i = 0; i < fluidTanks.length; i++) {
            fluidTanks[i] = new SmartTank(material);
        }

        redstoneControlMode = RedstoneControlMode.IGNORE;

        allSlots = new int[inv.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }

    }

    // IoType - IoMode
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
    public void clearAllIoModes(IoType type) {
        if (ioConfigs != null && ioConfigs.containsKey(type)) {
            ioConfigs.remove(type);
            forceClientUpdate = true;
            notifyNeighbours = true;
            updateBlock();
        }
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

    @Override
    public final boolean isItemValidForSlot(int i, ItemStack itemstack) {
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

    // --- Process Loop
    // --------------------------------------------------------------------------

    @Override
    public void doUpdate() {
        if (!isServerSide()) {
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

    @Override
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

    // ---- Inventory
    // ------------------------------------------------------------------------------

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
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

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
        IoMode mode = getIoMode(dir, IoType.ITEM);
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

    // ---- IRedstoneConnectable
    // ------------------------------------------------------------------------------

    @Override
    public boolean shouldRedstoneConduitConnect(World world, int x, int y, int z, ForgeDirection from) {
        return true;
    }

    // ---- ModularUI2
    // ------------------------------------------------------------------------------

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .build();
    }

}
