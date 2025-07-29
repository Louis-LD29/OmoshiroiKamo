package louis.omoshiroikamo.common.block.multiblock.part.fluid;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.api.io.SlotDefinition;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.common.block.abstractClass.AbstractStorageTE;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import louis.omoshiroikamo.api.fluid.IFluidHandlerAdv;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;

public abstract class TEFluidInOut extends AbstractStorageTE implements IFluidHandlerAdv {

    protected SmartTank tank;

    public TEFluidInOut(MaterialEntry material) {
        super(new SlotDefinition(-1,-1), material);
        tank = new SmartTank(material);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
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
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

}
