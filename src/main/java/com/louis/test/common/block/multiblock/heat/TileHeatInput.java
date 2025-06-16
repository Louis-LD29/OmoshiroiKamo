package com.louis.test.common.block.multiblock.heat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.enums.*;
import com.louis.test.api.interfaces.heat.HeatStorage;
import com.louis.test.api.interfaces.heat.HeatUtil;
import com.louis.test.api.interfaces.heat.IHeatHandler;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.block.multiblock.TileAddon;
import com.louis.test.common.gui.modularui2.MGuis;

public class TileHeatInput extends TileAddon implements IHeatHandler {

    private boolean didSyncHeat = false;
    private float temperatureLimit = 0f;
    private String timeToReachTemperature = "";

    public TileHeatInput() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1));
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            setIoMode(direction, IoMode.INPUT, IoType.HEAT);
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockHeatInput.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockHeatInput.unlocalisedName;
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
    public void verifyConnectionToController() {
        super.verifyConnectionToController();

        didSyncHeat = false;
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        if (worldObj.isRemote) return;

        if (!didSyncHeat && hasValidController() && getController().getHeat() != null) {

            HeatStorage current = getHeat();
            HeatStorage expected = new HeatStorage(Material.COPPER, BlockMassType.HEAT_INOUT);

            // So sánh maxHeat để biết có cần thay không
            if (current.getMaxHeatStored() != expected.getMaxHeatStored()) {
                float heat = current.getHeatStored();
                getController().setHeatStorage(expected);
                getController().getHeat()
                    .setHeatStored(heat);

                if (temperatureLimit > getHeat().getMaxHeatStored() || temperatureLimit < 1) {
                    temperatureLimit = getHeat().getMaxHeatStored();
                }

            }

            didSyncHeat = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (hasValidController() && getHeat() != null) {
            float heat = getHeat().getHeatStored();
            getController().setHeatStorage(getController().HEATSTORAGE());
            getHeat().setHeatStored(heat);
            didSyncHeat = false;
            temperatureLimit = 0f;
        }
    }

    @Override
    public void validate() {
        super.validate();
        didSyncHeat = false;
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        nbtRoot.setBoolean("didSyncHeat", didSyncHeat);
        nbtRoot.setFloat("temperatureLimit", temperatureLimit);
        nbtRoot.setString("timeToReachTemperature", timeToReachTemperature);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        didSyncHeat = nbtRoot.getBoolean("didSyncHeat");
        temperatureLimit = nbtRoot.getFloat("temperatureLimit");
        timeToReachTemperature = nbtRoot.getString("timeToReachTemperature");
    }

    @Override
    protected boolean doPull(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal(), IoType.HEAT)) {
            return false;
        }
        boolean res = super.doPull(dir);
        if (!hasValidController()) {
            return res;
        }

        BlockCoord loc = getLocation().getLocation(dir);
        IHeatHandler target = HeatUtil.getHeatHandler(worldObj, loc);

        if (target != null && target.canExtractHeat(dir.getOpposite())
            && getHeat().getHeatStored() <= this.temperatureLimit) {
            float sourceTemp = getHeat().getHeatStored();
            float targetTemp = target.getHeat()
                .getHeatStored();
            float deltaT = targetTemp - sourceTemp;

            if (deltaT > 0.01f) {
                float limitingHeatCapacity = Math.min(
                    target.getHeat()
                        .getHeatCapacity(),
                    getHeat().getHeatCapacity());

                float idealTransfer = deltaT * limitingHeatCapacity;

                float maxTransfer = Math.min(
                    idealTransfer,
                    Math.min(
                        getHeat().getMaxTransfer(),
                        target.getHeat()
                            .getMaxTransfer()));
                this.timeToReachTemperature = getHeat()
                    .calculateTimeToReachTemperature(maxTransfer, getHeat().getHeatStored(), temperatureLimit);

                float actualTransferred = target.extractHeat(dir.getOpposite(), maxTransfer, true);
                if (actualTransferred > 0) {
                    getHeat().receiveHeat(actualTransferred, true);
                    this.timeToReachTemperature = getHeat().calculateTimeToReachTemperature(
                        actualTransferred,
                        getHeat().getHeatStored(),
                        temperatureLimit);
                    return true;
                }
            }
        }

        return res;
    }

    @Override
    public HeatStorage getHeat() {
        return getController().getHeat();
    }

    @Override
    public float receiveHeat(ForgeDirection from, float amount, boolean doTransfer) {
        if (!hasValidController() && !canReceiveHeat(from)) {
            return 0;
        }
        return getHeat().receiveHeat(amount, doTransfer);
    }

    @Override
    public boolean canReceiveHeat(ForgeDirection from) {
        IoMode mode = getIoMode(from);
        return hasValidController() && mode != IoMode.INPUT && mode != IoMode.DISABLED;
    }

    @Override
    public float extractHeat(ForgeDirection from, float amount, boolean doTransfer) {
        return 0;
    }

    @Override
    public boolean canExtractHeat(ForgeDirection from) {
        IoMode mode = getIoMode(from);
        return mode != IoMode.OUTPUT && mode != IoMode.DISABLED;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        ModularPanel panel = MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .doesAddConfigIOHeat(true)
            .setHeight(190)
            .build();
        if (!hasValidController() || getHeat() == null) {
            return panel;
        }

        syncManager.syncValue(
            "heat",
            new DoubleSyncValue(() -> (double) getHeat().getHeatStored(), val -> getHeat().setHeatStored((float) val)));

        syncManager.syncValue(
            "heatCapacity",
            new DoubleSyncValue(
                () -> (double) getHeat().getHeatCapacity(),
                val -> getHeat().setHeatCapacity((float) val)));

        syncManager.syncValue(
            "maxReceive",
            new DoubleSyncValue(
                () -> (double) getHeat().getMaxTransfer(),
                val -> getHeat().setMaxTransfer((float) val)));

        syncManager.syncValue(
            "MaxHeatStored",
            new DoubleSyncValue(
                () -> (double) getHeat().getMaxHeatStored(),
                val -> getHeat().setMaxHeatStored((float) val)));

        syncManager.syncValue(
            "temperatureLimit",
            new DoubleSyncValue(() -> (double) this.temperatureLimit, val -> this.temperatureLimit = (float) val));

        syncManager.syncValue(
            "timeToReachTemperature",
            new StringSyncValue(() -> this.timeToReachTemperature, val -> this.timeToReachTemperature = val));

        panel.child(
            new Column().childPadding(2)
                .child(
                    new Row().marginTop(7)
                        .alignX(Alignment.Center)
                        .coverChildren()
                        .child(
                            IKey.dynamic(() -> "Current Temperature\n" + getHeat().getHeatStored() + " °C")
                                .alignment(Alignment.Center)
                                .asWidget()))
                .child(
                    new Row().alignX(Alignment.Center)
                        .coverChildren()
                        .child(
                            IKey.dynamic(() -> "Heat Capacity\n" + getHeat().getHeatCapacity() + " J/°C")
                                .alignment(Alignment.Center)
                                .asWidget()))
                .child(
                    new Row().alignX(Alignment.Center)
                        .coverChildren()
                        .child(
                            IKey.dynamic(() -> "Time To Reach\n" + this.timeToReachTemperature)
                                .alignment(Alignment.Center)
                                .asWidget()))
                .child(
                    new Row().alignX(Alignment.Center)
                        .coverChildren()
                        .child(
                            IKey.dynamic(() -> "Max Transfer\n" + getHeat().getMaxTransfer() + " J/t")
                                .alignment(Alignment.Center)
                                .asWidget()))
                .child(
                    new Row().alignX(Alignment.Center)
                        .coverChildren()
                        .child(
                            new ButtonWidget<>().size(8, 8)
                                .overlay(GuiTextures.REMOVE)
                                .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                                    float decrement = 1f;
                                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
                                        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                        decrement = 0.1f;
                                    } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
                                        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                            decrement = 10f;
                                        }
                                    this.temperatureLimit = Math.max(0, this.temperatureLimit - decrement);
                                }))
                                .margin(4))
                        .child(
                            IKey.dynamic(
                                () -> "Temperature Limit\n" + this.temperatureLimit
                                    + "/"
                                    + getHeat().getMaxHeatStored()
                                    + " °C")
                                .alignment(Alignment.Center)
                                .asWidget())

                        .child(
                            new ButtonWidget<>().size(8, 8)
                                .overlay(GuiTextures.ADD)
                                .syncHandler(new InteractionSyncHandler().setOnMousePressed(mouseData -> {
                                    float increment = 1f;
                                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
                                        || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                        increment = 0.1f;
                                    } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
                                        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                                            increment = 10f;
                                        }
                                    this.temperatureLimit = Math
                                        .min(this.temperatureLimit + increment, getHeat().getMaxHeatStored());
                                }))
                                .margin(4))));

        return panel;
    }
}
