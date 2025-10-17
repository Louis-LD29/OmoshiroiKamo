package ruiseki.omoshiroikamo.common.block.multiblock.part.energy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import ruiseki.omoshiroikamo.api.energy.EnergyStorageAdv;
import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.io.SlotDefinition;
import ruiseki.omoshiroikamo.api.material.MaterialRegistry;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

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
