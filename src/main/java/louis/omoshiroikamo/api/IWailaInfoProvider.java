package louis.omoshiroikamo.api;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;

public interface IWailaInfoProvider {

    default boolean hasFluidStorage() {
        return false;
    }

    default boolean hasEnergyStorage() {
        return false;
    }

    // WDMLA only
    default boolean hasItemStorage() {
        return false;
    }

    default boolean hasActiveStatus() {
        return false;
    }

    default boolean hasProcessStatus() {
        return false;
    }

    default void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {};

    default void appendServerData(NBTTagCompound data, BlockAccessor accessor) {};
}
