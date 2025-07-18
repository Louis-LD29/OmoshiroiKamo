package louis.omoshiroikamo.api.redstone;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneConnectable {

    boolean shouldRedstoneConduitConnect(World world, int x, int y, int z, ForgeDirection from);
}
