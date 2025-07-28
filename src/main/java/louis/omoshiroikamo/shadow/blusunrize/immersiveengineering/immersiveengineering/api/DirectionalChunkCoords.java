package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public class DirectionalChunkCoords extends ChunkCoordinates {

    public ForgeDirection direction;

    public DirectionalChunkCoords(ChunkCoordinates chunkCoordinates) {
        this(chunkCoordinates, ForgeDirection.UNKNOWN);
    }

    public DirectionalChunkCoords(ChunkCoordinates chunkCoordinates, ForgeDirection direction) {
        this(chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ, ForgeDirection.UNKNOWN);
    }

    public DirectionalChunkCoords(int x, int y, int z, ForgeDirection direction) {
        super(x, y, z);
        this.direction = direction;
    }

    public String toString() {
        return "DirectionalChunkCoords{x=" + this.posX
            + ", y="
            + this.posY
            + ", z="
            + this.posZ
            + ", direction="
            + this.direction.toString()
            + "}";
    }

    public TileEntity getTile(World world) {
        return world.getTileEntity(posX, posY, posZ);
    }
}
