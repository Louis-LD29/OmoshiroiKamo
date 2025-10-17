package ruiseki.omoshiroikamo.api.item;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.api.client.IRenderUpgrade;

public interface IAnvilUpgrade extends IAdvancedTooltipProvider {

    String getUnlocalizedName();

    int getLevelCost();

    boolean isUpgradeItem(ItemStack stack);

    boolean canAddToItem(ItemStack stack);

    boolean hasUpgrade(ItemStack stack);

    void writeToItem(ItemStack stack);

    void removeFromItem(ItemStack stack);

    ItemStack getUpgradeItem();

    String getUpgradeItemName();

    @Nullable
    @SideOnly(Side.CLIENT)
    IRenderUpgrade getRender();
}
