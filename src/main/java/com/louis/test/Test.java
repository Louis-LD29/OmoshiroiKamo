package com.louis.test;

import com.google.common.collect.ImmutableList;
import com.louis.test.api.IMC;
import com.louis.test.common.block.test.TestRecipeManager;
import com.louis.test.lib.LibMisc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

@Mod(
    modid = LibMisc.MOD_ID,
    name = LibMisc.MOD_NAME,
    version = LibMisc.VERSION,
    dependencies = LibMisc.DEPENDENCIES,
    guiFactory = LibMisc.GUI_FACTORY)
public class Test {

    @Instance(LibMisc.MOD_ID)
    public static Test instance;

    @SidedProxy(serverSide = LibMisc.PROXY_COMMON, clientSide = LibMisc.PROXY_CLIENT)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        processImc(FMLInterModComms.fetchRuntimeMessages(this));
    }

    private void processImc(ImmutableList<IMCMessage> messages) {
        for (IMCMessage msg : messages) {
            String key = msg.key;
            try {
                if (msg.isStringMessage()) {
                    String value = msg.getStringValue();
                    if (IMC.TEST_RECIPE.equals(key)) TestRecipeManager.getInstance()
                        .addCustomRecipes(value);
                }
            } catch (Exception e) {}
        }
    }
}
