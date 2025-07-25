package louis.omoshiroikamo.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import louis.omoshiroikamo.common.recipes.ModRecipes;

public class CommandReloadRecipes extends CommandBase {

    @Override
    public String getCommandName() {
        return "reloadrecipes";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/reloadrecipes";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ModRecipes.reloadRecipes();
        sender.addChatMessage(new ChatComponentText("Recipes reloaded from JSON files."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
