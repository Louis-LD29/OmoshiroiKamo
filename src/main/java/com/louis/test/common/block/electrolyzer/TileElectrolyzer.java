package com.louis.test.common.block.electrolyzer;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.*;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.machine.AbstractProcessingEntity;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.config.Config;
import com.louis.test.common.gui.modularui2.MGuis;

public class TileElectrolyzer extends AbstractProcessingEntity {

    public TileElectrolyzer() {
        super(new SlotDefinition(0, 2, 3, 5, 0, 2, 3, 5, -1, -1), Material.IRON);
    }

    @Override
    public int getPowerUsePerTick() {
        return Config.PowerUserPerTickRF;
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
        syncManager.syncValue("progress", new DoubleSyncValue(this::getProgress, value -> setProgress((float) value)));

        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .doesAddConfigR(true)
            .doesAddConfigIOHeat(true)
            .doesAddConfigIOFluid(true)
            .doesAddConfigIOItem(true)
            .build()
            .child(
                new Column().child(
                    SlotGroupWidget.builder()
                        .matrix("IIISSIII", "FFFSSFFF")
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
                        .build()
                        .topRel(0.1f)
                        .alignX(Alignment.CENTER))
                    .child(
                        new ProgressWidget().progress(this::getProgress)
                            .topRel(0.1f)
                            .leftRel(0.5f)
                            .texture(GuiTextures.PROGRESS_ARROW, 20))

                    .child(
                        new ToggleButton().size(18, 18)
                            .overlay(true, GuiTextures.LOCKED)
                            .overlay(false, GuiTextures.UNLOCKED)
                            .value(new BooleanSyncValue(this::isRecipeLocked, this::setRecipeLocked))
                            .tooltipDynamic(richTooltip -> {
                                richTooltip.add(IKey.str("Predicted Outputs:\n"));

                                List<ItemStack> items = getItemOutput();
                                List<FluidStack> fluids = getFluidOutput();

                                if (items.isEmpty() && fluids.isEmpty()) {
                                    richTooltip.add(IKey.str(" - No valid recipe\n"));
                                } else {
                                    for (ItemStack item : items) {
                                        if (item != null) {
                                            richTooltip.add(
                                                IKey.str(" - " + item.stackSize + "x " + item.getDisplayName() + "\n"));
                                        }
                                    }
                                    for (FluidStack fluid : fluids) {
                                        if (fluid != null && fluid.getFluid() != null) {
                                            richTooltip.add(
                                                IKey.str(
                                                    " - " + fluid.amount
                                                        + " mB of "
                                                        + fluid.getLocalizedName()
                                                        + "\n"));
                                        }
                                    }
                                }

                                richTooltip.markDirty();
                            })
                            .selectedBackground(GuiTextures.MC_BUTTON)
                            .selectedHoverBackground(GuiTextures.MC_BUTTON_HOVERED)
                            .topRel(0.21f)
                            .leftRel(0.5f)));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtRoot) {
        super.writeCustomNBT(nbtRoot);
    }
}
