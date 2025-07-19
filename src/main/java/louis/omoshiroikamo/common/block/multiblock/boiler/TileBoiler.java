package louis.omoshiroikamo.common.block.multiblock.boiler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget.Direction;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.client.gui.modularui2.MGuiTextures;
import louis.omoshiroikamo.client.gui.modularui2.MGuis;
import louis.omoshiroikamo.common.block.basicblock.machine.SlotDefinition;
import louis.omoshiroikamo.common.block.multiblock.TileMain;

public class TileBoiler extends TileMain {

    SteamCalculator steamC = new SteamCalculator();
    double simulateWaterUsedMB = 0f;
    double simulateSteamMB = 0f;
    double pressureAtm = 0f;
    double tBoil = 0f;

    public TileBoiler() {
        super(new SlotDefinition(-1, -1, -1, -1, 0, 0, 1, 1, -1, -1), MaterialRegistry.get("Iron"));
        setHeatStorage(HEATSTORAGE());
    }

    @Override
    public String getMachineName() {
        return ModObject.blockBoiler.unlocalisedName;
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
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        nbtRoot.setDouble("simulateWaterUsedMB", simulateWaterUsedMB);
        nbtRoot.setDouble("simulateSteamMB", simulateSteamMB);
        nbtRoot.setDouble("pressureAtm", pressureAtm);
        nbtRoot.setDouble("tBoil", tBoil);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        nbtRoot.getDouble("simulateWaterUsedMB");
        nbtRoot.getDouble("simulateSteamMB");
        nbtRoot.getDouble("pressureAtm");
        nbtRoot.getDouble("tBoil");
    }

