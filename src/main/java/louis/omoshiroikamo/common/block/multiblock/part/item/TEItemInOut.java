package louis.omoshiroikamo.common.block.multiblock.part.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import louis.omoshiroikamo.api.io.SlotDefinition;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.common.block.abstractClass.AbstractStorageTE;

public abstract class TEItemInOut extends AbstractStorageTE implements ISidedInventory {

    public ItemStackHandler inv;
    private final int[] allSlots;

    public TEItemInOut(MaterialEntry material) {
        super(new SlotDefinition(-1, -1), material);
        inv = new ItemStackHandler(material.getItemSlotCount());
        this.material = material;
        allSlots = new int[inv.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        root.setTag("item_inv", this.inv.serializeNBT());
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        this.inv.deserializeNBT(root.getCompoundTag("item_inv"));
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        ItemStack existing = inv.getStackInSlot(slot);
        if (existing != null) {
            return existing.isStackable() && existing.isItemEqual(itemstack);
        }
        return isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        ItemStack existing = inv.getStackInSlot(slot);
        if (existing == null || existing.stackSize < itemstack.stackSize) {
            return false;
        }
        return itemstack.getItem() == existing.getItem();
    }

    @Override
    public int getSizeInventory() {
        return inv.getSlots();
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
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack contents) {
        if (contents == null) {
            inv.setStackInSlot(slot, null);
        } else {
            inv.setStackInSlot(slot, contents.copy());
        }

        ItemStack stack = inv.getStackInSlot(slot);
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
            inv.setStackInSlot(slot, stack);
        }
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return canPlayerAccess(player);
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return isMachineItemValidForSlot(slot, stack);
    }

    protected abstract boolean isMachineItemValidForSlot(int slot, ItemStack itemstack);
}
