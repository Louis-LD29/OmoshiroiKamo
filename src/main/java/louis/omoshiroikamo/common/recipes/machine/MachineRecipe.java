package louis.omoshiroikamo.common.recipes.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;
import louis.omoshiroikamo.common.util.OreDictUtils;

public class MachineRecipe {

    public final List<ChanceItemStack> itemInputs;
    public final List<ChanceFluidStack> fluidInputs;
    public final List<ChanceItemStack> itemOutputs;
    public final List<ChanceFluidStack> fluidOutputs;

    public final int requiredTemperature;
    public final float requiredPressure;
    public final int energyCost;
    public final String uid;

    public MachineRecipe(List<ChanceItemStack> itemInputs, List<ChanceFluidStack> fluidInputs,
                         List<ChanceItemStack> itemOutputs, List<ChanceFluidStack> fluidOutputs, int requiredTemperature,
                         int requiredPressure, int energyCost, String uid) {
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
        this.requiredTemperature = requiredTemperature;
        this.requiredPressure = requiredPressure;
        this.energyCost = energyCost;
        this.uid = uid;
    }

    public List<ChanceItemStack> getItemInputs() {
        return itemInputs;
    }

    public List<ChanceItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public List<ChanceFluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public List<ChanceFluidStack> getFluidOutputs() {
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

    private boolean containsAllItems(List<ItemStack> available, List<ChanceItemStack> required) {
        for (ChanceItemStack req : required) {
            if (req.chance < 1.0f) {
                continue;
            }
            int remaining = req.stack.stackSize;

            for (ItemStack candidate : available) {
                if (OreDictUtils.isOreDictMatch(candidate, req.stack)) {
                    remaining -= candidate.stackSize;
                    if (remaining <= 0) {
                        break;
                    }
                }
            }

            if (remaining > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean containsAllFluids(List<FluidStack> available, List<ChanceFluidStack> required) {
        for (ChanceFluidStack req : required) {
            if (req.chance < 1.0f) {
                continue;
            }
            int remaining = req.stack.amount;

            for (FluidStack candidate : available) {
                if (canMergeFluids(candidate, req.stack)) {
                    remaining -= candidate.amount;
                    if (remaining <= 0) {
                        break;
                    }
                }
            }

            if (remaining > 0) {
                return false;
            }
        }
        return true;
    }

    public int getInputComplexityScore() {
        int itemScore = itemInputs != null ? itemInputs.stream()
            .mapToInt(entry -> entry != null && entry.stack != null ? (int) (entry.stack.stackSize * entry.chance) : 0)
            .sum() : 0;

        int fluidScore = fluidInputs != null ? fluidInputs.stream()
            .mapToInt(entry -> entry != null && entry.stack != null ? (int) (entry.stack.amount * entry.chance) : 0)
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
