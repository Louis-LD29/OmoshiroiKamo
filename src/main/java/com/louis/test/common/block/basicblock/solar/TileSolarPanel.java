package com.louis.test.common.block.basicblock.solar;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.energy.PowerDistributor;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.fluid.IFluidCoolant;
import com.louis.test.api.io.IoMode;
import com.louis.test.api.io.IoType;
import com.louis.test.client.gui.modularui2.MGuis;
import com.louis.test.common.block.basicblock.machine.AbstractGeneratorEntity;
import com.louis.test.common.block.basicblock.machine.SlotDefinition;
import com.louis.test.common.fluid.FluidFuelRegister;

import cofh.api.energy.EnergyStorage;

public class TileSolarPanel extends AbstractGeneratorEntity {

    private PowerDistributor powerDis;

    private int lastCollectionValue = -1;

    private int dustLevel = 0;
    private float temperatureCelsius = 25f;

    private static final int CHECK_INTERVAL = 100;

    public TileSolarPanel() {
        super(new SlotDefinition(0, 1, -1, -1, -1, -1));
        this.setEnergyStorage(new EnergyStorage(1000, 400, 200));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockSolar.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockSolar.unlocalisedName;
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
        nbtRoot.setInteger("dustLevel", this.dustLevel);
        nbtRoot.setFloat("temperatureC", temperatureCelsius);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        this.dustLevel = nbtRoot.getInteger("dustLevel");
        this.temperatureCelsius = nbtRoot.getFloat("temperatureC");
    }

    @Override
    public void onNeighborBlockChange(Block blockId) {
        super.onNeighborBlockChange(blockId);
        if (powerDis != null) {
            powerDis.neighboursChanged();
        }

    }

