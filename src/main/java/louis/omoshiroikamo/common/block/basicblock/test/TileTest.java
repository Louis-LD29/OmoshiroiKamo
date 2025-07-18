package louis.omoshiroikamo.common.block.basicblock.test;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

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
import com.enderio.core.api.common.util.ITankAccess;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.FluidUtil;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.api.io.IoMode;
import louis.omoshiroikamo.api.io.IoType;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.basicblock.machine.AbstractPowerConsumerEntity;
import louis.omoshiroikamo.common.block.basicblock.machine.SlotDefinition;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.core.lib.LibResources;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IThrottledPacket;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ManaNetworkHandler;

public class TileTest extends AbstractPowerConsumerEntity
    implements IFluidHandler, ITankAccess, IManaPool, ISparkAttachable, IThrottledPacket {

    public static final int MAX_MANA = 1000;
    public static final int MAX_MANA_DILLUTED = 1000;
    private static final String TAG_MANA = "mana";
    private static final String TAG_KNOWN_MANA = "knownMana";
    private static final String TAG_MANA_CAP = "manaCap";
    private static final String TAG_OUTPUTTING = "outputting";
    private static int IO_MB_TICK = 100;
    public int manaCap = -1;
    public boolean isDoingTransfer = false;
    public int ticksDoingTransfer = 0;
    protected SmartTank inputTank = new SmartTank(10000);
    protected SmartTank outputTank = new SmartTank(10000);
    boolean outputting = false;
    int mana;
    int knownMana = -1;
    boolean sendPacket = false;
    int ticks = 0;
    private boolean tanksDirty = false;

    public TileTest(int meta) {
        super(new SlotDefinition(0, 1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
    }

    public TileTest() {
        this(0);
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockTest.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getMachineName() {
        return ModObject.blockTest.unlocalisedName;
    }

    @Override
    protected boolean doPush(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal(), IoType.FLUID)) {
            return false;
        }

        boolean res = super.doPush(dir);
        if (outputTank.getFluidAmount() > 0) {

            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {
                if (target.canFill(
                    dir.getOpposite(),
                    outputTank.getFluid()
                        .getFluid())) {
                    FluidStack push = outputTank.getFluid()
                        .copy();
                    push.amount = Math.min(push.amount, IO_MB_TICK);
                    int filled = target.fill(dir.getOpposite(), push, true);
                    if (filled > 0) {
                        outputTank.drain(filled, true);
                        tanksDirty = true;
                        return res;
                    }
                }
            }
        }
        return res;
    }

    @Override
    protected boolean doPull(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal(), IoType.FLUID)) {
            return false;
        }

        boolean res = super.doPull(dir);
        if (inputTank.getFluidAmount() < inputTank.getCapacity()) {
            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {

                if (inputTank.getFluidAmount() > 0) {
                    FluidStack canPull = inputTank.getFluid()
                        .copy();
                    canPull.amount = inputTank.getCapacity() - inputTank.getFluidAmount();
                    canPull.amount = Math.min(canPull.amount, IO_MB_TICK);
                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                    if (drained != null && drained.amount > 0) {
                        inputTank.fill(drained, true);
                        tanksDirty = true;
                        return res;
                    }
                } else {
                    // empty input tank
                    FluidTankInfo[] infos = target.getTankInfo(dir.getOpposite());
                    if (infos != null) {
                        for (FluidTankInfo info : infos) {
                            if (info.fluid != null && info.fluid.amount > 0) {
                                if (canFill(dir, info.fluid.getFluid())) {
                                    FluidStack canPull = info.fluid.copy();
                                    canPull.amount = Math.min(IO_MB_TICK, canPull.amount);
                                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                                    if (drained != null && drained.amount > 0) {
                                        inputTank.fill(drained, true);
                                        tanksDirty = true;
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
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (isSideDisabled(from.ordinal(), IoType.FLUID)) {
            return 0;
        }

        if (resource == null || !canFill(from, resource.getFluid())) {
            return 0;
        }
        int res = inputTank.fill(resource, doFill);
        if (res > 0 && doFill) {
            tanksDirty = true;
        }
        return res;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (isSideDisabled(from.ordinal(), IoType.FLUID)) {
            return null;
        }
        if (outputTank.getFluid() == null || resource == null || !resource.isFluidEqual(outputTank.getFluid())) {
            return null;
        }
        FluidStack res = outputTank.drain(resource.amount, doDrain);
        if (res != null && res.amount > 0 && doDrain) {
            tanksDirty = true;
        }
        return res;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (isSideDisabled(from.ordinal(), IoType.FLUID)) {
            return null;
        }
        FluidStack res = outputTank.drain(maxDrain, doDrain);
        if (res != null && res.amount > 0 && doDrain) {
            tanksDirty = true;
        }
        return res;
    }

    @Override
    protected void sendTaskProgressPacket() {
        ticksSinceLastProgressUpdate = 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return canFill(from) && fluid != null
            && (inputTank.getFluidAmount() > 0 && inputTank.getFluid()
                .getFluidID() == fluid.getID() || inputTank.getFluidAmount() == 0);
    }

    private boolean canFill(ForgeDirection from) {
        IoMode mode = getIoMode(from, IoType.FLUID);
        return mode != IoMode.OUTPUT && mode != IoMode.DISABLED;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        if (isSideDisabled(from.ordinal(), IoType.FLUID)) {
            return false;
        }
        return outputTank.getFluid() != null && outputTank.getFluid()
            .getFluid()
            .getID() == fluid.getID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (isSideDisabled(from.ordinal(), IoType.FLUID)) {
            return new FluidTankInfo[0];
        }
        return new FluidTankInfo[] { inputTank.getInfo(), outputTank.getInfo() };
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);

        nbtRoot.setInteger(TAG_MANA, mana);
        nbtRoot.setBoolean(TAG_OUTPUTTING, outputting);
        nbtRoot.setInteger(TAG_MANA_CAP, manaCap);

        inputTank.writeCommon("inputTank", nbtRoot);

        outputTank.writeCommon("outputTank", nbtRoot);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);

        mana = nbtRoot.getInteger(TAG_MANA);
        if (nbtRoot.hasKey(TAG_MANA_CAP)) manaCap = nbtRoot.getInteger(TAG_MANA_CAP);
        outputting = nbtRoot.getBoolean(TAG_OUTPUTTING);
        if (nbtRoot.hasKey(TAG_KNOWN_MANA)) knownMana = nbtRoot.getInteger(TAG_KNOWN_MANA);

        inputTank.readCommon("inputTank", nbtRoot);

        outputTank.readCommon("outputTank", nbtRoot);

    }

    @Override
    public int getPowerUsePerTick() {
        return Config.PowerUserPerTickRF;
    }

    @Override
    public FluidTank getInputTank(FluidStack forFluidType) {
        return inputTank;
    }

    @Override
    public FluidTank[] getOutputTanks() {
        return new FluidTank[] { outputTank /* , inputTank */ };
    }

    @Override
    public void setTanksDirty() {
        tanksDirty = true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        ModularPanel panel = super.buildUI(data, syncManager, settings);
        syncManager.registerSlotGroup("item_inv", 2);

        panel.child(
            new Column().child(
                new FluidSlot()
                    .syncHandler(new FluidSlotSyncHandler(outputTank).canDrainSlot(outputTank.getFluidAmount() >= 1000)

                    ))
                .child(
                    SlotGroupWidget.builder()
                        .matrix("IIF")
                        .key(
                            'I',
                            index -> {
                                return new ItemSlot().slot(new ModularSlot(this.inv, index).slotGroup("item_inv"))
                                    .debugName("Slot " + index);
                            })
                        .key('F', index -> {
                            return new FluidSlot().syncHandler(
                                new FluidSlotSyncHandler(inputTank).canDrainSlot(inputTank.getFluidAmount() >= 1000)

                        )
                                .debugName("Slot " + index);
                        })
                        .build()));
        return panel;
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
    public void doUpdate() {
        super.doUpdate();

        boolean wasDoingTransfer = isDoingTransfer;
        isDoingTransfer = false;
        if (manaCap == -1) manaCap = getBlockMetadata() == 2 ? MAX_MANA_DILLUTED : MAX_MANA;

        if (!ManaNetworkHandler.instance.isPoolIn(this) && !isInvalid()) ManaNetworkEvent.addPool(this);

        if (worldObj.isRemote) {
            double particleChance = 1F - (double) getCurrentMana() / (double) manaCap * 0.1;
            Color color = new Color(0x00C6FF);
            if (Math.random() > particleChance) Botania.proxy.wispFX(
                worldObj,
                xCoord + 0.3 + Math.random() * 0.5,
                yCoord + 0.6 + Math.random() * 0.25,
                zCoord + Math.random(),
                color.getRed() / 255F,
                color.getGreen() / 255F,
                color.getBlue() / 255F,
                (float) Math.random() / 3F,
                (float) -Math.random() / 25F,
                2F);
        }

        if (sendPacket && ticks % 10 == 0) {
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
            sendPacket = false;
        }

        if (isDoingTransfer) ticksDoingTransfer++;
        else {
            ticksDoingTransfer = 0;
            if (wasDoingTransfer) VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
        }

        ticks++;
    }

    public void renderHUD(Minecraft mc, ScaledResolution res) {
        String name = StatCollector.translateToLocal("tile." + LibResources.PREFIX_MOD + "blockTest.name");
        int color = 0x4444FF;
        HUDHandler.drawSimpleManaHUD(color, knownMana, manaCap, name, res);
    }

    public void onWanded(EntityPlayer player, ItemStack wand) {
        if (player == null) return;

        if (!worldObj.isRemote) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            writeCustomNBT(nbttagcompound);
            nbttagcompound.setInteger(TAG_KNOWN_MANA, getCurrentMana());
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).playerNetServerHandler
                    .sendPacket(new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound));
            }
        }

        worldObj.playSoundAtEntity(player, "botania:ding", 0.11F, 1F);
    }

    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    public void attachSpark(ISparkEntity entity) {

    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(
            ISparkEntity.class,
            AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        if (sparks.size() == 1) {
            Entity e = (Entity) sparks.get(0);
            return (ISparkEntity) e;
        }

        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }

    @Override
    public int getAvailableSpaceForMana() {
        return Math.max(0, manaCap - getCurrentMana());
    }

    @Override
    public boolean isFull() {
        return getCurrentMana() >= manaCap;
    }

    @Override
    public void recieveMana(int mana) {
        this.mana = Math.max(0, Math.min(getCurrentMana() + mana, manaCap));
        worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
        markDispatchable();
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        return worldObj != null && getBlockMetadata() == 1 ? MAX_MANA : mana;
    }

    @Override
    public void markDispatchable() {
        sendPacket = true;
    }

    @Override
    public boolean isOutputtingPower() {
        return outputting;
    }
}
