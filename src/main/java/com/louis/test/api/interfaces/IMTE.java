package com.louis.test.api.interfaces;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.louis.test.api.interfaces.fluid.IFluidHandlerAdv;
import com.louis.test.api.interfaces.heat.IHeatHandler;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

public interface IMTE extends IFluidHandlerAdv, IEnergyHandler, IGuiHolder<PosGuiData>, IHeatHandler,
    IImmersiveConnectable, IEnergySink, IEnergySource {
}
