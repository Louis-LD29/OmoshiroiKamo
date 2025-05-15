package com.louis.test;

import com.louis.test.core.handlers.ClientTickHandler;
import com.louis.test.core.handlers.FlightHandler;
import com.louis.test.gui.ManaHUD;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
        FMLCommonHandler.instance().bus().register(new FlightHandler());
        MinecraftForge.EVENT_BUS.register(new ManaHUD());
    }
}
