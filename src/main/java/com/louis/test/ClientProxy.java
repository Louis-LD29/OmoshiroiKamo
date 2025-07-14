package com.louis.test;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.louis.test.client.gui.ManaHUD;
import com.louis.test.client.handler.ClientTickHandler;
import com.louis.test.client.handler.DameEvents;
import com.louis.test.common.block.AbstractBlock;
import com.louis.test.common.block.ModBlocks;
import com.louis.test.common.block.meta.MTEISBRH;
import com.louis.test.common.block.meta.MTETESR;
import com.louis.test.common.block.meta.TEMeta;
import com.louis.test.common.item.ModItems;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        FMLCommonHandler.instance()
            .bus()
            .register(ClientTickHandler.instance);

        AbstractBlock.renderId = RenderingRegistry.getNextAvailableRenderId();

        MTEISBRH connectionRenderer = new MTEISBRH();
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockMeta), connectionRenderer);
        RenderingRegistry.registerBlockHandler(connectionRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TEMeta.class, new MTETESR());

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        MinecraftForge.EVENT_BUS.register(new DameEvents());

    }

}
