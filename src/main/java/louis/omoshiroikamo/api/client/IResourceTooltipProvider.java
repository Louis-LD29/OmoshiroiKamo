package louis.omoshiroikamo.api.client;

import net.minecraft.item.ItemStack;

public interface IResourceTooltipProvider {

    String getUnlocalizedNameForTooltip(ItemStack itemStack);
}