    @Override
    public void doUpdate() {
        super.doUpdate();

        boolean shouldUpdate = shouldDoWorkThisTick(20, 0);
        if (!shouldUpdate) return;

        SmartTank inputTank = getTank(0);
        SmartTank outputTank = getTank(1);
        float currentHeat = getHeat().getHeatStored();

        // Cập nhật trạng thái vào steam calculator
        steamC.setTankCapacityMB(outputTank.getCapacity() + inputTank.getFluidAmount());
        steamC.setWaterVolumeMB(inputTank.getFluidAmount());
        steamC.setSteamVolumeMB(outputTank.getFluidAmount());
        steamC.setHeatSourceTempK(currentHeat);
        steamC.setN(getFluidFilter() ? 1f : 0.7f);

        // Client chỉ cần cập nhật mô phỏng
        if (worldObj.isRemote) return;

        // Mô phỏng
        simulateWaterUsedMB = steamC.simulateWaterUsedMB();
        simulateSteamMB = steamC.simulateSteamMB();
        pressureAtm = steamC.getPressureAtm();
        tBoil = steamC.getTboilK();
        // Kiểm tra điều kiện sinh hơi
        if (simulateWaterUsedMB <= 0 || simulateSteamMB <= 0) return;
        if (currentHeat < tBoil + 5) return;

        FluidStack inputFluid = inputTank.getFluid();
        if (inputFluid == null || inputFluid.getFluid() != FluidRegistry.WATER) return;
        if (inputTank.getFluidAmount() < simulateWaterUsedMB) return;

        int outputRoom = outputTank.getCapacity() - outputTank.getFluidAmount();
        if (outputRoom <= 0) return;

        // Tính toán hơi thực sự tạo ra
        double generatedSteamMB = steamC.calculateSteamStep(); // thực sự trừ nước & sinh hơi
        int steamToInsert = Math.min((int) generatedSteamMB, outputRoom);

        if (steamToInsert <= 0) return;

        // Cập nhật nhiệt
        getHeat().setHeatStored((float) steamC.getHeatSourceTempK());

        // Bơm hơi vào tank
        FluidStack currentSteam = outputTank.getFluid();
        // if (currentSteam == null || currentSteam.getFluid() != ModFluids.fluidSteam) {
        // outputTank.setFluid(new FluidStack(ModFluids.fluidSteam, steamToInsert));
        // } else {
        // currentSteam.amount += steamToInsert;
        // }
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.syncValue(
            "simulateWaterUsedMB",
            new DoubleSyncValue(() -> this.simulateWaterUsedMB, value -> simulateWaterUsedMB = value));
        syncManager.syncValue(
            "simulateSteamMB",
            new DoubleSyncValue(() -> this.simulateSteamMB, value -> simulateSteamMB = value));

        syncManager.syncValue("pressureAtm", new DoubleSyncValue(() -> this.pressureAtm, value -> pressureAtm = value));

        syncManager.syncValue("tBoil", new DoubleSyncValue(() -> this.tBoil, value -> tBoil = value));

        syncManager.syncValue(
            "heat",
            new DoubleSyncValue(() -> getHeat().getHeatStored(), val -> getHeat().setHeatStored((float) val)));

        syncManager.syncValue(
            "heatStorage",
            new DoubleSyncValue(
                () -> (double) getHeat().getMaxHeatStored(),
                val -> getHeat().setMaxHeatStored((float) val)));

        syncManager.syncValue("fluidFilter", new BooleanSyncValue(this::getFluidFilter, this::setFluidFilter));

        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .setHeight(196)
            .build()
            .child(
                new ParentWidget<>().debugName("root parent")
                    .sizeRel(1f)
                    .child(
                        new ParentWidget<>().debugName("page 1 parent")
                            .sizeRel(1f, 1f)
                            .padding(7)
                            .child(
                                new Row().height(101)
                                    .width(140)
                                    .child(
                                        new Column().childPadding(2)
                                            .padding(3)
                                            .background(GuiTextures.DISPLAY)
                                            .child(
                                                IKey.dynamic(
                                                    () -> "Temp:\n"
                                                        + Math.round(steamC.getHeatSourceTempK() * 100.0f) / 100.0f
                                                        + "K"
                                                        + "/"
                                                        + getHeat().getMaxHeatStored()
                                                        + "K")
                                                    .color(0xFFFFFFFF)
                                                    .alignment(Alignment.Center)
                                                    .asWidget())
                                            .child(
                                                IKey.dynamic(() -> { return "Has Filter: " + getFluidFilter(); })

                                                    .color(0xFFFFFFFF)
                                                    .asWidget())
                                            .child(
                                                IKey.dynamic(
                                                    () -> {
                                                        return "Pressure: "
                                                            + Math.round(this.pressureAtm * 100.0f) / 100.0f
                                                            + " atm";
                                                    })

                                                    .color(0xFFFFFFFF)
                                                    .asWidget())
                                            .child(
                                                IKey.dynamic(
                                                    () -> {
                                                        return "Boiling Temp: "
                                                            + Math.round(this.tBoil * 100.0f) / 100.0f
                                                            + "K";
                                                    })

                                                    .color(0xFFFFFFFF)
                                                    .asWidget())
                                            .child(
                                                IKey.dynamic(
                                                    () -> {
                                                        return "Steam Generate:\n "
                                                            + Math.round(this.simulateSteamMB * 100.0f) / 100.0f
                                                            + "L/s";
                                                    })

                                                    .color(0xFFFFFFFF)
                                                    .asWidget())
                                            .child(
                                                IKey.dynamic(
                                                    () -> {
                                                        return "Water Consume:\n "
                                                            + Math.round(this.simulateWaterUsedMB * 100.0f) / 100.0f
                                                            + "L/s";
                                                    })

                                                    .color(0xFFFFFFFF)
                                                    .asWidget())
                                    //
                                    ))))
            .child(
                new Column().debugName("Slot Row")
                    .top(0)
                    .leftRelOffset(1f, 1)
                    .background(GuiTextures.MC_BACKGROUND)
                    .excludeAreaInNEI()
                    .coverChildren()
                    .padding(4)
                    .childPadding(2)
                    .child(
                        new FluidSlot().syncHandler(
                            new FluidSlotSyncHandler(getTank(1))
                                .canDrainSlot(getTank(1).getFluidAmount() >= 1000 && getTank(1).getFluid() != null)))
                    .child(
                        new FluidSlot()
                            .syncHandler(
                                new FluidSlotSyncHandler(getTank(0))
                                    .canDrainSlot(getTank(0).getFluidAmount() >= 1000 && getTank(0).getFluid() != null))
                            .marginTop(4))
                    .child(
                        new ProgressWidget().progress(() -> getHeat().getHeatStored() / (double) 100f)
                            .texture(MGuiTextures.PROGRESS_BURN, 16)
                            .direction(Direction.UP)));
    }

}
