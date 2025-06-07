package com.louis.test.common.block.machine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.vecmath.VecmathUtil;
import com.louis.test.api.interfaces.power.IInternalPoweredTile;
import com.louis.test.core.handlers.PowerHandlerUtil;
import com.louis.test.core.network.PacketHandler;
import com.louis.test.core.network.PacketPowerStorage;

import cofh.api.energy.EnergyStorage;

public abstract class AbstractPoweredMachineEntity extends AbstractMachineEntity implements IInternalPoweredTile {

    public EnergyStorage energyStorage = new EnergyStorage(64000, 400, 200);
    private int storedEnergyRF;
    protected float lastSyncPowerStored = -1;

    protected AbstractPoweredMachineEntity(SlotDefinition slotDefinition) {
        super(slotDefinition);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void doUpdate() {

        super.doUpdate();

        if (worldObj.isRemote) {
            return;
        }
        boolean powerChanged = (lastSyncPowerStored != storedEnergyRF && shouldDoWorkThisTick(5));
        if (powerChanged) {
            lastSyncPowerStored = storedEnergyRF;
            PacketHandler.sendToAllAround(new PacketPowerStorage(this), this);
        }
    }

    // RF API Power

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return !isSideDisabled(from.ordinal());
    }

    @Override
    public int getMaxEnergyRecieved(ForgeDirection dir) {
        if (isSideDisabled(dir.ordinal())) {
            return 0;
        }
        return energyStorage.getMaxReceive();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public void setEnergyStored(int stored) {
        storedEnergyRF = MathHelper.clamp_int(stored, 0, getMaxEnergyStored());
    }

    @Override
    public int getEnergyStored() {
        return storedEnergyRF;
    }

    public void setEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    // ----- Common Machine Functions

    @Override
    public boolean displayPower() {
        return true;
    }

    public boolean hasPower() {
        return storedEnergyRF > 0;
    }

    public int getEnergyStoredScaled(int scale) {
        // NB: called on the client so can't use the power provider
        return VecmathUtil.clamp(Math.round(scale * ((float) storedEnergyRF / getMaxEnergyStored())), 0, scale);
    }

    public int getPowerUsePerTick() {
        return energyStorage.getMaxExtract();
    }

    // --------- NBT

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        int energy;
        if (nbtRoot.hasKey("storedEnergy")) {
            float storedEnergyMJ = nbtRoot.getFloat("storedEnergy");
            energy = (int) (storedEnergyMJ * 10);
        } else {
            energy = nbtRoot.getInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY);
        }
        setEnergyStored(energy);
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        nbtRoot.setInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY, storedEnergyRF);
    }
}
