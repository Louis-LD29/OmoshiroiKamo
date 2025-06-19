package com.louis.test.common.block.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.louis.test.common.recipes.IPoweredTask;
import com.louis.test.common.recipes.MachineRecipe;
import com.louis.test.common.recipes.MachineRecipeRegistry;

public class PoweredTask implements IPoweredTask {

    public static final String KEY_INPUT_STACKS = "inputsStacks";

    public static final String KEY_RECIPE = "recipeUid";
    public static final String KEY_USED_ENERGY = "usedEnergy";
    private static final String KEY_CHANCE = "chance";

    private MachineRecipe recipe;

    private float usedEnergy = 0;

    private float requiredEnergy;

    private float chance;

    private List<ItemStack> itemStacks;

    private List<FluidStack> fluidStacks;

    public PoweredTask(MachineRecipe recipe, float chance, List<ItemStack> itemStacks, List<FluidStack> fluidStacks) {
        this(recipe, 0, chance, itemStacks, fluidStacks);
    }

    public PoweredTask(MachineRecipe recipe, float usedEnergy, float chance, List<ItemStack> itemStacks,
        List<FluidStack> fluidStacks) {

        this.recipe = recipe;
        this.usedEnergy = usedEnergy;
        this.chance = MathHelper.clamp_float(chance, 0, 1);

        this.itemStacks = new ArrayList<ItemStack>();
        if (itemStacks != null) {
            for (ItemStack stack : itemStacks) {
                if (stack != null && stack.stackSize > 0) {
                    this.itemStacks.add(stack.copy());
                }
            }
        }

        this.fluidStacks = new ArrayList<FluidStack>();
        if (fluidStacks != null) {
            for (FluidStack fluid : fluidStacks) {
                if (fluid != null && fluid.amount > 0) {
                    this.fluidStacks.add(fluid.copy());
                }
            }
        }
        this.requiredEnergy = recipe.getEnergyCost();
    }

    @Override
    public void update(float availableEnergy) {
        usedEnergy += availableEnergy;
    }

    @Override
    public boolean isComplete() {
        return usedEnergy >= requiredEnergy;
    }

    @Override
    public float getProgress() {
        return MathHelper.clamp_float(usedEnergy / requiredEnergy, 0, 1);
    }

    @Override
    public float getRequiredEnergy() {
        return requiredEnergy;
    }

    public void setRequiredEnergy(float requiredEnergy) {
        this.requiredEnergy = requiredEnergy;
    }

    @Override
    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtRoot) {
        if (recipe != null) {
            nbtRoot.setString(KEY_RECIPE, recipe.getUid());
        }
        nbtRoot.setFloat(KEY_USED_ENERGY, usedEnergy);
        nbtRoot.setFloat(KEY_CHANCE, chance);

        // Write itemStacks
        NBTTagList itemList = new NBTTagList();
        for (ItemStack stack : itemStacks) {
            NBTTagCompound tag = new NBTTagCompound();
            stack.writeToNBT(tag);
            itemList.appendTag(tag);
        }
        nbtRoot.setTag("ItemStacks", itemList);

        // Write fluidStacks
        NBTTagList fluidList = new NBTTagList();
        for (FluidStack fluid : fluidStacks) {
            NBTTagCompound tag = new NBTTagCompound();
            fluid.writeToNBT(tag);
            fluidList.appendTag(tag);
        }
        nbtRoot.setTag("FluidStacks", fluidList);
    }

    public static IPoweredTask readFromNBT(NBTTagCompound nbtRoot) {
        float usedEnergy = nbtRoot.getFloat(KEY_USED_ENERGY);
        float chance = nbtRoot.getFloat(KEY_CHANCE);

        // Đọc danh sách item
        List<ItemStack> itemStacks = new ArrayList<ItemStack>();
        if (nbtRoot.hasKey("ItemStacks")) {
            NBTTagList itemList = nbtRoot.getTagList("ItemStacks", 10); // 10 = compound
            for (int i = 0; i < itemList.tagCount(); i++) {
                NBTTagCompound tag = itemList.getCompoundTagAt(i);
                ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                if (stack != null) {
                    itemStacks.add(stack);
                }
            }
        }

        // Đọc danh sách fluid
        List<FluidStack> fluidStacks = new ArrayList<FluidStack>();
        if (nbtRoot.hasKey("FluidStacks")) {
            NBTTagList fluidList = nbtRoot.getTagList("FluidStacks", 10);
            for (int i = 0; i < fluidList.tagCount(); i++) {
                NBTTagCompound tag = fluidList.getCompoundTagAt(i);
                FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag);
                if (fluid != null) {
                    fluidStacks.add(fluid);
                }
            }
        }

        // Lấy recipe từ UID
        String uid = nbtRoot.getString(KEY_RECIPE);
        MachineRecipe recipe = MachineRecipeRegistry.getRecipeForUid(uid);
        if (recipe != null) {
            return new PoweredTask(recipe, usedEnergy, chance, itemStacks, fluidStacks);
        }

        return null;
    }

    @Override
    public @Nullable MachineRecipe getRecipe() {
        return recipe;
    }

    @Override
    public List<ItemStack> getItemOutputs() {
        return recipe.getItemOutputs();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return recipe.getFluidOutputs();
    }
}
