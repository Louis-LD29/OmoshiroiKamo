package louis.omoshiroikamo.api.mana;

import net.minecraft.item.ItemStack;

public interface IManaItem extends vazkii.botania.api.mana.IManaItem {

    public float getConversionRate(ItemStack stack);
}
