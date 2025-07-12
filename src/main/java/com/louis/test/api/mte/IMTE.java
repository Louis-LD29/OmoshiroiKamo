package com.louis.test.api.mte;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import cofh.api.energy.IEnergyHandler;
import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.louis.test.api.fluid.IFluidHandlerAdv;
import com.louis.test.api.heat.IHeatHandler;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

public interface IMTE extends IFluidHandlerAdv, IEnergyHandler, IGuiHolder<PosGuiData>, IHeatHandler,
    IImmersiveConnectable, IEnergySink, IEnergySource {
}
