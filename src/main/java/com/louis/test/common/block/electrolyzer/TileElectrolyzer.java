package com.louis.test.common.block.electrolyzer;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.machine.AbstractProcessingEntity;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.gui.modularui2.MGuis;

public class TileElectrolyzer extends AbstractProcessingEntity {

    public TileElectrolyzer() {
        super(new SlotDefinition(0, 2, 3, 5, 0, 2, 3, 5, -1, -1));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockElectrolyzer.unlocalisedName;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null) return false;
        return slot >= slotDefinition.minItemInputSlot && slot <= slotDefinition.maxItemInputSlot;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.registerSlotGroup("item_inv", 1);
        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .build()
            .child(
                new Column().child(
                    SlotGroupWidget.builder()
                        .matrix("III>III", "FFF FFF")
                        .key('I', index -> {
                            return new ItemSlot().slot(
                                new ModularSlot(this.inv, index).slotGroup("item_inv")
                                    .filter(stack -> isItemValidForSlot(index, stack)))
                                .debugName("Slot " + index);
                        })
                        .key('F', index -> {
                            return new FluidSlot()
                                .syncHandler(
                                    new FluidSlotSyncHandler(fluidTanks[index])
                                        .canFillSlot(index <= slotDefinition.maxFluidInputSlot))
                                .debugName("Slot " + index);
                        })
                        .build()));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtRoot) {
        super.writeCustomNBT(nbtRoot);
    }
}
