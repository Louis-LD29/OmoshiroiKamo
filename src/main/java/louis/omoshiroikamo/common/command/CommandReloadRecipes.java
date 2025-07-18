package louis.omoshiroikamo.common.command;

import java.io.File;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import louis.omoshiroikamo.common.recipes.RecipeLoader;

public class CommandReloadRecipes extends CommandBase {

    @Override
    public String getCommandName() {
        return "reloadrecipes";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/reloadrecipes [filename]\n" + "  - Nếu không có filename → load tất cả.\n"
            + "  - Nếu có → chỉ load recipe theo tên file, không cần '.json'";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        int total = 0;

        if (args.length == 0) {
            // Load tất cả file .json trong folder
            File[] files = RecipeLoader.RECIPE_FOLDER.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File f : files) {
                    total += RecipeLoader.loadRecipesFromFile(f);
                }
                sender.addChatMessage(new ChatComponentText("Reloaded " + total + " recipes from all files."));
            } else {
                sender.addChatMessage(new ChatComponentText("No recipe files found."));
            }
        } else {
            String fileName = args[0];
            int count = RecipeLoader.loadRecipes(fileName);
            sender.addChatMessage(new ChatComponentText("Reloaded " + count + " recipes from: " + fileName));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
