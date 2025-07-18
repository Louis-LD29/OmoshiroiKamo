package louis.omoshiroikamo.common.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import louis.omoshiroikamo.api.energy.EnergyStorageAdv;
import louis.omoshiroikamo.api.energy.PowerHandlerUtil;

@Optional.InterfaceList({ @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2"),
    @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "IC2") })
public abstract class AbstractPoweredTE extends AbstractTE implements IEnergyHandler, IEnergySink {

    private int storedEnergyRF = 0;
    protected float lastSyncPowerStored = -1;
    boolean inICNet = false;
    protected boolean canReceivePower = true;

    protected EnergyStorageAdv energyStorage;

    @Override
    public void doUpdate() {
        super.doUpdate();

        if (!isServerSide()) {
            return;
        }

        loadIC2();

        boolean powerChanged = (lastSyncPowerStored != storedEnergyRF && shouldDoWorkThisTick(5));
        if (powerChanged) {
            lastSyncPowerStored = storedEnergyRF;
            forceClientUpdate = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        unloadIC2();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        unloadIC2();
    }

    @Optional.Method(modid = "IC2")
    void loadIC2() {
        if (Loader.isModLoaded("IC2") && !this.inICNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) this));
            this.inICNet = true;
        }
    }

    @Optional.Method(modid = "IC2")
    void unloadIC2() {
        if (Loader.isModLoaded("IC2") && this.inICNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) this));
            this.inICNet = false;
        }
    }

    public void setCanReceivePower(boolean value) {
        this.canReceivePower = value;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        root.setInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY, storedEnergyRF);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        int energy;
        if (root.hasKey("storedEnergy")) {
            float storedEnergyMJ = root.getFloat("storedEnergy");
            energy = (int) (storedEnergyMJ * 10);
        } else {
            energy = root.getInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY);
        }
        setEnergyStored(energy);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (!canReceivePower) return 0;
        return PowerHandlerUtil.recieveInternal(this, maxReceive, from, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStored();
    }

    public int getEnergyStored() {
        return storedEnergyRF;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    public void setEnergyStored(int energy) {
        storedEnergyRF = Math.min(energy, energyStorage.getMaxEnergyStored());
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    public int getMaxEnergyRecieved() {
        return energyStorage.getMaxReceive();
    }

    public int getMaxEnergyExtract() {
        return energyStorage.getMaxExtract();
    }

    @Optional.Method(modid = "IC2")
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return canConnectEnergy(direction);
    }

    @Optional.Method(modid = "IC2")
    public double getDemandedEnergy() {
        if (!canReceivePower) return 0;
        return convertRFtoEU(getMaxEnergyRecieved() - storedEnergyRF, getIC2Tier());
    }

    @Optional.Method(modid = "IC2")
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        if (!canReceivePower) return amount;
        int rf = convertEUtoRF(amount);
        int r = Math.max(0, Math.min(getMaxEnergyRecieved() - storedEnergyRF, rf));
        storedEnergyRF += r;
        double eu = convertRFtoEU(r, getIC2Tier());
        return amount - eu;
    }

    @Optional.Method(modid = "IC2")
    public int getSinkTier() {
        return getIC2Tier();
    }

    int getIC2Tier() {
        return material != null ? material.getVoltageTier()
            .getIC2VoltageTier() : 1;
    }

    public static double convertRFtoEU(int rf, int maxTier) {
        return (double) rf / 4;
    }

    public static int convertEUtoRF(double eu) {
        return (int) eu * 4;
    }
}
