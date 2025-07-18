package louis.omoshiroikamo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import louis.omoshiroikamo.api.energy.MaterialWireType;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.command.ModCommands;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.core.handlers.ConvertManaRegenHandler;
import louis.omoshiroikamo.common.core.handlers.ElementalHandler;
import louis.omoshiroikamo.common.core.handlers.FlightHandler;
import louis.omoshiroikamo.common.core.handlers.ManaRegenHandler;
import louis.omoshiroikamo.common.fluid.ModFluids;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.plugin.compat.IECompat;
import louis.omoshiroikamo.common.plugin.nei.IMCForNEI;
import louis.omoshiroikamo.common.plugin.tic.TICCompat;
import louis.omoshiroikamo.common.plugin.waila.WailaRegistrar;
import louis.omoshiroikamo.common.recipes.ModRecipes;

public class CommonProxy {

    public CommonProxy() {}

    public void preInit(FMLPreInitializationEvent event) {
        MaterialRegistry.init();
        MaterialWireType.init();

        Config.preInit(event);

        ModBlocks.init();
        ModItems.init();
        ModFluids.init();
        ModRecipes.init();

        callAssembleResourcePack();
        IECompat.preInit();
    }

    public void init(FMLInitializationEvent event) {
        Config.init();
        FMLCommonHandler.instance()
            .bus()
            .register(ManaRegenHandler.instance);
        FMLCommonHandler.instance()
            .bus()
            .register(ConvertManaRegenHandler.instance);
        FMLCommonHandler.instance()
            .bus()
            .register(FlightHandler.instance);

        IECompat.init();

        if (Loader.isModLoaded("Waila")) {
            WailaRegistrar.init();
        }

        if (Loader.isModLoaded("NotEnoughItems")) {
            IMCForNEI.IMCSender();
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        Config.postInit();
        MinecraftForge.EVENT_BUS.register(new ElementalHandler());

        if (Loader.isModLoaded("TConstruct")) {
            TICCompat.registerTinkersConstructIntegration(event);
        }
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.init(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        IECompat.serverLoad();
    }

    public void callAssembleResourcePack() {
        Config.assembleResourcePack();
    }
}
