package com.louis.test.common.block.multiblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;

import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.machine.AbstractMachineEntity;
import com.louis.test.common.block.machine.SlotDefinition;

public abstract class TileMain extends AbstractMachineEntity {

    SmartTank inputTank = new SmartTank(FluidContainerRegistry.BUCKET_VOLUME * 8);
    SmartTank outputTank = new SmartTank(FluidContainerRegistry.BUCKET_VOLUME * 8);
    private final List<TileAddon> addons = new ArrayList<>();

    public TileMain(SlotDefinition slotDefinition) {
        super(slotDefinition);
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        inputTank.writeCommon("inputTank", nbtRoot);
        outputTank.writeCommon("outputTank", nbtRoot);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        inputTank.readCommon("inputTank", nbtRoot);
        outputTank.readCommon("outputTank", nbtRoot);
    }

    public void registerAddon(TileAddon addon) {
        if (!addons.contains(addon)) {
            addons.add(addon);
        }
    }

    public void unregisterAddon(TileAddon addon) {
        addons.remove(addon);
    }

    public void onAddonTick(TileAddon addon) {}

    @Override
    public void invalidate() {
        super.invalidate();
        for (TileAddon addon : new ArrayList<>(addons)) {
            addon.invalidateController();
        }
        addons.clear();
    }

    public SmartTank getInputTank() {
        return inputTank;
    }

    public SmartTank getOutputTank() {
        return outputTank;
    }
}
