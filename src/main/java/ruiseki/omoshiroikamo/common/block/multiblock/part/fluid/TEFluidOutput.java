package ruiseki.omoshiroikamo.common.block.multiblock.part.fluid;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.fluid.SmartTank;
import ruiseki.omoshiroikamo.api.material.MaterialRegistry;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class TEFluidOutput extends TEFluidInOut {

    protected TEFluidOutput(int meta) {
        super(MaterialRegistry.fromMeta(meta % LibResources.META1));
        this.meta = meta;
    }

    public TEFluidOutput() {
        this(100);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInOut.unlocalisedName + "." + (meta >= LibResources.META1 ? "output" : "input");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
        return false;
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
            tanksDirty = true;
        }
        return res;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack res = tank.drain(maxDrain, doDrain);
        if (res != null && res.amount > 0 && doDrain) {
            tanksDirty = true;
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
