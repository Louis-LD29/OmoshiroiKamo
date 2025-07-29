package louis.omoshiroikamo.api.energy;

import net.minecraft.item.ItemStack;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is IWireCoil
 */
public interface IWireCoil {

    public abstract WireType getWireType(ItemStack stack);
}
