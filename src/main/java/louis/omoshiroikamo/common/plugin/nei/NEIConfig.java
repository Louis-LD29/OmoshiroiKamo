package louis.omoshiroikamo.common.plugin.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.plugin.nei.recipe.ElectrolyzerRecipeHandler;
import louis.omoshiroikamo.common.plugin.nei.recipe.MaterialPropertiesHandler;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new ElectrolyzerRecipeHandler());
        API.registerUsageHandler(new ElectrolyzerRecipeHandler());

        API.registerRecipeHandler(new MaterialPropertiesHandler());
        API.registerUsageHandler(new MaterialPropertiesHandler());
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
