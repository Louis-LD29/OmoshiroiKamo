package louis.omoshiroikamo.plugin.wdmla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.plugin.universal.FluidStorageProvider;

import louis.omoshiroikamo.api.IWailaInfoProvider;
import louis.omoshiroikamo.api.fluid.IFluidHandlerAdv;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public enum FluidProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof AbstractTE te)) {
            return;
        }
        if (!(tileEntity instanceof IFluidHandlerAdv handler)) {
            return;
        }
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return;
        }
        if (!provider.hasFluidStorage()) {
            return;
        }

        SmartTank[] tanks = handler.getTanks();
        if (tanks == null || tanks.length == 0) {
            return;
        }

        if (!accessor.showDetails()) {
            return;
        }

        List<ClientViewGroup<FluidView>> clientGroups = new ArrayList<>();
        for (int i = 0; i < tanks.length; i++) {
            SmartTank tank = tanks[i];
            if (tank == null) {
                continue;
            }

            FluidStack fluidStack = tank.getFluid();
            int capacity = tank.getCapacity();

            FluidView view = FluidView.readDefault(new FluidView.Data(fluidStack, capacity));
            if (view == null) {
                continue;
            }

            view.hasScale = true;
            view.description = ThemeHelper.INSTANCE.info(
                String.format(
                    "%s (%d mB)",
                    fluidStack != null ? fluidStack.getLocalizedName() : "Empty",
                    fluidStack != null ? fluidStack.amount : 0));

            ClientViewGroup<FluidView> group = new ClientViewGroup<>(Collections.singletonList(view));
            group.title = "Tank " + (i + 1);
            group.messageType = MessageType.SUCCESS;
            clientGroups.add(group);
        }

        FluidStorageProvider.getBlock()
            .append(tooltip, accessor, clientGroups);

        if (te.getMaterial() != null) {
            int meltPointK = (int) te.getMaterial()
                .getMeltingPointK();
            tooltip.child(ThemeHelper.INSTANCE.info(String.format("Melting Point: %d K", meltPointK)));

        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), "te_fluid");
    }
}
