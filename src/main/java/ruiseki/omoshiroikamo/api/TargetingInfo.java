package ruiseki.omoshiroikamo.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author BluSunrize - 11.03.2015
 *         <p>
 *         Similar too MovingObjectPosition.class, but this is specifically designed for sub-targets on a block
 */
/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is TargetingInfo
 */
public class TargetingInfo {

    public final int side;
    public final float hitX;
    public final float hitY;
    public final float hitZ;

    public TargetingInfo(int side, float hitX, float hitY, float hitZ) {
        this.side = side;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setInteger("side", side);
        tag.setFloat("hitX", hitX);
        tag.setFloat("hitY", hitY);
        tag.setFloat("hitZ", hitZ);
    }

    public static TargetingInfo readFromNBT(NBTTagCompound tag) {
        return new TargetingInfo(
            tag.getInteger("side"),
            tag.getFloat("hitX"),
            tag.getFloat("hitY"),
            tag.getFloat("hitZ"));
    }
}
