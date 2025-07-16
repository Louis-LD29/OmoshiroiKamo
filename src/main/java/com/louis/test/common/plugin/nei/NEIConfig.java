package com.louis.test.common.plugin.nei;

import com.louis.test.common.core.lib.LibMisc;
import com.louis.test.common.plugin.nei.recipe.ElectrolyzerRecipeHandler;
import com.louis.test.common.plugin.nei.recipe.MaterialPropertiesHandler;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

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
