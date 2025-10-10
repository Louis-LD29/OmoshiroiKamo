package louis.omoshiroikamo.common.block.solarArray;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.enderio.core.common.util.BlockCoord;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.energy.PowerDistributor;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.io.SlotDefinition;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.abstractClass.AbstractPoweredTE;

public class TESolarArray extends AbstractPoweredTE {

    protected IStructureDefinition<?> multiDefinition = null;
    private PowerDistributor powerDis;

    private int lastCollectionValue = -1;

    private static final int CHECK_INTERVAL = 100;

    public TESolarArray() {
        super(new SlotDefinition(0, 1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
        energyStorage.setCapacity(10000);
        energyStorage.setMaxExtract(200);
        energyStorage.setMaxReceive(200);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockSolarArray.unlocalisedName;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockSolarArray.unlocalisedName;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int i, ItemStack itemstack) {
        return false;
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
    public void onNeighborBlockChange(Block blockId) {
        super.onNeighborBlockChange(blockId);
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
        collectEnergy();
        transmitEnergy();
    }

    private int getEnergyPerTick() {
        return 40;
    }

    private void transmitEnergy() {
        if (powerDis == null) {
            powerDis = new PowerDistributor(new BlockCoord(this));
        }
        int canTransmit = Math.min(getEnergyStored(), getPowerUsePerTick() * 2);
        if (canTransmit <= 0) {
            return;
        }
        int transmitted = powerDis.transmitEnergy(worldObj, canTransmit);
        setEnergyStored(getEnergyStored() - transmitted);
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

    private int getEnergyRegen() {
        float fromSun = calculateLightRatio();
        return Math.round(getEnergyPerTick() * fromSun);
    }

    float calculateLightRatio() {
        return calculateLightRatio(worldObj, xCoord, yCoord, zCoord);
    }

    boolean canSeeSun() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
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
}
