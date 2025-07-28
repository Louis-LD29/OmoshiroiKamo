package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy;

import net.minecraft.util.Vec3;

import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.TargetingInfo;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public interface IImmersiveConnectable {

    boolean canConnect();

    boolean isEnergyOutput();

    int outputEnergy(int var1, boolean var2, int var3);

    boolean canConnectCable(WireType var1, TargetingInfo var2);

    void connectCable(WireType var1, TargetingInfo var2);

    WireType getCableLimiter(TargetingInfo var1);

    boolean allowEnergyToPass(ImmersiveNetHandler.Connection var1);

    void onEnergyPassthrough(int var1);

    void removeCable(ImmersiveNetHandler.Connection var1);

    Vec3 getRaytraceOffset(IImmersiveConnectable var1);

    Vec3 getConnectionOffset(ImmersiveNetHandler.Connection var1);
}
