package louis.omoshiroikamo.common.block.multiblock.part.energy;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.common.util.BlockCoord;

import louis.omoshiroikamo.api.energy.EnergyStorageAdv;
import louis.omoshiroikamo.api.energy.PowerDistributor;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.io.SlotDefinition;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class TEEnergyOutput extends TEEnergyInOut {

    private PowerDistributor powerDis;

    protected TEEnergyOutput(int meta) {
        super(new SlotDefinition(0, 0, 0, 0, -1, -1), new EnergyStorageAdv(MaterialRegistry.fromMeta(meta % 100)));
        this.meta = meta;
        this.material = MaterialRegistry.fromMeta(meta % LibResources.META1);
    }

    public TEEnergyOutput() {
        this(100);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInOut.unlocalisedName + "." + (meta >= LibResources.META1 ? "output" : "input");
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
    public void doUpdate() {
        super.doUpdate();
        if (worldObj.isRemote) {
            return;
        }
        transmitEnergy();
    }

    @Override
    protected boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        super.onNeighborBlockChange(world, x, y, z, nbid);
        if (powerDis != null) {
            powerDis.neighboursChanged();
        }
    }

    private boolean transmitEnergy() {
        if (powerDis == null) {
            powerDis = new PowerDistributor(new BlockCoord(this));
        }
        int canTransmit = Math.min(getEnergyStored(), getMaxEnergyExtract());
        if (canTransmit <= 0) {
            return false;
        }
        int transmitted = powerDis.transmitEnergy(worldObj, canTransmit);
        setEnergyStored(getEnergyStored() - transmitted);
        return transmitted > 0;
    }

    @Override
    public void setCanReceivePower(boolean value) {
        super.setCanReceivePower(false);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }

}
