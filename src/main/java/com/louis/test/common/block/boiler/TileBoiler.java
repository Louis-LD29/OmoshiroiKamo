package com.louis.test.common.block.boiler;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.block.multiblock.TileMain;
import com.louis.test.common.fluid.ModFluids;

public class TileBoiler extends TileMain {

    public TileBoiler() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockBoiler.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockBoiler.unlocalisedName;
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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return super.buildUI(data, syncManager, settings).child(
            new Column().child(
                new FluidSlot().syncHandler(
                    new FluidSlotSyncHandler(getInputTank()).canDrainSlot(
                        getInputTank().getFluidAmount() >= 1000 && !getInputTank().getFluid()
                            .getFluid()
                            .equals(ModFluids.fluidMana))

                ))
                .child(
                    new FluidSlot().syncHandler(
                        new FluidSlotSyncHandler(getOutputTank()).canDrainSlot(
                            getOutputTank().getFluidAmount() >= 1000 && !getOutputTank().getFluid()
                                .getFluid()
                                .equals(ModFluids.fluidMana))

                    )));
    }
}
