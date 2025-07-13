package com.louis.test.api.heat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.enderio.core.common.util.BlockCoord;

public class HeatUtil {

    public static final List<IHeatReceptor> heatReceptors = new ArrayList<IHeatReceptor>();

    public static IHeatHandler getHeatHandler(IBlockAccess world, BlockCoord bc) {
        return getHeatHandler(world, bc.x, bc.y, bc.z);
    }

    public static IHeatHandler getHeatHandler(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return getHeatHandler(te);
    }

    public static IHeatHandler getHeatHandler(TileEntity te) {
        if (te instanceof IHeatHandler) {
            IHeatHandler res = (IHeatHandler) te;
            for (IHeatReceptor rec : heatReceptors) {
                if (!rec.isValidReceptor(res)) {
                    return null;
                }
            }
            return res;
        }
        return null;
    }
}
