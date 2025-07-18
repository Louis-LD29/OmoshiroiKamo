package louis.omoshiroikamo.api.mana;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IManaItem extends vazkii.botania.api.mana.IManaItem {

    public void addMana(ItemStack stack, int mana, EntityPlayer player);

    public float getConversionRate(ItemStack stack);
}
