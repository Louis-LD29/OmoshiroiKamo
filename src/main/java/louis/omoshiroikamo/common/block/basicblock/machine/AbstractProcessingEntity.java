package louis.omoshiroikamo.common.block.basicblock.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import louis.omoshiroikamo.api.energy.IInternalPowerReceiver;
import louis.omoshiroikamo.api.io.IoMode;
import louis.omoshiroikamo.api.io.IoType;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.common.core.helper.Logger;
import louis.omoshiroikamo.common.core.helper.OreDictUtils;
import louis.omoshiroikamo.common.recipes.IPoweredTask;
import louis.omoshiroikamo.common.recipes.MachineRecipe;
import louis.omoshiroikamo.common.recipes.MachineRecipeRegistry;
import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public abstract class AbstractProcessingEntity extends AbstractPowerConsumerEntity
    implements IInternalPowerReceiver, IProgressTile {

    protected final Random random = new Random();
    protected IPoweredTask currentTask = null;
    protected MachineRecipe lastCompletedRecipe;
    protected MachineRecipe cachedNextRecipe;
    protected int ticksSinceCheckedRecipe = 0;
    protected boolean startFailed = false;
    protected float nextChance = Float.NaN;

    protected boolean confirmedToStart = false;
    protected MachineRecipe lockedRecipe = null;

    public AbstractProcessingEntity(SlotDefinition slotDefinition, MaterialEntry material) {
        super(slotDefinition, material);
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        ForgeDirection dir = ForgeDirection.getOrientation(var1);
        IoMode mode = getIoMode(dir, IoType.ITEM);
        if (mode == IoMode.DISABLED) {
            return new int[0];
        }

        int[] res = new int[inv.getSlots() - slotDefinition.getNumUpgradeSlots()];
        int index = 0;
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!slotDefinition.isUpgradeSlot(i)) {
                res[index] = i;
                index++;
            }
        }
        return res;
    }

    @Override
    public boolean isActive() {
        return currentTask == null ? false : currentTask.getProgress() >= 0 && redstoneCheckPassed;
    }

    @Override
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
        return usePower(getPowerUsePerTick());
    }

    public int usePower(int wantToUse) {
        int used = Math.min(getEnergyStored(), wantToUse);
        setEnergyStored(Math.max(0, getEnergyStored() - used));
        if (currentTask != null) {
            currentTask.update(used);
        }
        return used;
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
        List<ItemStack> outputStacks = new ArrayList<ItemStack>();
        for (int i = slotDefinition.minItemOutputSlot; i <= slotDefinition.maxItemOutputSlot; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            outputStacks.add(stack != null ? stack.copy() : null);
        }

        for (ChanceItemStack output : itemStacks) {
            if (output == null) continue;

            ItemStack copy = output.stack.copy();
            int remaining = copy.stackSize;

            // Merge vào stack đã có (ưu tiên oredict)
            for (int i = 0; i < outputStacks.size() && remaining > 0; i++) {
                ItemStack current = outputStacks.get(i);
                if (current != null && current.isItemEqual(copy) && ItemStack.areItemStackTagsEqual(current, copy)) {
                    int canMerge = Math.min(remaining, current.getMaxStackSize() - current.stackSize);
                    if (canMerge > 0) {
                        current.stackSize += canMerge;
                        remaining -= canMerge;
                        outputStacks.set(i, current);
                    }
                }
            }

            // Tìm slot trống
            for (int i = 0; i < outputStacks.size() && remaining > 0; i++) {
                if (outputStacks.get(i) == null) {
                    ItemStack newStack = copy.copy();
                    newStack.stackSize = remaining;
                    outputStacks.set(i, newStack);
                    remaining = 0;
                }
            }

            if (remaining > 0) {
                Logger.info("[mergeResults] Không đủ chỗ chứa ItemStack: " + copy);
            }
        }

        // Ghi lại vào inv
        int listIndex = 0;
        for (int i = slotDefinition.minItemOutputSlot; i <= slotDefinition.maxItemOutputSlot; i++) {
            inv.setStackInSlot(i, outputStacks.get(listIndex++));
        }

        for (ChanceFluidStack output : fluidStacks) {
            if (output == null) continue;

            int remaining = output.stack.amount;

            for (int i = slotDefinition.minFluidOutputSlot; i <= slotDefinition.maxFluidOutputSlot
                && remaining > 0; i++) {
                FluidStack current = fluidTanks[i].getFluid();
                if (current != null && current.isFluidEqual(output.stack)) {
                    int filled = fluidTanks[i].fill(new FluidStack(output.stack.getFluid(), remaining), true);
                    remaining -= filled;
                }
            }

            for (int i = slotDefinition.minFluidOutputSlot; i <= slotDefinition.maxFluidOutputSlot
                && remaining > 0; i++) {
                FluidStack current = fluidTanks[i].getFluid();
                if (current == null || current.amount == 0) {
                    int filled = fluidTanks[i].fill(new FluidStack(output.stack.getFluid(), remaining), true);
                    remaining -= filled;
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
                .findMatchingRecipe(getMachineName(), getItemInputs(), getFluidInputs());
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
            if (out == null) continue;
            boolean canInsert = false;

            for (int i = slotDefinition.minItemOutputSlot; i <= slotDefinition.maxItemOutputSlot; i++) {
                ItemStack target = inv.getStackInSlot(i);
                if (target == null || (ItemStack.areItemStacksEqual(target, out.stack)
                    && target.stackSize + out.stack.stackSize <= target.getMaxStackSize())) {
                    canInsert = true;
                    break;
                }
            }
            if (!canInsert) return false;
        }

        // Kiểm tra fluid outputs
        for (ChanceFluidStack out : recipe.getFluidOutputs()) {
            if (out == null) continue;
            boolean canInsert = false;

            for (int i = slotDefinition.minFluidOutputSlot; i <= slotDefinition.maxFluidOutputSlot; i++) {
                FluidTank tank = fluidTanks[i];
                if (tank.fill(out.stack, false) == out.stack.amount) {
                    canInsert = true;
                    break;
                }
            }

            if (!canInsert) return false;
        }

        return true;
    }

    private void consumeInputs(MachineRecipe recipe) {
        for (ChanceItemStack input : recipe.getItemInputs()) {
            int remaining = input.stack.stackSize;

            for (int i = slotDefinition.minItemInputSlot; i <= slotDefinition.maxItemInputSlot && remaining > 0; i++) {
                ItemStack target = inv.getStackInSlot(i);
                if (target == null) continue;

                boolean matches = OreDictUtils.isOreDictMatch(input.stack, target);

                if (matches) {
                    int consumed = Math.min(remaining, target.stackSize);
                    target.stackSize -= consumed;
                    remaining -= consumed;

                    // Nếu là slot input thật sự -> reset cached recipe
                    if (slotDefinition.isInputSlot(i)) {
                        cachedNextRecipe = null;
                    }

                    // Cập nhật lại slot
                    if (target.stackSize <= 0) {
                        inv.setStackInSlot(i, null);
                    } else {
                        inv.setStackInSlot(i, target);
                    }
                }
            }

            if (remaining > 0) {
                Logger.info("[consumeInputs] Không đủ item để tiêu thụ: " + input.stack.getDisplayName());
            }
        }

        // Tiêu thụ fluid inputs
        for (ChanceFluidStack input : recipe.getFluidInputs()) {
            int remaining = input.stack.amount;

            for (int i = slotDefinition.minFluidInputSlot; i <= slotDefinition.maxFluidInputSlot
                && remaining > 0; i++) {
                FluidTank tank = fluidTanks[i];
                FluidStack contained = tank.getFluid();

                if (contained != null && contained.isFluidEqual(input.stack)) {
                    int drained = Math.min(remaining, contained.amount);
                    tank.drain(drained, true);
                    remaining -= drained;
                }
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
            == MachineRecipeRegistry.findMatchingRecipe(getMachineName(), getItemInputs(), getFluidInputs())) {
            consumeInputs(nextRecipe);
            currentTask = createTask(nextRecipe, chance);
            return true;
        }
        return false;
    }

    protected IPoweredTask createTask(MachineRecipe nextRecipe, float chance) {
        return new PoweredTask(nextRecipe, chance, getItemInputs(), getFluidInputs());
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
            .findMatchingRecipe(getMachineName(), getItemInputs(), getFluidInputs());
        return recipe != null;
    }

    private List<ItemStack> getItemInputs() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = slotDefinition.minItemInputSlot; i <= slotDefinition.maxItemInputSlot; i++) {
            ItemStack stack = this.inv.getStackInSlot(i);
            if (stack != null) {
                list.add(stack.copy());
            }
        }
        return list;
    }

    private List<FluidStack> getFluidInputs() {
        List<FluidStack> list = new ArrayList<>();
        for (int i = slotDefinition.minFluidInputSlot; i <= slotDefinition.maxFluidInputSlot; i++) {
            FluidTank tank = this.fluidTanks[i];
            if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
                list.add(
                    tank.getFluid()
                        .copy());
            }
        }
        return list;
    }

    public MachineRecipe getPredictedRecipe() {
        return MachineRecipeRegistry.findMatchingRecipe(getMachineName(), getItemInputs(), getFluidInputs());
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
