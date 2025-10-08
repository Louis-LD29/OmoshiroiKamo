package louis.omoshiroikamo.plugin.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.plugin.nei.recipe.AnvilRecipeHandler;
import louis.omoshiroikamo.plugin.nei.recipe.ElectrolyzerRecipeHandler;
import louis.omoshiroikamo.plugin.nei.recipe.MaterialPropertiesHandler;

@SuppressWarnings("unused")
public class NEIConfig implements IConfigureNEI {

    public static final GuiContainerWrapperStackPositioner stackPositioner = new GuiContainerWrapperStackPositioner();
    public static final GuiContainerWrapperOverlayHandler overlayHandler = new GuiContainerWrapperOverlayHandler();

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new ElectrolyzerRecipeHandler());
        API.registerUsageHandler(new ElectrolyzerRecipeHandler());

        API.registerRecipeHandler(new AnvilRecipeHandler());
        API.registerUsageHandler(new AnvilRecipeHandler());

        API.registerRecipeHandler(new MaterialPropertiesHandler());
        API.registerUsageHandler(new MaterialPropertiesHandler());
        Logger.info("Loaded NeiConfig");
    }

    @Override
    public String getName() {
        return LibMisc.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return LibMisc.VERSION;
    }
}
