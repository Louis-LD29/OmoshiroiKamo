package com.louis.test.common.nei;

import com.louis.test.common.nei.recipe.ElectrolyzerRecipeHandler;
import com.louis.test.lib.LibMisc;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new ElectrolyzerRecipeHandler());
        API.registerUsageHandler(new ElectrolyzerRecipeHandler());
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
