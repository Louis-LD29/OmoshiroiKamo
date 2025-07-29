package louis.omoshiroikamo.common.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import louis.omoshiroikamo.common.item.ModItems;

public class CraftingRecipes {

    public void init() {

        ItemStack hammer = new ItemStack(ModItems.itemHammer, 1, 0);
        RecipeUtil.addShaped(hammer, "  c", "  s", "   ", 'c', Blocks.cobblestone, 's', Items.stick);

    }

}
