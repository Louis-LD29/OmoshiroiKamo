package louis.omoshiroikamo.common.block.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.common.block.basicblock.machine.IProgressTile;
import louis.omoshiroikamo.common.block.basicblock.machine.PoweredTask;
import louis.omoshiroikamo.common.block.basicblock.machine.PoweredTaskProgress;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.TEFluidInput;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.TEFluidOutput;
import louis.omoshiroikamo.common.block.multiblock.part.item.TEItemInput;
import louis.omoshiroikamo.common.block.multiblock.part.item.TEItemOutput;
import louis.omoshiroikamo.common.core.helper.Logger;
import louis.omoshiroikamo.common.core.helper.OreDictUtils;
import louis.omoshiroikamo.common.recipes.IPoweredTask;
import louis.omoshiroikamo.common.recipes.MachineRecipe;
import louis.omoshiroikamo.common.recipes.MachineRecipeRegistry;
import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public abstract class AbstractMultiBlockProcessing<T extends AbstractMultiBlockProcessing<T>>
    extends AbstractMultiBlockEntity<T> implements IProgressTile {

    protected final Random random = new Random();
    protected IPoweredTask currentTask = null;
    protected MachineRecipe lastCompletedRecipe;
    protected MachineRecipe cachedNextRecipe;
    protected int ticksSinceCheckedRecipe = 0;
    protected boolean startFailed = false;
    protected float nextChance = Float.NaN;

    protected boolean confirmedToStart = false;
    protected MachineRecipe lockedRecipe = null;

    @Override
    public boolean isActive() {
        return currentTask == null ? false : currentTask.getProgress() >= 0 && redstoneCheckPassed;
    }

    public float getProgress() {
        return currentTask == null ? -1 : currentTask.getProgress();
    }

    @Override
    public void setProgress(float progress) {
        this.currentTask = progress < 0 ? null : new PoweredTaskProgress(progress);
    }

    public IPoweredTask getCurrentTask() {
        return currentTask;
    }

    public boolean getRedstoneChecksPassed() {
        return redstoneCheckPassed;
    }

    @Override
    protected boolean processTasks(boolean redstoneChecksPassed) {

        if (!redstoneChecksPassed) {
            return false;
        }

        boolean requiresClientSync = false;
        // Process any current items
        requiresClientSync |= checkProgress(redstoneChecksPassed);

        if (currentTask != null || !hasValidInputsForRecipe()) {
            return requiresClientSync;
        }

        if (startFailed) {
            ticksSinceCheckedRecipe++;
            if (ticksSinceCheckedRecipe < 20) {
                return false;
            }
        }
        ticksSinceCheckedRecipe = 0;

        // Get a new chance when we don't have one yet
        // If a recipe could not be started we will try with the same chance next time
        if (Float.isNaN(nextChance)) {
            nextChance = random.nextFloat();
        }

        // Then see if we need to start a new one
        MachineRecipe nextRecipe = canStartNextTask(nextChance);
        if (nextRecipe != null) {
            if (!confirmedToStart) {
                return requiresClientSync;
            }
            boolean started = startNextTask(nextRecipe, nextChance);
            if (started) {
                // this chance value has been used up
                nextChance = Float.NaN;
            }
            startFailed = !started;
        } else {
            startFailed = true;
        }
        sendTaskProgressPacket();

        return requiresClientSync;
    }

    protected boolean checkProgress(boolean redstoneChecksPassed) {
        if (currentTask == null) {
            return false;
        }
        if (redstoneChecksPassed && !currentTask.isComplete()) {
            usePower();
        }
        // then check if we are done
        if (currentTask.isComplete()) {
            taskComplete();
            return false;
        }

        return false;
    }

    protected double usePower() {
        return usePower(0);
    }

    public int usePower(int wantToUse) {

        return wantToUse;
    }

    protected void taskComplete() {
        if (currentTask != null) {
            lastCompletedRecipe = currentTask.getRecipe();
            List<ChanceItemStack> itemOutputs = currentTask.getItemOutputs();
            List<ChanceFluidStack> fluidOutputs = currentTask.getFluidOutputs();
            mergeResults(itemOutputs, fluidOutputs);
        }
        markDirty();
        currentTask = null;
        lastProgressScaled = 0;
    }

    protected void mergeResults(List<ChanceItemStack> itemStacks, List<ChanceFluidStack> fluidStacks) {
        // Merge ItemStack vào các output inventory
        for (ChanceItemStack output : itemStacks) {
            if (output == null) continue;

            ItemStack remaining = output.stack.copy();

            outer: for (TEItemOutput io : mItemOutput) {
                ItemStackHandler handler = io.getInv();
                if (handler == null) continue;

                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack current = handler.getStackInSlot(i);
                    if (current != null && ItemStack.areItemStacksEqual(current, remaining)
                        && ItemStack.areItemStackTagsEqual(current, remaining)) {

                        int canInsert = Math.min(remaining.stackSize, current.getMaxStackSize() - current.stackSize);
                        if (canInsert > 0) {
                            current.stackSize += canInsert;
                            handler.setStackInSlot(i, current);
                            remaining.stackSize -= canInsert;

                            if (remaining.stackSize <= 0) break outer;
                        }
                    }
                }
            }

            // Vòng 2: tìm slot trống
            outer: for (TEItemOutput io : mItemOutput) {
                ItemStackHandler handler = io.getInv();
                if (handler == null) continue;

                for (int i = 0; i < handler.getSlots(); i++) {
                    if (handler.getStackInSlot(i) == null) {
                        handler.setStackInSlot(i, remaining);
                        remaining = null;
                        break outer;
                    }
                }
            }

            if (remaining != null && remaining.stackSize > 0) {}
        }

        for (ChanceFluidStack output : fluidStacks) {
            if (output == null) continue;

            int remaining = output.stack.amount;

            outer: for (TEFluidOutput fo : mFluidOutput) {
                SmartTank[] tanks = fo.getTanks();
                if (tanks == null) continue;

                for (SmartTank tank : tanks) {
                    if (tank == null || tank.getFluid() == null) continue;

                    if (tank.getFluid()
                        .isFluidEqual(output.stack)) {
                        int filled = tank.fill(new FluidStack(output.stack.getFluid(), remaining), true);
                        remaining -= filled;
                        if (remaining <= 0) break outer;
                    }
                }
            }

            outer: for (TEFluidOutput fo : mFluidOutput) {
                SmartTank[] tanks = fo.getTanks();
                if (tanks == null) continue;

                for (SmartTank tank : tanks) {
                    if (tank == null || tank.getFluidAmount() > 0) continue;

                    int filled = tank.fill(new FluidStack(output.stack.getFluid(), remaining), true);
                    remaining -= filled;
                    if (remaining <= 0) break outer;
                }
            }

            if (remaining > 0) {}
        }

        cachedNextRecipe = null;
    }

    protected MachineRecipe getNextRecipe() {
        if (lockedRecipe != null) {
            return lockedRecipe; // Prioritize locked recipe
        }
        if (cachedNextRecipe == null) {
            cachedNextRecipe = MachineRecipeRegistry
                .findMatchingRecipe(getMachineName(), getItemInput(), getFluidInput());
        }
        return cachedNextRecipe;
    }

    protected MachineRecipe canStartNextTask(float chance) {
        MachineRecipe nextRecipe = getNextRecipe();
        if (nextRecipe == null) {
            return null;
        }
        return canOutput(nextRecipe) ? nextRecipe : null;
    }

    private boolean canOutput(MachineRecipe recipe) {
        // Kiểm tra item outputs
        for (ChanceItemStack out : recipe.getItemOutputs()) {
            if (out == null || out.stack.stackSize <= 0) continue;

            boolean canInsert = false;

            for (TEItemOutput output : mItemOutput) {
                ItemStackHandler inv = output.getInv();
                if (inv == null) continue;

                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack slot = inv.getStackInSlot(i);
                    if (slot == null || (ItemStack.areItemStacksEqual(slot, out.stack)
                        && slot.stackSize + out.stack.stackSize <= slot.getMaxStackSize())) {
                        canInsert = true;
                        break;
                    }
                }

                if (canInsert) break;
            }

            if (!canInsert) return false;
        }

        for (ChanceFluidStack out : recipe.getFluidOutputs()) {
            if (out == null || out.stack.amount <= 0) continue;
            boolean canInsert = false;

            for (TEFluidOutput output : mFluidOutput) {
                SmartTank[] tanks = output.getTanks();
                if (tanks == null) continue;

                for (SmartTank tank : tanks) {
                    if (tank.fill(out.stack, false) == out.stack.amount) {
                        canInsert = true;
                        break;
                    }
                }

                if (canInsert) break;
            }

            if (!canInsert) return false;
        }

        return true;
    }

    private void consumeInputs(MachineRecipe recipe) {
        // Tiêu thụ item inputs
        for (ChanceItemStack input : recipe.getItemInputs()) {
            if (input == null || input.stack.stackSize <= 0) continue;

            int remaining = input.stack.stackSize;

            for (TEItemInput inputTile : mItemInput) {
                ItemStackHandler inv = inputTile.getInv();
                if (inv == null) continue;

                for (int i = 0; i < inv.getSlots() && remaining > 0; i++) {
                    ItemStack stackInSlot = inv.getStackInSlot(i);
                    if (stackInSlot != null && stackInSlot.stackSize > 0
                        && OreDictUtils.isOreDictMatch(stackInSlot, input.stack)) {

                        int consumed = Math.min(remaining, stackInSlot.stackSize);
                        stackInSlot.stackSize -= consumed;
                        remaining -= consumed;

                        if (stackInSlot.stackSize <= 0) {
                            inv.setStackInSlot(i, null);
                        } else {
                            inv.setStackInSlot(i, stackInSlot);
                        }
                    }
                }

                if (remaining <= 0) break;
            }

            if (remaining > 0) {}
        }

        // Tiêu thụ fluid inputs
        for (ChanceFluidStack input : recipe.getFluidInputs()) {
            if (input == null || input.stack.amount <= 0) continue;

            int remaining = input.stack.amount;

            for (TEFluidInput inputTile : mFluidInput) {
                SmartTank[] tanks = inputTile.getTanks();
                if (tanks == null) continue;

                for (SmartTank tank : tanks) {
                    FluidStack fluidInTank = tank.getFluid();
                    if (fluidInTank != null && fluidInTank.isFluidEqual(input.stack)) {
                        int drained = Math.min(remaining, fluidInTank.amount);
                        tank.drain(drained, true);
                        remaining -= drained;

                        if (remaining <= 0) break;
                    }
                }

                if (remaining <= 0) break;
            }

            if (remaining > 0) {
                Logger.info(
                    "[consumeInputs] Không đủ fluid để tiêu thụ: " + input.stack.amount
                        + "L of "
                        + input.stack.getFluid()
                            .getName());
            }
        }
    }

    protected boolean startNextTask(MachineRecipe nextRecipe, float chance) {
        if (lockedRecipe != null && !lockedRecipe.equals(nextRecipe)) {
            return false;
        }

        if (lockedRecipe != null || nextRecipe
            == MachineRecipeRegistry.findMatchingRecipe(getMachineName(), getItemInput(), getFluidInput())) {
            consumeInputs(nextRecipe);
            currentTask = createTask(nextRecipe, chance);
            return true;
        }
        return false;
    }

    protected IPoweredTask createTask(MachineRecipe nextRecipe, float chance) {
        return new PoweredTask(nextRecipe, chance, getItemInput(), getFluidInput());
    }

    protected IPoweredTask createTask(NBTTagCompound taskTagCompound) {
        return PoweredTask.readFromNBT(taskTagCompound);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtRoot) {
        super.readCustomNBT(nbtRoot);
        currentTask = nbtRoot.hasKey("currentTask") ? createTask(nbtRoot.getCompoundTag("currentTask")) : null;
        String uid = nbtRoot.getString("lastCompletedRecipe");
        lastCompletedRecipe = MachineRecipeRegistry.getRecipeForUid(uid);

    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtRoot) {
        super.writeCustomNBT(nbtRoot);
        if (currentTask != null) {
            NBTTagCompound currentTaskNBT = new NBTTagCompound();
            currentTask.writeToNBT(currentTaskNBT);
            nbtRoot.setTag("currentTask", currentTaskNBT);
        }
        if (lastCompletedRecipe != null) {
            nbtRoot.setString("lastCompletedRecipe", lastCompletedRecipe.getUid());
        }
        nbtRoot.setBoolean("confirmedToStart", confirmedToStart);
        if (lockedRecipe != null) {
            nbtRoot.setString("lockedRecipe", lockedRecipe.getUid());
        }
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        cachedNextRecipe = null;
        confirmedToStart = nbtRoot.getBoolean("confirmedToStart");
        if (nbtRoot.hasKey("lockedRecipe")) {
            lockedRecipe = MachineRecipeRegistry.getRecipeForUid(nbtRoot.getString("lockedRecipe"));
        }
    }

    protected boolean hasValidInputsForRecipe() {
        MachineRecipe recipe = MachineRecipeRegistry
            .findMatchingRecipe(getMachineName(), getItemInput(), getFluidInput());
        return recipe != null;
    }

    public MachineRecipe getPredictedRecipe() {
        return MachineRecipeRegistry.findMatchingRecipe(getMachineName(), getItemInput(), getFluidInput());
    }

    public List<ItemStack> getItemOutput() {
        MachineRecipe recipe = isLocked() ? lockedRecipe : getPredictedRecipe();
        if (recipe == null) return Collections.emptyList();

        List<ItemStack> result = new ArrayList<>();
        for (ChanceItemStack is : recipe.getItemOutputs()) {
            result.add(is != null ? OreDictUtils.getOreDictRepresentative(is.stack) : null);
        }
        return result;
    }

    public List<FluidStack> getFluidOutput() {
        MachineRecipe recipe = isLocked() ? lockedRecipe : getPredictedRecipe();
        if (recipe == null) return Collections.emptyList();

        List<FluidStack> result = new ArrayList<>();
        for (ChanceFluidStack fs : recipe.getFluidOutputs()) {
            result.add(fs != null ? fs.stack.copy() : null);
        }
        return result;
    }

    public void confirmRecipe(MachineRecipe recipe) {
        this.lockedRecipe = recipe;
        this.confirmedToStart = true;
    }

    public void unlockRecipe() {
        this.lockedRecipe = null;
        this.confirmedToStart = false;
    }

    public boolean isLocked() {
        return this.lockedRecipe != null;
    }

    public boolean isRecipeLocked() {
        return this.lockedRecipe != null;
    }

    public void setRecipeLocked(boolean value) {
        if (value) {
            MachineRecipe predicted = getPredictedRecipe();
            if (predicted != null) {
                confirmRecipe(predicted); // lockedRecipe = predicted + confirmedToStart = true
            }
        } else {
            unlockRecipe(); // lockedRecipe = null, confirmedToStart = false
        }
    }

}
