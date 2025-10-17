package ruiseki.omoshiroikamo.common.recipes.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import ruiseki.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import ruiseki.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public class RecipeBuilder {

    private final List<ChanceItemStack> itemInputs = new ArrayList<>();
    private final List<ChanceItemStack> itemOutputs = new ArrayList<>();
    private final List<ChanceFluidStack> fluidInputs = new ArrayList<>();
    private final List<ChanceFluidStack> fluidOutputs = new ArrayList<>();

    private int requiredTemperature = 0;
    private int requiredPressure = 0;
    private int energyCost = 0;
    private String uid = "";

    public RecipeBuilder addItemInput(ItemStack stack, float chance) {
        itemInputs.add(new ChanceItemStack(stack.copy(), chance));
        return this;
    }

    public RecipeBuilder addItemInput(String oreDict, int amount, float chance) {
        List<ItemStack> oreStacks = OreDictionary.getOres(oreDict);
        if (!oreStacks.isEmpty()) {
            ItemStack stack = oreStacks.get(0)
                .copy();
            stack.stackSize = amount;
            return addItemInput(stack, chance);
        } else {
            throw new IllegalArgumentException("OreDict not found: " + oreDict);
        }
    }

    public List<ChanceItemStack> getItemInputs() {
        return itemInputs;
    }

    public RecipeBuilder addFluidInput(FluidStack fluidStack, float chance) {
        fluidInputs.add(new ChanceFluidStack(fluidStack, chance));
        return this;
    }

    public List<ChanceFluidStack> getFluidInputs() {
        return fluidInputs;
    }

    public RecipeBuilder addItemOutput(ItemStack stack, float chance) {
        itemOutputs.add(new ChanceItemStack(stack.copy(), chance));
        return this;
    }

    public RecipeBuilder addItemOutput(String oreDict, int amount, float chance) {
        List<ItemStack> oreStacks = OreDictionary.getOres(oreDict);
        if (!oreStacks.isEmpty()) {
            ItemStack stack = oreStacks.get(0)
                .copy();
            stack.stackSize = amount;
            return addItemOutput(stack, chance);
        } else {
            throw new IllegalArgumentException("OreDict not found: " + oreDict);
        }
    }

    public List<ChanceItemStack> getItemOutputs() {
        return itemOutputs;
    }

    public RecipeBuilder addFluidOutput(FluidStack fluidStack, float chance) {
        fluidOutputs.add(new ChanceFluidStack(fluidStack, chance));
        return this;
    }

    public List<ChanceFluidStack> getFluidOutputs() {
        return fluidOutputs;
    }

    public RecipeBuilder setTemperature(int kelvin) {
        this.requiredTemperature = kelvin;
        return this;
    }

    public int getTemperature() {
        return requiredTemperature;
    }

    public RecipeBuilder setPressure(int kPa) {
        this.requiredPressure = kPa;
        return this;
    }

    public int getPressure() {
        return requiredPressure;
    }

    public RecipeBuilder setEnergyCost(int energy) {
        this.energyCost = energy;
        return this;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public RecipeBuilder setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public MachineRecipe build() {
        return new MachineRecipe(
            itemInputs,
            fluidInputs,
            itemOutputs,
            fluidOutputs,
            requiredTemperature,
            requiredPressure,
            energyCost,
            uid);
    }
}
