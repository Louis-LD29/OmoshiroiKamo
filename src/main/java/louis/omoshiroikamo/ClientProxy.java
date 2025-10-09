package louis.omoshiroikamo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.client.ResourePackGen;
import louis.omoshiroikamo.client.handler.DameEvents;
import louis.omoshiroikamo.client.handler.KeyHandler;
import louis.omoshiroikamo.client.render.block.anvil.AnvilISBRH;
import louis.omoshiroikamo.client.render.block.anvil.AnvilTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectableISBRH;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorEVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorHVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorIVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorLVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorMVTESR;
import louis.omoshiroikamo.client.render.block.connectable.ConnectorULVTESR;
import louis.omoshiroikamo.client.render.block.connectable.InsulatorTESR;
import louis.omoshiroikamo.client.render.block.connectable.TransformerTESR;
import louis.omoshiroikamo.client.render.item.backpack.BackpackRenderer;
import louis.omoshiroikamo.client.render.item.hammer.HammerRenderer;
import louis.omoshiroikamo.client.render.item.pufferfish.PufferFishRenderer;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.block.anvil.TEAnvil;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorEV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorHV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorIV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorLV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorMV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorULV;
import louis.omoshiroikamo.common.block.energyConnector.TEInsulator;
import louis.omoshiroikamo.common.block.energyConnector.TETransformer;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.config.item.ItemConfig;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy() {}

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        FMLCommonHandler.instance()
            .bus()
            .register(KeyHandler.instance);

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

        AnvilISBRH anvilISBRH = new AnvilISBRH();
        RenderingRegistry.registerBlockHandler(anvilISBRH);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.blockAnvil), anvilISBRH);
        ClientRegistry.bindTileEntitySpecialRenderer(TEAnvil.class, new AnvilTESR());

        ModItems.registerItemRenderer();

        if (ItemConfig.renderPufferFish) {
            MinecraftForgeClient.registerItemRenderer(Items.fish, new PufferFishRenderer());
        }

        MinecraftForgeClient.registerItemRenderer(ModItems.itemHammer, new HammerRenderer());
        MinecraftForgeClient.registerItemRenderer(ModItems.itemBackPack, new BackpackRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        MinecraftForge.EVENT_BUS.register(new DameEvents());
    }

    @Override
    public void callAssembleResourcePack(FMLPreInitializationEvent event) {
        ResourePackGen.applyAllTexture(event);
        super.callAssembleResourcePack(event);
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance()
            .getClient().theWorld;
    }

    @Override
    public long getTickCount() {
        return clientTickCount;
    }

    @Override
    protected void onClientTick() {
        if (!Minecraft.getMinecraft()
            .isGamePaused() && Minecraft.getMinecraft().theWorld != null) {
            ++clientTickCount;
        }
    }
}
