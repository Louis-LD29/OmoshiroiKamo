package com.louis.test.common.plugin.wdmla;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.ui.MessageType;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.FluidView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.louis.test.api.fluid.IFluidHandlerAdv;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.common.core.lib.LibMisc;

public enum FluidProvider
    implements IServerExtensionProvider<FluidView.Data>, IClientExtensionProvider<FluidView.Data, FluidView> {

    INSTANCE;

    @Override
    public List<ClientViewGroup<FluidView>> getClientGroups(Accessor accessor, List<ViewGroup<FluidView.Data>> groups) {
        return ClientViewGroup.map(groups, FluidView::readDefault, (group, clientGroup) -> {
            // Nếu có ID thì dùng làm title
            if (group.id != null) {
                clientGroup.title = group.id;
            }

            for (FluidView view : clientGroup.views) {
                view.hasScale = true;
            }

            clientGroup.messageType = MessageType.SUCCESS;
        });
    }

    @Override
    public List<ViewGroup<FluidView.Data>> getGroups(Accessor accessor) {
        Object target = accessor.getTarget();
        if (!(target instanceof IFluidHandlerAdv handler)) return null;

        SmartTank[] tanks = handler.getTanks();
        if (tanks == null || tanks.length == 0) return null;

        List<ViewGroup<FluidView.Data>> result = new ArrayList<>();
        for (int i = 0; i < tanks.length; i++) {
            SmartTank tank = tanks[i];
            if (tank == null) continue;

            FluidStack fluidStack = tank.getFluid();
            int capacity = tank.getCapacity();

            ViewGroup<FluidView.Data> group = new ViewGroup<>(Arrays.asList(new FluidView.Data(fluidStack, capacity)));
            group.id = "Tank " + (i + 1);
            result.add(group);
        }

        return result;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), "te_fluid");
    }
}
