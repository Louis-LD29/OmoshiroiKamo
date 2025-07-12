package com.louis.test.common.block.multiblock.fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.FluidUtil;
import com.louis.test.api.ModObject;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.api.io.IoMode;
import com.louis.test.api.io.IoType;
import com.louis.test.api.material.MaterialRegistry;
import com.louis.test.client.gui.modularui2.MGuis;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.block.multiblock.TileAddon;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.lwjgl.input.Keyboard;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileFluidInput extends TileAddon implements IFluidHandler {

    private int tankSlot = 0;
    private int maxTransfer = 100;
    private boolean didSyncFluid = false;

    public TileFluidInput() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            setIoMode(direction, IoMode.INPUT, IoType.FLUID);
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInput.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockFluidInput.unlocalisedName;
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
        if (worldObj.isRemote) return;

        if (hasValidController() && !didSyncFluid && getTargetTank() != null) {
            FluidStack fluid = getTargetTank().getFluid();
            getController().setTank(
                getController().getSlotDefinition()
                    .getMinFluidInputSlot() + tankSlot,
                FluidContainerRegistry.BUCKET_VOLUME * 8);
            getTargetTank().setFluid(fluid);
            didSyncFluid = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (hasValidController() && getTargetTank() != null) {
            FluidStack fluid = getTargetTank().getFluid();
            getController().setTank(
                getController().getSlotDefinition()
                    .getMinFluidInputSlot() + tankSlot,
                getController().TANK());
            getTargetTank().setFluid(fluid);
            didSyncFluid = false;
        }
    }

    private SmartTank getTargetTank() {
        List<SmartTank> inputTanks = getController().getInputTanks();
        return inputTanks.get(tankSlot);
    }

    @Override
    protected boolean doPull(ForgeDirection dir) {
        if (isSideDisabled(dir.ordinal(), IoType.FLUID)) {
            return false;
        }

        boolean res = super.doPull(dir);
        if (!hasValidController()) {
            return res;
        }

        if (getTargetTank().getFluidAmount() < getTargetTank().getCapacity()) {
            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {

                if (getTargetTank().getFluidAmount() > 0) {
                    FluidStack canPull = getTargetTank().getFluid()
                        .copy();
                    canPull.amount = getTargetTank().getCapacity() - getTargetTank().getFluidAmount();
                    canPull.amount = Math.min(canPull.amount, maxTransfer);
                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                    if (drained != null && drained.amount > 0) {
                        getTargetTank().fill(drained, true);
                        return res;
                    }
                } else {

                    FluidTankInfo[] infos = target.getTankInfo(dir.getOpposite());
                    if (infos != null) {
                        for (FluidTankInfo info : infos) {
                            if (info.fluid != null && info.fluid.amount > 0) {
                                if (canFill(dir, info.fluid.getFluid())) {
                                    FluidStack canPull = info.fluid.copy();
                                    canPull.amount = Math.min(maxTransfer, canPull.amount);
                                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                                    if (drained != null && drained.amount > 0) {
                                        getTargetTank().fill(drained, true);
                                        return res;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (!hasValidController() && !canFill(from)) return 0;
        return getTargetTank().fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return hasValidController() && canFill(from)
            && fluid != null
            && (getTargetTank().getFluidAmount() > 0 && getTargetTank().getFluid()
            .getFluidID() == fluid.getID() || getTargetTank().getFluidAmount() == 0);
    }

    private boolean canFill(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.FLUID);
        return mode != IoMode.OUTPUT && mode != IoMode.DISABLED;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return hasValidController() ? new FluidTankInfo[]{getTargetTank().getInfo()} : new FluidTankInfo[0];
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        nbtRoot.setBoolean("didSyncFluid", didSyncFluid);
        nbtRoot.setInteger("tankSlot", this.tankSlot);
        nbtRoot.setInteger("maxTransfer", this.maxTransfer);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        this.didSyncFluid = nbtRoot.getBoolean("didSyncFluid");
        this.tankSlot = nbtRoot.getInteger("tankSlot");
        this.maxTransfer = nbtRoot.getInteger("maxTransfer");

    }

    @Override
    public void setIoMode(ForgeDirection faceHit, IoMode mode, IoType type) {
        if (mode == IoMode.NONE && ioConfigs == null) {
            return;
        }

        if (ioConfigs == null) {
            ioConfigs = new EnumMap<IoType, Map<ForgeDirection, IoMode>>(IoType.class);
        }

        Map<ForgeDirection, IoMode> faceMap = ioConfigs.get(type);
        if (faceMap == null) {
            faceMap = new EnumMap<ForgeDirection, IoMode>(ForgeDirection.class);
            ioConfigs.put(type, faceMap);
        }

        if (mode == IoMode.OUTPUT && type == IoType.FLUID) mode = IoMode.DISABLED;

        faceMap.put(faceHit, mode);

        forceClientUpdate = true;
        notifyNeighbours = true;

        updateBlock();
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.syncValue("tankSlot", new IntSyncValue(() -> this.tankSlot, val -> this.tankSlot = val));
        syncManager.syncValue("maxTransfer", new IntSyncValue(() -> this.maxTransfer, val -> this.maxTransfer = val));

        ModularPanel panel = MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .doesAddConfigR(false)
            .doesAddConfigIOFluid(true)
            .build();

        if (!hasValidController() || getTargetTank() == null) {
            return panel;
        }

        List<SmartTank> inputTanks = getController().getInputTanks();

        Flow column = new Column().childPadding(2);

        column.child(
            new Row().alignX(Alignment.Center)
                .marginTop(7)
                .coverChildren()
                .child(
                    new ButtonWidget<>().size(8, 8)
                        .overlay(GuiTextures.REMOVE)
                        .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                            int decrement = 1;
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                decrement = 10;
                            }
                            this.maxTransfer = Math.max(0, this.maxTransfer - decrement);
                        }))
                        .margin(4))
                .child(
                    IKey.dynamic(() -> "Max Transfer\n" + this.maxTransfer)
                        .alignment(Alignment.Center)

                        .asWidget())

                .child(
                    new ButtonWidget<>().size(8, 8)
                        .overlay(GuiTextures.ADD)
                        .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                            int increment = 1;
                            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                increment = 10;
                            }
                            this.maxTransfer = this.maxTransfer + increment;
                        }))
                        .margin(4)));

        column.child(
            IKey.dynamic(() -> "TankSlot: " + this.tankSlot)
                .asWidget());

        Flow row = new Row().alignX(Alignment.Center)
            .coverChildren();

        row.child(
            new ButtonWidget<>().size(8, 8)
                .overlay(GuiTextures.REMOVE)
                .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (!inputTanks.isEmpty()) {
                        this.tankSlot = (tankSlot - 1 + inputTanks.size()) % inputTanks.size();
                    }
                }))
                .margin(4));

        ParentWidget<?> fluidStack = new ParentWidget<>().size(18, 18)
            .coverChildren();

        for (int i = 0; i < inputTanks.size(); i++) {
            SmartTank tank = inputTanks.get(i);
            int finalI = i;

            FluidSlot slot = new FluidSlot().syncHandler(new FluidSlotSyncHandler(tank).canDrainSlot(true))
                .setEnabledIf(f -> tankSlot == finalI)
                .size(18, 18);
            slot.tooltip(richTooltip -> {
            });

            fluidStack.child(slot);
        }

        row.child(fluidStack);

        row.child(
            new ButtonWidget<>().size(8, 8)
                .overlay(GuiTextures.ADD)
                .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (!inputTanks.isEmpty()) {
                        this.tankSlot = (tankSlot + 1) % inputTanks.size();
                    }
                }))
                .margin(4));

        column.child(row);

        column.child(
            new ButtonWidget<>().size(16, 16)
                .overlay(new ItemDrawable(Items.bucket).asIcon())
                .tooltip(richTooltip -> {
                    richTooltip.showUpTimer(2);
                    richTooltip.addLine(IKey.str("Dump Fluid"));
                })
                .syncHandler(
                    new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                        getTargetTank().setFluid(null);
                    })));

        panel.child(column);

        return panel;
    }

}
