package louis.omoshiroikamo.common.recipes.chance;

import net.minecraftforge.fluids.FluidStack;

public class ChanceFluidStack {

    public final FluidStack stack;
    public final float chance;

    public ChanceFluidStack(FluidStack stack, float chance) {
        this.stack = stack;
        this.chance = chance;
    }
}
