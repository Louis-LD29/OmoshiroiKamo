package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api;

import net.minecraft.tileentity.TileEntity;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public class DimensionBlockPos {

    public int dim, posX, posY, posZ;

    public DimensionBlockPos(int d, int x, int y, int z) {
        dim = d;
        posX = x;
        posY = y;
        posZ = z;
    }

    public DimensionBlockPos(TileEntity te) {
        this(te.getWorldObj().provider.dimensionId, te.xCoord, te.yCoord, te.zCoord);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dim;
        result = prime * result + posX;
        result = prime * result + posY;
        result = prime * result + posZ;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DimensionBlockPos other = (DimensionBlockPos) obj;
        if (dim != other.dim) return false;
        if (posX != other.posX) return false;
        if (posY != other.posY) return false;
        if (posZ != other.posZ) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dimension: " + dim + " Pos: " + posX + ":" + posY + ":" + posZ;
    }
}
