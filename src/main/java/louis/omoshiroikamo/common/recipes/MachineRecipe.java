package louis.omoshiroikamo.common.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import louis.omoshiroikamo.common.core.helper.OreDictUtils;

public class MachineRecipe {

    public final List<ItemStack> itemInputs;
    public final List<FluidStack> fluidInputs;
    public final List<ItemStack> itemOutputs;
    public final List<FluidStack> fluidOutputs;

    public final int requiredTemperature; // Độ K
    public final float requiredPressure; // Áp suất kPa hoặc tuỳ định nghĩa
    public final int energyCost; // EU hoặc RF hoặc tick
    public final String uid; // EU hoặc RF hoặc tick

    public MachineRecipe(List<ItemStack> itemInputs, List<FluidStack> fluidInputs, List<ItemStack> itemOutputs,
        List<FluidStack> fluidOutputs, int requiredTemperature, int requiredPressure, int energyCost, String uid) {
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
        this.requiredTemperature = requiredTemperature;
        this.requiredPressure = requiredPressure;
        this.energyCost = energyCost;
        this.uid = uid;
    }

    public List<ItemStack> getItemInputs() {
        return itemInputs;
    }

    public List<ItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public List<FluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public List<FluidStack> getFluidOutputs() {
        return fluidOutputs;
    }

    public int getRequiredTemperature() {
        return requiredTemperature;
    }

    public float getRequiredPressure() {
        return requiredPressure;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public String getUid() {
        return uid;
    }

    /**
     * Kiểm tra xem input hiện tại có khớp với công thức này không.
     * Không cần đúng vị trí, chỉ cần đủ số lượng và loại.
     */
    public boolean matches(List<ItemStack> items, List<FluidStack> fluids, int temperature, int pressure,
        int availableEnergy) {
        return this.requiredTemperature >= temperature && this.requiredPressure >= pressure
            && this.energyCost >= availableEnergy
            && containsAllItems(items, itemInputs)
            && containsAllFluids(fluids, fluidInputs);
    }

    private boolean containsAllItems(List<ItemStack> available, List<ItemStack> required) {
        for (ItemStack req : required) {
            int remaining = req.stackSize;

            for (ItemStack candidate : available) {
                if (OreDictUtils.isOreDictMatch(candidate, req)) {
                    remaining -= candidate.stackSize;
                    if (remaining <= 0) break;
                }
            }

            if (remaining > 0) return false;
        }
        return true;
    }

    private boolean containsAllFluids(List<FluidStack> available, List<FluidStack> required) {
        for (FluidStack req : required) {
            int remaining = req.amount;

            for (FluidStack candidate : available) {
                if (canMergeFluids(candidate, req)) {
                    remaining -= candidate.amount;
                    if (remaining <= 0) break;
                }
            }

            if (remaining > 0) return false;
        }
        return true;
    }

    public int getInputComplexityScore() {
        int itemScore = itemInputs != null ? itemInputs.stream()
            .mapToInt(stack -> stack != null ? stack.stackSize : 0)
            .sum() : 0;
        int fluidScore = fluidInputs != null ? fluidInputs.stream()
            .mapToInt(fluid -> fluid != null ? fluid.amount : 0)
            .sum() : 0;
        return itemScore + fluidScore;
    }

    private boolean canMergeItems(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem();
    }

    private boolean canMergeFluids(FluidStack a, FluidStack b) {
        return a.getFluid() == b.getFluid();
    }

    private List<ItemStack> copyItemList(List<ItemStack> list) {
        List<ItemStack> copy = new ArrayList<>();
        for (ItemStack stack : list) {
            copy.add(stack.copy());
        }
        return copy;
    }

    private List<FluidStack> copyFluidList(List<FluidStack> list) {
        List<FluidStack> copy = new ArrayList<>();
        for (FluidStack stack : list) {
            copy.add(stack.copy());
        }
        return copy;
    }
}
