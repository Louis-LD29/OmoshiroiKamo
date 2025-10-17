package ruiseki.omoshiroikamo.plugin.nei;

import java.util.ArrayList;

import com.cleanroommc.modularui.screen.GuiContainerWrapper;
import com.cleanroommc.modularui.screen.ModularContainer;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IStackPositioner;

public class GuiContainerWrapperStackPositioner implements IStackPositioner {

    // Hacky way around not having much params passed here
    public GuiContainerWrapper wrapper;
    public ModularContainer container;
    public INEIRecipeTransfer<?> recipeTransfer;

    @Override
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai) {
        return recipeTransfer.positionStacks(wrapper, container, ai);
    }
}
