package louis.omoshiroikamo.common.block.anvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.AbstractTE;

public class TEAnvil extends AbstractTE implements ISidedInventory {

    public ItemStackHandler inv;
    private final int[] allSlots;

    public TEAnvil() {
        inv = new ItemStackHandler(9);
        allSlots = new int[inv.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockAnvil.unlocalisedName;
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
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        if (world.isRemote) return true;

        ItemStack held = player.getHeldItem();
        boolean isSneaking = player.isSneaking();

        if (held == null && isSneaking) {
            boolean didAnything = false;

            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        player.dropPlayerItemWithRandomChoice(stack, false);
                    }
                    inv.setStackInSlot(i, null);
                    didAnything = true;
                }
            }

            if (didAnything) {
                markDirty();
                world.markBlockForUpdate(xCoord, yCoord, zCoord);
                player.inventoryContainer.detectAndSendChanges();
            }
            return didAnything;
        }

        if (held == null) {
            for (int i = 1; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null) {
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        player.dropPlayerItemWithRandomChoice(stack, false);
                    }
                    inv.setStackInSlot(i, null);
                    markDirty();
                    world.markBlockForUpdate(xCoord, yCoord, zCoord);
                    player.inventoryContainer.detectAndSendChanges();
                    return true;
                }
            }

            ItemStack input = inv.getStackInSlot(0);
            if (input != null) {
                if (!player.inventory.addItemStackToInventory(input)) {
                    player.dropPlayerItemWithRandomChoice(input, false);
                }
                inv.setStackInSlot(0, null);
                markDirty();
                world.markBlockForUpdate(xCoord, yCoord, zCoord);
                player.inventoryContainer.detectAndSendChanges();
                return true;
            }

            return false;
        }

        ItemStack existing = inv.getStackInSlot(0);
        int maxStackSize = held.getMaxStackSize();

        if (canInsertItem(0, held, side.ordinal())) {
            if (existing == null) {
                int insertCount = Math.min(held.stackSize, maxStackSize);
                ItemStack insert = held.copy();
                insert.stackSize = insertCount;
                inv.setStackInSlot(0, insert);
                held.stackSize -= insertCount;
            } else if (existing.isItemEqual(held) && ItemStack.areItemStackTagsEqual(existing, held)) {
                int canAdd = Math.min(held.stackSize, maxStackSize - existing.stackSize);
                if (canAdd <= 0) return false;

                existing.stackSize += canAdd;
                inv.setStackInSlot(0, existing);
                held.stackSize -= canAdd;
            } else {
                return false; // Không thể chồng
            }

            // Cập nhật inventory người chơi
            if (held.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            } else {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, held);
            }

            markDirty();
            world.markBlockForUpdate(xCoord, yCoord, zCoord);
            player.inventoryContainer.detectAndSendChanges(); // cập nhật client
            return true;
        }

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
    public int[] getAccessibleSlotsFromSide(int slot) {
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
        if (slot != 0) return false;
        ItemStack existing = inv.getStackInSlot(slot);
        if (existing != null) {
            return existing.isStackable() && existing.isItemEqual(itemstack);
        }
        return isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        if (slot < 1) {
            return false;
        }
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
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }
}
