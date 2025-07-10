package com.louis.test.common.block.multiblock.part.fluid;

import java.util.Locale;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.fluid.SmartTank;

public class TEFluidInput extends TEFluidInOut {

    protected SmartTank tank;
    private boolean tankDirty = false;

    protected TEFluidInput(int meta) {
        this.meta = meta;
        material = Material.fromMeta(meta % 100);
        tank = new SmartTank(material);
    }

    public TEFluidInput() {
        this(0);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInOut.unlocalisedName + "." + (meta >= 100 ? "output" : "input");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        boolean needsUpdate = tankDirty;
        tankDirty = false;
        return needsUpdate;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        tank.writeCommon("tank", root);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        tank.readCommon("tank", root);
    }

    @Override
    public SmartTank[] getTanks() {
        return new SmartTank[] { tank };
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (!canFill(from, resource.getFluid())) {
            return 0;
        }
        int res = tank.fill(resource, doFill);
        if (res > 0 && doFill) {
            tankDirty = true;
        }
        return res;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid != null && (tank.getFluidAmount() > 0 && tank.getFluid()
            .getFluidID() == fluid.getID() || tank.getFluidAmount() == 0);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        openGui(player);
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return ModularPanel.defaultPanel(getMachineName())
            .child(
                IKey.lang(
                    getBlockType().getUnlocalizedName() + "."
                        + material.name()
                            .toLowerCase(Locale.ROOT)
                        + ".name")
                    .asWidget()
                    .margin(7))
            .child(
                new FluidSlot().syncHandler(new FluidSlotSyncHandler(tank))
                    .leftRel(0.5f)
                    .topRel(0.2f))
            .bindPlayerInventory();
    }
}
