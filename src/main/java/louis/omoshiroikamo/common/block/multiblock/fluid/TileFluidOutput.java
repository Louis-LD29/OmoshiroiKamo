package louis.omoshiroikamo.common.block.multiblock.fluid;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.lwjgl.input.Keyboard;

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

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.api.io.IoMode;
import louis.omoshiroikamo.api.io.IoType;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.client.gui.modularui2.MGuis;
import louis.omoshiroikamo.common.block.basicblock.machine.SlotDefinition;
import louis.omoshiroikamo.common.block.multiblock.TileAddon;
import louis.omoshiroikamo.common.block.multiblock.TileMain;
import louis.omoshiroikamo.common.block.multiblock.boiler.TileBoiler;

public class TileFluidOutput extends TileAddon implements IFluidHandler {

    private int tankSlot = 0;
    private int maxTransfer = 100;
    private boolean didSyncFluid = false;

    public TileFluidOutput() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            setIoMode(direction, IoMode.OUTPUT, IoType.FLUID);
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidOutput.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockFluidOutput.unlocalisedName;
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
                    .getMinFluidOutputSlot() + tankSlot,
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
                    .getMinFluidOutputSlot() + tankSlot,
                getController().TANK());
            getTargetTank().setFluid(fluid);
            didSyncFluid = false;
        }
    }

    private SmartTank getTargetTank() {
        List<SmartTank> outputTanks = getController().getOutputTanks();
        return outputTanks.get(tankSlot);
    }

    @Override
    public void findAndSetController() {
        if (hasValidController()) {
            getController().unregisterAddon(this);
            setController(null);
        }

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (te instanceof TileBoiler) {
                setController((TileMain) te);
                getController().registerAddon(this);
                return;
            }

            if (te instanceof TileAddon otherAddon && otherAddon != this && otherAddon.hasValidController()) {
                TileMain potential = otherAddon.getController();
                if (potential instanceof TileBoiler potentialBoiler && !potentialBoiler.isInvalid()) {
                    setController(potentialBoiler);
                    getController().registerAddon(this);
                    return;
                }
            }
        }
    }

    @Override
    public TileBoiler getController() {
        return (TileBoiler) super.getController();
    }

    @Override
    protected boolean doPush(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal(), IoType.FLUID)) {
            return false;
        }

        boolean res = super.doPush(dir);
        if (!hasValidController()) {
            return res;
        }

        if (getTargetTank().getFluidAmount() > 0) {

            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {
                if (target.canFill(
                    dir.getOpposite(),
                    getTargetTank().getFluid()
                        .getFluid())) {
                    FluidStack push = getTargetTank().getFluid()
                        .copy();
                    push.amount = Math.min(push.amount, maxTransfer);
                    int filled = target.fill(dir.getOpposite(), push, true);
                    if (filled > 0) {
                        getTargetTank().drain(filled, true);
                        return res;
                    }
                }
            }
        }
        return res;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!canDrain(from) && !hasValidController()) {
            return null;
        }
        return getTargetTank().drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (!canDrain(from) && !hasValidController()) {
            return null;
        }
        return getTargetTank().drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return canDrain(from) && getTargetTank().canDrainFluidType(fluid) && hasValidController();
    }

    private boolean canDrain(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.FLUID);
        return mode != IoMode.INPUT && mode != IoMode.DISABLED;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return hasValidController() ? new FluidTankInfo[] { getTargetTank().getInfo() } : new FluidTankInfo[0];
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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.syncValue("tankSlot", new IntSyncValue(() -> this.tankSlot, val -> this.tankSlot = val));
        syncManager.syncValue("maxTransfer", new IntSyncValue(() -> this.maxTransfer, val -> this.maxTransfer = val));

        ModularPanel panel = MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .doesAddConfigIOFluid(true)
            .doesAddConfigR(false)
            .build();

        if (!hasValidController() || getTargetTank() == null) {
            return panel;
        }

        List<SmartTank> outputTanks = getController().getOutputTanks();
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
                    if (!outputTanks.isEmpty()) {
                        this.tankSlot = (tankSlot - 1 + outputTanks.size()) % outputTanks.size();
                    }
                }))
                .margin(4));

        ParentWidget<?> fluidStack = new ParentWidget<>().size(18, 18)
            .coverChildren();

        for (int i = 0; i < outputTanks.size(); i++) {
            SmartTank tank = outputTanks.get(i);
            int finalI = i;

            FluidSlot slot = new FluidSlot().syncHandler(new FluidSlotSyncHandler(tank).canDrainSlot(true))
                .setEnabledIf(f -> tankSlot == finalI)
                .size(18, 18);
            slot.tooltip(richTooltip -> {});
            fluidStack.child(slot);
        }

        row.child(fluidStack);

        row.child(
            new ButtonWidget<>().size(8, 8)
                .overlay(GuiTextures.ADD)
                .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                    if (!outputTanks.isEmpty()) {
                        this.tankSlot = (tankSlot + 1) % outputTanks.size();
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
                    new InteractionSyncHandler().setOnMousePressed(mouseData -> { getTargetTank().setFluid(null); })));

        panel.child(column);

        return panel;
    }
}
