package louis.omoshiroikamo.plugin.nei;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibMods;

public class NEICompat {

    public static void init() {
        if (!LibMods.NotEnoughItems.isLoaded()) {
            return;
        }
        IMCSender();
    }

    public static void IMCSender() {
        sendHandler(ModObject.blockElectrolyzer.getRegistryName(), 85, 6);
        sendCatalyst(ModObject.blockElectrolyzer.getRegistryName());
        sendHandler(ModObject.blockAnvil.getRegistryName(), 64, 6);
        sendCatalyst(ModObject.blockAnvil.getRegistryName());
        sendCatalyst("smelting", ModObject.blockFurnace.getRegistryName());
        sendCatalyst("fuel", ModObject.blockFurnace.getRegistryName());
        sendHandler("materialProperties", ModObject.itemItemMaterial.getRegistryName(), 85, 1);
        Logger.info("Loaded IMCForNEI");
    }

    private static void sendHandler(String handler, String itemName, int height, int recipesPerPage) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("handler", handler);
        tag.setString("itemName", itemName);
        tag.setInteger("handlerHeight", height);
        tag.setInteger("maxRecipesPerPage", recipesPerPage);
        tag.setString("modName", LibMisc.MOD_NAME);
        tag.setString("modId", LibMisc.MOD_ID);
        tag.setBoolean("modRequired", true);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", tag);
    }

    private static void sendHandler(String handler, int height, int recipesPerPage) {
        sendHandler(handler, handler, height, recipesPerPage);
    }

    private static void sendCatalyst(String handlerID, String itemName, int priority) {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setString("handlerID", handlerID);
        aNBT.setString("itemName", itemName);
        aNBT.setInteger("priority", priority);
        FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", aNBT);
    }

    private static void sendCatalyst(String handlerName, String stack) {
        sendCatalyst(handlerName, stack, 0);
    }

    private static void sendCatalyst(String handler) {
        sendCatalyst(handler, handler, 0);
    }
}
