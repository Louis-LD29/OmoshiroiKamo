package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarPanelStructure.STRUCTURE_DEFINITION;
import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarPanelStructure.TIER_OFFSET;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import louis.omoshiroikamo.api.energy.IPowerContainer;
import louis.omoshiroikamo.api.energy.PowerDistributor;
import louis.omoshiroikamo.api.energy.PowerHandlerUtil;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.multiblock.AbstractMultiBlockEntity;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.network.PacketPowerStorage;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.config.block.SolarArrayConfig;
import louis.omoshiroikamo.plugin.structureLib.StructureLibUtils.UpgradeEntry;

public class TESolarArray extends AbstractMultiBlockEntity<TESolarArray> implements IEnergyProvider, IPowerContainer {

    private PowerDistributor powerDis;

    private int lastCollectionValue = -1;
    private static final int CHECK_INTERVAL = 100;
    private final Set<UpgradeEntry> mUpgrade = new HashSet<>();
    private final Set<UpgradeEntry> mPiezo = new HashSet<>();

    private int storedEnergyRF = 0;
    protected float lastSyncPowerStored = -1;
    private EnergyStorage energyStorage;

    public TESolarArray(int meta) {
        this.meta = meta;
        energyStorage = new EnergyStorage(getEnergySolarArrayTier() * 10);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockSolarArray.unlocalisedName;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        super.onNeighborBlockChange(world, x, y, z, nbid);
        if (powerDis != null) {
            powerDis.neighboursChanged();
        }
    }

    @Override
    public void doUpdate() {
        super.doUpdate();

        if (worldObj.isRemote) {
            return;
        }

        if (structureCheck("tier" + (meta + 1), TIER_OFFSET[meta][0], TIER_OFFSET[meta][1], TIER_OFFSET[meta][2])) {
            return;
        }

        if (!mMachine) {
            return;
        }

        boolean powerChanged = (lastSyncPowerStored != storedEnergyRF && shouldDoWorkThisTick(5));
        if (powerChanged) {
            lastSyncPowerStored = storedEnergyRF;
            PacketHandler.sendToAllAround(new PacketPowerStorage(this), this);
        }

        collectEnergy();
        transmitEnergy();
    }

    private int getEnergyPerTick() {
        return getEnergySolarArrayTier();
    }

    private void collectEnergy() {
        if (canSeeSun()) {
            if (lastCollectionValue == -1 || shouldDoWorkThisTick(CHECK_INTERVAL)) {
                lastCollectionValue = getEnergyRegen();
            }
            if (lastCollectionValue > 0) {
                this.setEnergyStored(Math.min(lastCollectionValue + this.getEnergyStored(), this.getMaxEnergyStored()));
            }
        }
    }

    private void transmitEnergy() {
        if (powerDis == null) {
            powerDis = new PowerDistributor(getLocation());
        }

        int canTransmit = Math.min(getEnergyStored(), getMaxEnergyStored());
        if (canTransmit <= 0) {
            return;
        }

        int transmitted = powerDis.transmitEnergy(worldObj, canTransmit);
        setEnergyStored(getEnergyStored() - transmitted);
    }

    float calculateLightRatio() {
        return calculateLightRatio(worldObj, xCoord, yCoord, zCoord);
    }

    boolean canSeeSun() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord);
    }

    private int getEnergyRegen() {
        float fromSun = calculateLightRatio();
        float isRaining = worldObj.isRaining() ? (2f / 3f) : 1f;
        int formPiezo = worldObj.isRaining() ? mPiezo.size() * 64 : 0;
        int gen = Math.round(getEnergyPerTick() * fromSun * isRaining) + formPiezo;
        Logger.info(formPiezo);
        return Math.min(getEnergySolarArrayTier(), gen);
    }

    public static float calculateLightRatio(World world, int x, int y, int z) {
        int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
        float sunAngle = world.getCelestialAngleRadians(1.0F);

        if (sunAngle < (float) Math.PI) {
            sunAngle += (0.0F - sunAngle) * 0.2F;
        } else {
            sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
        }

        lightValue = Math.round(lightValue * MathHelper.cos(sunAngle));

        lightValue = MathHelper.clamp_int(lightValue, 0, 15);

        return lightValue / 15f;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return false;
    }

    @Override
    public int getEnergyStored() {
        return storedEnergyRF;
    }

    @Override
    public void setEnergyStored(int storedEnergy) {
        storedEnergyRF = Math.min(storedEnergy, getMaxEnergyStored());
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public IStructureDefinition<TESolarArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected String getStructurePieceName() {
        return "tier" + getTier();
    }

    @Override
    public boolean addToMachine(Block block, int meta, int x, int y, int z) {
        if (block == null) {
            return false;
        }

        UpgradeEntry entry = new UpgradeEntry(block, meta, x, y, z);
        if (mUpgrade.contains(entry)) {
            return false;
        }

        boolean added = false;

        if (block == ModBlocks.blockModifier && meta == 1) {
            added = true;
            mPiezo.add(entry);
        }

        if (added) {
            mUpgrade.add(entry);
        }

        return added;
    }

    @Override
    public void clearStructureParts() {
        super.clearStructureParts();
        mUpgrade.clear();
        mPiezo.clear();
    }

    public int getTier() {
        return getMeta() + 1;
    }

    public int getEnergySolarArrayTier() {

        int maxEnergyStored = 0;

        switch (getMeta()) {
            case 1:
                maxEnergyStored = SolarArrayConfig.peakEnergyTier2;
                break;
            case 2:
                maxEnergyStored = SolarArrayConfig.peakEnergyTier3;
                break;
            case 3:
                maxEnergyStored = SolarArrayConfig.peakEnergyTier4;
                break;
            default:
                maxEnergyStored = SolarArrayConfig.peakEnergyTier1;
                break;
        }

        return maxEnergyStored;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
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
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
        root.setInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY, storedEnergyRF);
    }
}
