package louis.omoshiroikamo.api.item;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.enderio.core.common.util.DyeColor;

public interface IFocusableRegistry {

    List<WeightedStackBase> getFocusedList(DyeColor var1, float var2);

    List<WeightedStackBase> getUnFocusedList();

    boolean hasResource(ItemStack var1);

    boolean addResource(WeightedStackBase var1, DyeColor var2);

    DyeColor getPrioritizedLens(ItemStack var1);

    WeightedStackBase getWeightedStack(ItemStack var1);
}
