package ruiseki.omoshiroikamo.common.command;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ModCommands {

    public static void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadRecipes());
    }
}
