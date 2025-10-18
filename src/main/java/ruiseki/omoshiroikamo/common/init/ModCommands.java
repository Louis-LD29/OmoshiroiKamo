package ruiseki.omoshiroikamo.common.init;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ruiseki.omoshiroikamo.common.command.CommandReloadRecipes;

public class ModCommands {

    public static void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadRecipes());
    }
}
