package louis.omoshiroikamo.api.fluid;

import net.minecraftforge.fluids.IFluidHandler;

public interface IFluidHandlerAdv extends IFluidHandler {

    SmartTank[] getTanks();
}
