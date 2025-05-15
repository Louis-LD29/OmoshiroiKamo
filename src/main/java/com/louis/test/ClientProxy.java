package com.louis.test;

import com.louis.test.common.item.ModItems;
import com.louis.test.core.handlers.ClientTickHandler;
import com.louis.test.gui.ManaHUD;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModItems.registerItemRenderer();
        MinecraftForge.EVENT_BUS.register(ManaHUD.instance);
        FMLCommonHandler.instance().bus().register(ClientTickHandler.instance);

    }

    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
