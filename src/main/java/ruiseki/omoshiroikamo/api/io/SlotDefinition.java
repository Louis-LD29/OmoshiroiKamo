package ruiseki.omoshiroikamo.api.io;

public class SlotDefinition {

    public final int minUpgradeSlot;
    public final int maxUpgradeSlot;

    public final int minItemInputSlot;
    public final int maxItemInputSlot;

    public final int minFluidInputSlot;
    public final int maxFluidInputSlot;

    public final int minFluidOutputSlot;
    public final int maxFluidOutputSlot;

    public final int minItemOutputSlot;
    public final int maxItemOutputSlot;

    public SlotDefinition(int numInputs, int numOutputs, int numUpgradeSlots) {
        this.minItemInputSlot = 0;
        this.minFluidInputSlot = 0;
        this.maxItemInputSlot = numInputs - 1;
        this.maxFluidInputSlot = numInputs - 1;
        this.minItemOutputSlot = numOutputs > 0 ? numInputs : -10;
        this.maxItemOutputSlot = minItemOutputSlot + (numOutputs - 1);
        this.minFluidOutputSlot = numOutputs > 0 ? numInputs : -10;
        this.maxFluidOutputSlot = minFluidOutputSlot + (numOutputs - 1);
        this.minUpgradeSlot = numUpgradeSlots > 0 ? numInputs + numOutputs : -1;
        this.maxUpgradeSlot = minUpgradeSlot + (numUpgradeSlots - 1);
    }

    public SlotDefinition(int numInputs, int numOutputs) {
        this.minItemInputSlot = 0;
        this.minFluidInputSlot = 0;
        this.maxItemInputSlot = numInputs - 1;
        this.maxFluidInputSlot = numInputs - 1;
        this.minItemOutputSlot = numOutputs > 0 ? numInputs : -10;
        this.maxItemOutputSlot = minItemOutputSlot + (numOutputs - 1);
        this.minFluidOutputSlot = numOutputs > 0 ? numInputs : -10;
        this.maxFluidOutputSlot = minFluidOutputSlot + (numOutputs - 1);
        this.minUpgradeSlot = Math.max(maxItemInputSlot, maxItemOutputSlot) + 1;
        this.maxUpgradeSlot = minUpgradeSlot;
    }

    public SlotDefinition(int minItemInputSlot, int maxItemInputSlot, int minItemOutputSlot, int maxItemOutputSlot,
        int minUpgradeSlot, int maxUpgradeSlot) {
        this.minItemInputSlot = minItemInputSlot;
        this.minFluidInputSlot = minItemInputSlot;
        this.maxItemInputSlot = maxItemInputSlot;
        this.maxFluidInputSlot = maxItemInputSlot;
        this.minItemOutputSlot = minItemOutputSlot;
        this.maxItemOutputSlot = maxItemOutputSlot;
        this.minFluidOutputSlot = minItemOutputSlot;
        this.maxFluidOutputSlot = maxItemOutputSlot;
        this.minUpgradeSlot = minUpgradeSlot;
        this.maxUpgradeSlot = maxUpgradeSlot;
    }

    public SlotDefinition(int minItemInputSlot, int maxItemInputSlot, int minItemOutputSlot, int maxItemOutputSlot,
        int minFluidInputSlot, int maxFluidInputSlot, int minFluidOutputSlot, int maxFluidOutputSlot,
        int minUpgradeSlot, int maxUpgradeSlot) {
        this.minItemInputSlot = minItemInputSlot;
        this.maxItemInputSlot = maxItemInputSlot;
        this.minItemOutputSlot = minItemOutputSlot;
        this.maxItemOutputSlot = maxItemOutputSlot;
        this.minFluidInputSlot = minFluidInputSlot;
        this.maxFluidInputSlot = maxFluidInputSlot;
        this.minFluidOutputSlot = minFluidOutputSlot;
        this.maxFluidOutputSlot = maxFluidOutputSlot;
        this.minUpgradeSlot = minUpgradeSlot;
        this.maxUpgradeSlot = maxUpgradeSlot;
    }

