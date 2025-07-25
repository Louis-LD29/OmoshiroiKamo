package louis.omoshiroikamo.common.plugin.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.core.lib.LibMods;
import louis.omoshiroikamo.common.plugin.nei.recipe.AnvilRecipeHandler;
import louis.omoshiroikamo.common.plugin.nei.recipe.ElectrolyzerRecipeHandler;
import louis.omoshiroikamo.common.plugin.nei.recipe.MaterialPropertiesHandler;

public class NEICompat implements IConfigureNEI {

    public static void init() {
        if (!LibMods.nei) return;
        IMCForNEI.IMCSender();
    }

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new ElectrolyzerRecipeHandler());
        API.registerUsageHandler(new ElectrolyzerRecipeHandler());

        API.registerRecipeHandler(new AnvilRecipeHandler());
        API.registerUsageHandler(new AnvilRecipeHandler());

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
