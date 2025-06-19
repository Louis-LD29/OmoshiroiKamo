package com.louis.test.common.command;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ModCommands {

    public static void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadRecipes());
    }
}