    public boolean isUpgradeSlot(int slot) {
        return slot >= minUpgradeSlot && slot <= maxUpgradeSlot;
    }

    public boolean isInputSlot(int slot) {
        return slot >= minItemInputSlot && slot <= maxItemInputSlot;
    }

    public boolean isFluidInputSlot(int slot) {
        return slot >= minFluidInputSlot && slot <= minFluidInputSlot;
    }

    public boolean isOutputSlot(int slot) {
        return slot >= minItemOutputSlot && slot <= maxItemOutputSlot;
    }

    public boolean isFluidOutputSlot(int slot) {
        return slot >= minFluidOutputSlot && slot <= maxFluidOutputSlot;
    }

    public int getNumUpgradeSlots() {
        if (minUpgradeSlot < 0) {
            return 0;
        }
        return Math.max(0, maxUpgradeSlot - minUpgradeSlot + 1);
    }

    public int getNumInputSlots() {
        if (minItemInputSlot < 0) {
            return 0;
        }
        return Math.max(0, maxItemInputSlot - minItemInputSlot + 1);
    }

    public int getNumOutputSlots() {
        if (minItemOutputSlot < 0) {
            return 0;
        }
        return Math.max(0, maxItemOutputSlot - minItemOutputSlot + 1);
    }

    public int getNumSlots() {
        return Math.max(Math.max(getMaxInputSlot(), getMaxOutputSlot()), getMaxUpgradeSlot()) + 1;
    }

    public int getNumFluidSlots() {
        return Math.max(getMaxFluidInputSlot(), getMaxFluidOutputSlot()) + 1;
    }

    public int getNumInputFluidSlots() {
        if (minFluidInputSlot < 0) return 0;
        return Math.max(0, maxFluidInputSlot - minFluidInputSlot + 1);
    }

    public int getNumOutputFluidSlots() {
        if (minFluidOutputSlot < 0) return 0;
        return Math.max(0, maxFluidOutputSlot - minFluidOutputSlot + 1);
    }

    public int getMinUpgradeSlot() {
        return minUpgradeSlot;
    }

    public int getMaxUpgradeSlot() {
        return maxUpgradeSlot;
    }

    public int getMinInputSlot() {
        return minItemInputSlot;
    }

    public int getMaxInputSlot() {
        return maxItemInputSlot;
    }

    public int getMinItemInputSlot() {
        return minItemInputSlot;
    }

    public int getMinFluidInputSlot() {
        return minFluidInputSlot;
    }

    public int getMaxFluidInputSlot() {
        return maxFluidInputSlot;
    }

    public int getMinOutputSlot() {
        return minItemOutputSlot;
    }

    public int getMaxOutputSlot() {
        return maxItemOutputSlot;
    }

    public int getMinFluidOutputSlot() {
        return minFluidOutputSlot;
    }

    public int getMaxFluidOutputSlot() {
        return maxFluidOutputSlot;
    }

    @Override
    public String toString() {
        return "SlotDefinition [minUpgradeSlot=" + minUpgradeSlot
            + ", maxUpgradeSlot="
            + maxUpgradeSlot
            + ", minItemInputSlot="
            + minItemInputSlot
            + ", maxItemInputSlot="
            + maxItemInputSlot
            + ", minFluidInputSlot="
            + minFluidInputSlot
            + ", maxFluidInputSlot="
            + maxFluidInputSlot
            + ", minItemOutputSlot="
            + minItemOutputSlot
            + ", maxItemOutputSlot="
            + maxItemOutputSlot
            + ", minFluidOutputSlot="
            + minFluidOutputSlot
            + ", maxFluidOutputSlot="
            + maxFluidOutputSlot
            + ", nunSlots="
            + getNumSlots()
            + " ]";
    }
}
