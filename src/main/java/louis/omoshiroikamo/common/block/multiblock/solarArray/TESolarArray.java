package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarPanelStructure.STRUCTURE_DEFINITION;
import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarPanelStructure.TIER_OFFSET;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cofh.api.energy.IEnergyProvider;
import louis.omoshiroikamo.api.energy.PowerDistributor;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.multiblock.AbstractMultiBlockEntity;
import louis.omoshiroikamo.common.util.Logger;

public class TESolarArray extends AbstractMultiBlockEntity<TESolarArray> implements IEnergyProvider {

    private PowerDistributor powerDis;

    private int lastCollectionValue = -1;
    private static final int CHECK_INTERVAL = 100;

    public TESolarArray(int meta) {
        this.meta = meta;
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

        if (!structureCheck("tier" + (meta + 1), TIER_OFFSET[meta][0], TIER_OFFSET[meta][1], TIER_OFFSET[meta][2])) {
            return;
        }

        if (!mMachine) {
            return;
        }

        if (lastCollectionValue == -1 || shouldDoWorkThisTick(CHECK_INTERVAL)) {
            lastCollectionValue = getEnergyRegen();
        }

        if (canSeeSun() && lastCollectionValue > 0) {
            transmitEnergy(lastCollectionValue);
        }
    }

    private int getEnergyPerTick() {
        return 40;
    }

    private void transmitEnergy(int energyToSend) {
        if (energyToSend <= 0) {
            return;
        }

        if (powerDis == null) {
            powerDis = new PowerDistributor(new BlockCoord(this));
        }

        int transmitted = powerDis.transmitEnergy(worldObj, energyToSend);
    }

    float calculateLightRatio() {
        return calculateLightRatio(worldObj, xCoord, yCoord, zCoord);
    }

    boolean canSeeSun() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord);
    }

    private int getEnergyRegen() {
        float fromSun = calculateLightRatio();
        return Math.round(getEnergyPerTick() * fromSun);
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

    public int getTier() {
        return meta + 1;
    }

    @Override
    public IStructureDefinition<TESolarArray> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected String getStructurePieceName() {
        return "tier" + (meta + 1);
    }

    protected boolean structureCheck(String piece, int offsetX, int offsetY, int offsetZ) {
        boolean valid = getStructureDefinition().check(
            this,
            piece,
            worldObj,
            getExtendedFacing(),
            xCoord,
            yCoord,
            zCoord,
            offsetX,
            offsetY,
            offsetZ,
            false);

        if (valid && !mMachine) {
            mMachine = true;
            Logger.info("Multiblock formed!");
        } else if (!valid && mMachine) {
            mMachine = false;
            Logger.info("Multiblock broken!");
            clearStructureParts();
        }

        return valid;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return false;
    }
}
