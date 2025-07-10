package com.louis.test.common.block.multiblock;

import java.util.*;

import net.minecraft.nbt.NBTTagCompound;

import com.louis.test.api.enums.BlockMassType;
import com.louis.test.api.enums.Material;
import com.louis.test.api.interfaces.fluid.SmartTank;
import com.louis.test.api.interfaces.heat.HeatStorage;
import com.louis.test.common.block.machine.AbstractMachineEntity;
import com.louis.test.common.block.machine.SlotDefinition;

public abstract class TileMain extends AbstractMachineEntity {

    private HeatStorage heat;
    private boolean fluidFilter = false;

    private final List<TileAddon> addons = new ArrayList<>();

    public TileMain(SlotDefinition slotDefinition, Material material) {
        super(slotDefinition, material);
    }

    public void registerAddon(TileAddon addon) {
        if (!addons.contains(addon)) {
            addons.add(addon);
        }
    }

    public void unregisterAddon(TileAddon addon) {
        addons.remove(addon);
    }

    private final Map<String, SmartTank> tanks = new HashMap<>();

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        heat.writeCommon(nbtRoot);
        nbtRoot.setBoolean("fluidFilter", fluidFilter);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        heat.readCommon(nbtRoot);
        fluidFilter = nbtRoot.getBoolean("fluidFilter");
    }

    public void setTank(int index, int capacity) {
        fluidTanks[index] = new SmartTank(capacity);
    }

    public SmartTank getTank(int index) {
        return fluidTanks[index];
    }

    public List<SmartTank> getInputTanks() {
        List<SmartTank> inputs = new ArrayList<>();
        int start = slotDefinition.getMinFluidInputSlot();
        int count = slotDefinition.getNumInputFluidSlots();
        for (int i = 0; i < count; i++) {
            inputs.add(fluidTanks[start + i]);
        }
        return inputs;
    }

    public List<SmartTank> getOutputTanks() {
        List<SmartTank> outputs = new ArrayList<>();
        int start = slotDefinition.getMinFluidOutputSlot();
        int count = slotDefinition.getNumOutputFluidSlots();
        for (int i = 0; i < count; i++) {
            outputs.add(fluidTanks[start + i]);
        }
        return outputs;
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

    public HeatStorage getHeat() {
        return heat;
    }

    public void setHeatStorage(HeatStorage heatStorage) {
        this.heat = heatStorage;
    }

    public boolean getFluidFilter() {
        return fluidFilter;
    }

    public void setFluidFilter(boolean fluidFilter) {
        this.fluidFilter = fluidFilter;
    }

    public HeatStorage HEATSTORAGE() {
        return new HeatStorage(this.material, BlockMassType.MACHINE);
    }

    public int TANK() {
        return this.material.getVolumeMB();
    }
}
