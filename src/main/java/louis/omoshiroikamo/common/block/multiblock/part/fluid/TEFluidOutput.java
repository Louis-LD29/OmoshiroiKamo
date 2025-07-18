package louis.omoshiroikamo.common.block.multiblock.part.fluid;

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

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.api.material.MaterialRegistry;

public class TEFluidOutput extends TEFluidInOut {

    protected SmartTank tank;
    private boolean tankDirty = false;

    protected TEFluidOutput(int meta) {
        this.meta = meta;
        material = MaterialRegistry.fromMeta(meta % 100);
        tank = new SmartTank(material);
    }

    public TEFluidOutput() {
        this(100);
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
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        FluidStack res = tank.drain(resource, doDrain);
        if (res != null && res.amount > 0 && doDrain) {
            tankDirty = true;
        }
        return res;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack res = tank.drain(maxDrain, doDrain);
        if (res != null && res.amount > 0 && doDrain) {
            tankDirty = true;
        }
        return res;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tank.canDrainFluidType(fluid);
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
                IKey.lang(getBlockType().getUnlocalizedName() + "." + material.getUnlocalizedName() + ".name")
                    .asWidget()
                    .margin(7))
            .child(
                new FluidSlot().syncHandler(new FluidSlotSyncHandler(tank))
                    .leftRel(0.5f)
                    .topRel(0.2f))
            .bindPlayerInventory();
    }
}
