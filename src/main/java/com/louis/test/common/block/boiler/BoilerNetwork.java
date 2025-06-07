package com.louis.test.common.block.boiler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.Lists;
import com.louis.test.api.interfaces.network.IBoilerNetwork;
import com.louis.test.common.block.SmartTank;

public class BoilerNetwork {

    private List<IBoilerNetwork> boilers;
    private boolean empty = true;

    protected SmartTank inputTank;

    public static final int FLUID_PER = 10000;

    public BoilerNetwork() {
        boilers = Lists.newArrayList();
        inputTank = new SmartTank(getCapacity());
    }

    BoilerNetwork(IBoilerNetwork initial) {
        this();
        boilers.add(initial);
        inputTank.setCapacity(getCapacity());
        empty = false;
    }

    void onUpdate(IBoilerNetwork panel) {}

    boolean addToNetwork(IBoilerNetwork boiler) {

        if (boiler.getNetwork() == this && boiler.getNetwork()
            .isValid() && boilers.contains(boiler)) {
            return false;
        }

        if (boiler instanceof TileBoiler) {
            if (hasTileBoiler()) {
                return false; // Đã có 1 TileBoiler rồi, không thêm nữa
            }
        }

        if (boiler.getNetwork() == null || !boiler.getNetwork()
            .isValid() || boiler.getNetwork() == this) {
            if (!boilers.contains(boiler)) {
                boilers.add(boiler);
            }
            boiler.setNetwork(this);
            updateInputTank();
        } else {
            BoilerNetwork other = boiler.getNetwork();

            int totalFluid = inputTank.getFluidAmount() + other.inputTank.getFluidAmount();

            for (IBoilerNetwork otherPanel : other.boilers) {
                if (otherPanel instanceof TileBoiler && hasTileBoiler()) {
                    continue; // Bỏ qua nếu đã có TileBoiler rồi
                }
                boilers.add(otherPanel);
                otherPanel.setNetwork(this);
            }
            updateInputTank();

            inputTank.setFluidAmount(totalFluid);
            other.invalidate();
        }

        empty = false;
        return true;
    }

    void removeFromNetwork(IBoilerNetwork boiler) {
        List<IBoilerNetwork> neighbors = new ArrayList<>();
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = boiler.getLocation()
                .getLocation(dir)
                .getTileEntity(boiler.getWorldObj());
            if (te instanceof IBoilerNetwork) {
                neighbors.add((IBoilerNetwork) te);
            }
        }

        inputTank.setFluid(null);
        destroyNetwork();
    }

    private int getCapacity() {
        return FLUID_PER;
    }

    private void updateInputTank() {
        inputTank.setCapacity(getCapacity());
    }

    void destroyNetwork() {
        for (IBoilerNetwork te : boilers) {
            te.setNetwork(new BoilerNetwork());
        }
        invalidate();
    }

    private void invalidate() {
        boilers.clear();
        empty = true;
    }

    public boolean isValid() {
        return !empty;
    }

    public void addFluidBuffer(SmartTank tank) {
        inputTank.fill(tank.getFluid(), true);
    }

    IBoilerNetwork getMaster() {
        return boilers.get(0);
    }

    boolean shouldSave(IBoilerNetwork boiler) {
        return getMaster() == boiler;
    }

    public int size() {
        return boilers.size();
    }

    void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("validBoiler", true);
        inputTank.writeCommon("inputTank", tag);
    }

    void readFromNBT(IBoilerNetwork boiler, NBTTagCompound tag) {
        if (tag.getBoolean("validBoiler")) {
            inputTank.readCommon("inputTank", tag);
            addToNetwork(boiler);
        }
    }

    public SmartTank getInputTank() {
        return inputTank;
    }

    private boolean hasTileBoiler() {
        for (IBoilerNetwork b : boilers) {
            if (b instanceof TileBoiler) {
                return true;
            }
        }
        return false;
    }
}
