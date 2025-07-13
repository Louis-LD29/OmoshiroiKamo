package com.louis.test.common.block.basicblock.heatsource;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.enums.BlockMassType;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.heat.HeatStorage;
import com.louis.test.api.heat.HeatUtil;
import com.louis.test.api.heat.IHeatHandler;
import com.louis.test.api.io.IoMode;
import com.louis.test.api.io.IoType;
import com.louis.test.api.material.MaterialRegistry;
import com.louis.test.common.block.basicblock.machine.AbstractMachineEntity;
import com.louis.test.common.block.basicblock.machine.SlotDefinition;

public class TileHeatSource extends AbstractMachineEntity implements IHeatHandler {

    private final HeatStorage heat = new HeatStorage(MaterialRegistry.get("Copper"), BlockMassType.BLOCK);

    public TileHeatSource() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
        heat.setMaxTransfer(10000);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockHeatSource.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockHeatSource.unlocalisedName;
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
    public void doUpdate() {
        super.doUpdate();
        heat.setHeatStored(1000);

    }

    @Override
    protected boolean doPush(ForgeDirection dir) {
        if (isSideDisabled(dir.ordinal(), IoType.HEAT)) {
            return false;
        }

        boolean res = super.doPush(dir);

        BlockCoord loc = getLocation().getLocation(dir);
        IHeatHandler target = HeatUtil.getHeatHandler(worldObj, loc);

        if (target != null && target.canReceiveHeat(dir.getOpposite())) {
            float sourceTemp = heat.getHeatStored();
            float targetTemp = target.getHeat()
                .getHeatStored();

            if (sourceTemp > targetTemp) {
                float transfer = (sourceTemp - targetTemp) * 0.1f;
                transfer = Math.min(transfer, heat.getMaxTransfer());

                float actualTransferred = target.receiveHeat(dir.getOpposite(), transfer, true);

                if (actualTransferred > 0) {
                    heat.extractHeat(actualTransferred, true);
                    return res;
                }
            }
        }

        return res;
    }

    @Override
    public HeatStorage getHeat() {
        return heat;
    }

    @Override
    public float extractHeat(ForgeDirection from, float amount, boolean doTransfer) {
        if (!canExtractHeat(from)) {
            return 0;
        }
        return heat.extractHeat(amount, doTransfer);
    }

    @Override
    public float receiveHeat(ForgeDirection from, float amount, boolean doTransfer) {
        if (!canReceiveHeat(from)) {
            return 0;
        }
        return heat.receiveHeat(amount, doTransfer);
    }

    @Override
    public boolean canExtractHeat(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.HEAT);
        return mode != IoMode.OUTPUT && mode != IoMode.DISABLED;
    }

    @Override
    public boolean canReceiveHeat(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.HEAT);
        return mode != IoMode.INPUT && mode != IoMode.DISABLED;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.syncValue(
            "heat",
            new DoubleSyncValue(() -> (double) this.heat.getHeatStored(), val -> this.heat.setHeatStored((float) val)));

        return super.buildUI(data, syncManager, settings).child(
            new ParentWidget<>().debugName("root parent")
                .sizeRel(1f)
                .child(
                    new ParentWidget<>().debugName("page 1 parent")
                        .sizeRel(1f, 1f)
                        .padding(7)
                        .child(
                            new Row().height(71)
                                .width(140)
                                .child(
                                    new Column().childPadding(2)
                                        .padding(3)
                                        .background(GuiTextures.DISPLAY)
                                        .child(
                                            IKey.dynamic(
                                                () -> "Heat: " + heat.getHeatStored() + "/" + heat.getMaxHeatStored())
                                                .color(0xFFFFFFFF)
                                                .asWidget()
                                                .marginTop(2))))));
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        heat.writeCommon(nbtRoot);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        heat.readCommon(nbtRoot);
    }
}
