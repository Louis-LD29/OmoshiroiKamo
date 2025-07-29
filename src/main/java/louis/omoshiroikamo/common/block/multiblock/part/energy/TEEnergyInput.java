package louis.omoshiroikamo.common.block.multiblock.part.energy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import louis.omoshiroikamo.api.energy.EnergyStorageAdv;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.io.SlotDefinition;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class TEEnergyInput extends TEEnergyInOut {

    protected TEEnergyInput(int meta) {
        super(new SlotDefinition(0, 0, 0, 0, -1, -1), new EnergyStorageAdv(MaterialRegistry.fromMeta(meta % 100)));
        this.meta = meta;
        material = MaterialRegistry.fromMeta(meta % LibResources.META1);
    }

    public TEEnergyInput() {
        this(0);
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
    protected boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return super.receiveEnergy(from, maxReceive, simulate);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return null;
    }

}
