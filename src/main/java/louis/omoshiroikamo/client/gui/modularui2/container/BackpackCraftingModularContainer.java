package louis.omoshiroikamo.client.gui.modularui2.container;

import com.cleanroommc.modularui.screen.GuiContainerWrapper;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.test.CraftingModularContainer;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;

import codechicken.nei.recipe.IRecipeHandler;
import louis.omoshiroikamo.plugin.nei.INEIRecipeTransfer;

public class BackpackCraftingModularContainer extends CraftingModularContainer
    implements INEIRecipeTransfer<BackpackCraftingModularContainer> {

    public BackpackCraftingModularContainer(int width, int height, IItemHandlerModifiable craftingInventory) {
        super(width, height, craftingInventory);
    }

    @Override
    public String[] getIdents() {
        return new String[] { "crafting" };
    }

    @Override
    public int transferRecipe(GuiContainerWrapper gui, ModularContainer self, IRecipeHandler recipe, int recipeIndex,
        int multiplier) {
        return 0;
    }
}
