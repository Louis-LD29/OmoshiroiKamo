package louis.omoshiroikamo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.energy.MaterialWireType;
import louis.omoshiroikamo.api.fluid.FluidRegistry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.api.ore.OreRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.command.ModCommands;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.core.handlers.ConvertManaRegenHandler;
import louis.omoshiroikamo.common.core.handlers.ElementalHandler;
import louis.omoshiroikamo.common.core.handlers.FlightHandler;
import louis.omoshiroikamo.common.core.handlers.ManaRegenHandler;
import louis.omoshiroikamo.common.core.helper.Logger;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.fluid.ModFluids;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.plugin.nei.NEICompat;
import louis.omoshiroikamo.common.plugin.tic.TICCompat;
import louis.omoshiroikamo.common.plugin.waila.WailaCompat;
import louis.omoshiroikamo.common.recipes.ModRecipes;
import louis.omoshiroikamo.common.world.OKWorldGenerator;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.ImmersiveNetHandler;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.EventHandler;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.IESaveData;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.util.IELogger;
import makamys.mclib.core.MCLib;
import makamys.mclib.core.MCLibModules;

public class CommonProxy {

    public CommonProxy() {}

    public void preInit(FMLPreInitializationEvent event) {
        MaterialRegistry.init();
        FluidRegistry.init();
        OreRegistry.init();
        MaterialWireType.init();

        Config.preInit(event);

        ModBlocks.init();
        ModItems.init();
        ModFluids.init();
        OKWorldGenerator.init();

        callAssembleResourcePack();

        if (!LibMisc.SNAPSHOT_BUILD && !LibMisc.DEV_ENVIRONMENT) {
            MCLibModules.updateCheckAPI.submitModTask(LibMisc.MOD_ID, Tags.VERSION, LibMisc.VERSION_URL);
            Logger.info("Submitting update check for " + LibMisc.MOD_ID + " version " + LibMisc.VERSION);
        }
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

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandler());
        ModRecipes.init();

        WailaCompat.init();
        NEICompat.init();

    }

    public void postInit(FMLPostInitializationEvent event) {
        Config.postInit();
        MinecraftForge.EVENT_BUS.register(new ElementalHandler());

        TICCompat.init();

    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public World getClientWorld() {
        return null;
    }

    public World getEntityWorld() {
        return MinecraftServer.getServer()
            .getEntityWorld();
    }

    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.init(event);
    }

    public void serverStarted(FMLServerStartedEvent event) {
        if (ImmersiveNetHandler.INSTANCE == null) ImmersiveNetHandler.INSTANCE = new ImmersiveNetHandler();

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            World world = OmoshiroiKamo.proxy.getEntityWorld();
            if (!world.isRemote) {
                IELogger.info("WorldData loading");
                IESaveData worldData = (IESaveData) world.loadItemData(IESaveData.class, IESaveData.dataName);
                if (worldData == null) {
                    IELogger.info("WorldData not found");
                    worldData = new IESaveData(IESaveData.dataName);
                    world.setItemData(IESaveData.dataName, worldData);
                } else IELogger.info("WorldData retrieved");
                IESaveData.setInstance(world.provider.dimensionId, worldData);
            }
        }
    }

    public void onConstruction(FMLConstructionEvent event) {
        if (LibMisc.SNAPSHOT_BUILD && !LibMisc.DEV_ENVIRONMENT) {
            Logger.info(
                LibMisc.MOD_ID
                    + " is in snapshot mode. Disabling update checker... Other features may also be different.");
        }

        MCLib.init();
    }

    public void callAssembleResourcePack() {
        Config.assembleResourcePack();
    }
}