    @Override
    public void doUpdate() {
        super.doUpdate();

        if (worldObj.isRemote) {
            return;
        }
        collectEnergy();
        transmitEnergy();

        if (worldObj.getTotalWorldTime() % 24000 == 0) {
            if (dustLevel < 50) dustLevel++;
        }

        if (worldObj.getTotalWorldTime() % 100 == 0) {
            updateTemperature();
        }

        if (worldObj.isRaining()) {
            if (dustLevel > 0) dustLevel = 0;
        }

    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.ENERGY);
        return mode != IoMode.INPUT && mode != IoMode.DISABLED;
    }

    private int getEnergyPerTick() {
        return 40;
    }

    private boolean transmitEnergy() {
        if (powerDis == null) {
            powerDis = new PowerDistributor(new BlockCoord(this));
        }
        int canTransmit = Math.min(getEnergyStored(), getPowerUsePerTick() * 2);
        if (canTransmit <= 0) {
            return false;
        }
        int transmitted = powerDis.transmitEnergy(worldObj, canTransmit);
        setEnergyStored(getEnergyStored() - transmitted);
        return transmitted > 0;
    }

    private void collectEnergy() {
        if (canSeeSun()) {
            if (lastCollectionValue == -1 || shouldDoWorkThisTick(CHECK_INTERVAL)) {
                lastCollectionValue = getEnergyRegen();
            }
            if (lastCollectionValue > 0) {
                this.setEnergyStored(Math.min(lastCollectionValue + this.getEnergyStored(), this.getMaxEnergyStored()));
            }
        }
    }

    private int getEnergyRegen() {
        float fromSun = calculateLightRatio();
        float fromTemp = calculateTemperatureEfficiency();
        float fromDust = calculateDustLevelEfficiency();
        float fromWeather = calculateWeatherEfficiency();
        return Math.round(getEnergyPerTick() * fromSun * fromTemp * fromDust * fromWeather);
    }

    float calculateLightRatio() {
        return calculateLightRatio(worldObj, xCoord, yCoord, zCoord);
    }

    boolean canSeeSun() {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
    }

    public static float calculateLightRatio(World world, int x, int y, int z) {
        int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
        float sunAngle = world.getCelestialAngleRadians(1.0F);

        if (sunAngle < (float) Math.PI) {
            sunAngle += (0.0F - sunAngle) * 0.2F;
        } else {
            sunAngle += (((float) Math.PI * 2F) - sunAngle) * 0.2F;
        }

        lightValue = Math.round(lightValue * MathHelper.cos(sunAngle));

        lightValue = MathHelper.clamp_int(lightValue, 0, 15);

        float lightRatio = lightValue / 15f;

        return lightValue / 15f;
    }

    private void updateTemperature() {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
        float baseTemp = biome.temperature;
        float celestialAngle = worldObj.getCelestialAngle(1.0f);
        float angleRad = celestialAngle * 2.0f * (float) Math.PI;
        float sunHeat = Math.max(0f, (float) Math.cos(angleRad));
        float temp = baseTemp * 35f + sunHeat * 10f;

        int heatOffset = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Block block = worldObj.getBlock(xCoord + dx, yCoord + dy, zCoord + dz);
                    heatOffset += getBlockHeatContribution(block);
                }
            }
        }

        temp += heatOffset;
        if (worldObj.isRaining()) {
            temp -= 5f;
        }
        temperatureCelsius = Math.round(temp * 100f) / 100f;
    }

    private int getBlockHeatContribution(Block block) {
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        if (fluid != null) {
            IFluidCoolant coolant = FluidFuelRegister.instance.getCoolant(fluid);
            if (coolant != null) {
                float coolingValue = coolant.getDegreesCoolingPerMB(0f);
                int coolingEffect = Math.round(coolingValue * 1000f);
                return -coolingEffect;
            }
        }

        // Các block cứng định nghĩa sẵn
        if (block == Blocks.lava || block == Blocks.flowing_lava) {
            return 8;
        } else if (block == Blocks.fire || block == Blocks.torch) {
            return 2;
        } else if (block == Blocks.ice || block == Blocks.packed_ice
            || block == Blocks.snow
            || block == Blocks.snow_layer) {
                return -5;
            } else if (block == Blocks.water || block == Blocks.flowing_water) {
                return -2;
            }
        return 0;
    }

    public float calculateTemperatureEfficiency() {
        float penalty = Math.max(0f, temperatureCelsius - 25f) * 0.004f;
        float efficiency = 1.0f - penalty;
        return Math.max(0.5f, efficiency);
    }

    public float calculateDustLevelEfficiency() {
        float penalty = dustLevel * 0.01f;
        float efficiency = 1.0f - penalty;
        return Math.max(0.5f, efficiency);
    }

    public float calculateWeatherEfficiency() {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(xCoord, zCoord);
        boolean isCold = biome.getFloatTemperature(xCoord, yCoord, zCoord) <= 0.15f;
        if (worldObj.isRaining() && isCold) {
            return 0f;
        }
        if (worldObj.isRaining()) {
            return 0.4f; // Giảm còn 30% hiệu suất
        }
        return 1.0f; // Thời tiết đẹp
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.syncValue(
            "temperature",
            new DoubleSyncValue(() -> (double) this.temperatureCelsius, val -> this.temperatureCelsius = (float) val));
        syncManager.syncValue("dustlevel", new IntSyncValue(() -> this.dustLevel, val -> this.dustLevel = val));
        return MGuis.mteTemplatePanelBuilder(this, data, syncManager, settings)
            .build()
            .child(
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
                                                    () -> "Generate: " + String.valueOf(getEnergyRegen()) + " RF/t")
                                                    .color(0xFFFFFFFF)
                                                    .asWidget()
                                                    .marginTop(2))
                                            .child(IKey.dynamic(() -> {
                                                String effPercent = String
                                                    .format("%.0f%%", calculateTemperatureEfficiency() * 100f);
                                                return "Temperature: " + temperatureCelsius + " ~ " + effPercent;
                                            })
                                                .color(0xFFFFFFFF)
                                                .asWidget())
                                            .child(IKey.dynamic(() -> {
                                                float dustEff = Math.max(0.5f, 1.0f - (dustLevel * 0.01f));
                                                String effPercent = String.format("%.0f%%", dustEff * 100f);
                                                return "Dust Level: " + dustLevel + " ~ " + effPercent;
                                            })
                                                .color(0xFFFFFFFF)
                                                .asWidget())
                                            .child(
                                                IKey.dynamic(
                                                    () -> {
                                                        return "Raining: " + worldObj.isRaining()
                                                            + (worldObj.isRaining() ? " -40%" : "");
                                                    })
                                                    .color(0xFFFFFFFF)
                                                    .asWidget())))));
    }
}
