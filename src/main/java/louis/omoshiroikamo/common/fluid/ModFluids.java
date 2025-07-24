package louis.omoshiroikamo.common.fluid;

import net.minecraftforge.fluids.IFluidTank;

import louis.omoshiroikamo.common.core.lib.LibMisc;

public class ModFluids {

    public static String toCapactityString(IFluidTank tank) {
        if (tank == null) {
            return "0/0 " + MB();
        }
        return tank.getFluidAmount() + "/" + tank.getCapacity() + " " + MB();
    }

    public static String MB() {
        return LibMisc.lang.localize("fluid.millibucket.abr");
    }

    public static void init() {

        FluidMaterialRegister.init();
        FluidRegister.init();
    }

}
