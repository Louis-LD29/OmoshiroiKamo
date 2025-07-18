package louis.omoshiroikamo;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import louis.omoshiroikamo.client.fluid.FluidTextureGenerator;
import louis.omoshiroikamo.client.gui.ManaHUD;
import louis.omoshiroikamo.client.handler.ClientTickHandler;
import louis.omoshiroikamo.client.handler.DameEvents;
import louis.omoshiroikamo.client.render.block.connectable.ConnectableISBRH;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorEVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorHVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorIVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorLVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorMVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorULVTESR;
import louis.omoshiroikamo.client.render.block.connectable.InsulatorTESR;
import louis.omoshiroikamo.client.render.block.connectable.TransformerTESR;
import louis.omoshiroikamo.client.render.item.pufferfish.PufferFishRenderer;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorEV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorHV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorIV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorLV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorMV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorULV;
import louis.omoshiroikamo.common.block.energyConnector.TEInsulator;
import louis.omoshiroikamo.common.block.energyConnector.TETransformer;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.item.ModItems;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {}

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

        ConnectableISBRH connectableISBRH = new ConnectableISBRH();
        RenderingRegistry.registerBlockHandler(connectableISBRH);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockConnectable), connectableISBRH);
        ClientRegistry.bindTileEntitySpecialRenderer(TEInsulator.class, new InsulatorTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorULV.class, new ConnectorULVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorLV.class, new ConnectorLVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorMV.class, new ConnectorMVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorHV.class, new ConnectorHVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorEV.class, new ConnectorEVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TEConnectorIV.class, new ConnectorIVTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TETransformer.class, new TransformerTESR());

        if (Config.renderPufferFish) {
            MinecraftForgeClient.registerItemRenderer(Items.fish, new PufferFishRenderer());
        }

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        MinecraftForge.EVENT_BUS.register(new DameEvents());

    }

    @Override
    public void callAssembleResourcePack() {
        FluidTextureGenerator.applyAll();
        super.callAssembleResourcePack();
    }

}
