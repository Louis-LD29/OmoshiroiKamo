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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.energy.wire.MaterialWireType;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler;
import louis.omoshiroikamo.api.fluid.FluidRegistry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.api.ore.OreRegistry;
import louis.omoshiroikamo.client.ResourePackGen;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.command.ModCommands;
import louis.omoshiroikamo.common.fluid.ModFluids;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.recipes.ModRecipes;
import louis.omoshiroikamo.common.util.Logger;
import louis.omoshiroikamo.common.util.handler.ElementalHandler;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.world.OKWorldGenerator;
import louis.omoshiroikamo.common.world.WireNetSaveData;
import louis.omoshiroikamo.plugin.compat.EtFuturumCompat;
import louis.omoshiroikamo.plugin.compat.TICCompat;
import louis.omoshiroikamo.plugin.nei.NEICompat;
import louis.omoshiroikamo.plugin.structureLib.StructureCompat;
import louis.omoshiroikamo.plugin.waila.WailaCompat;
import makamys.mclib.core.MCLib;
import makamys.mclib.core.MCLibModules;

public class CommonProxy {

    protected long serverTickCount = 0;
    protected long clientTickCount = 0;
    protected final TickTimer tickTimer = new TickTimer();

    public CommonProxy() {}

    public void preInit(FMLPreInitializationEvent event) {
        MaterialRegistry.init();
        FluidRegistry.init();
        OreRegistry.init();
        MaterialWireType.init();

        ModBlocks.init();
        ModItems.init();
        ModFluids.init();
        OKWorldGenerator.init();

        callAssembleResourcePack(event);

        if (!LibMisc.SNAPSHOT_BUILD && !LibMisc.DEV_ENVIRONMENT) {
            MCLibModules.updateCheckAPI.submitModTask(LibMisc.MOD_ID, Tags.VERSION, LibMisc.VERSION_URL);
            Logger.info("Submitting update check for " + LibMisc.MOD_ID + " version " + LibMisc.VERSION);
        }
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(tickTimer);

        PacketHandler.init();

        ModRecipes.init();

        WailaCompat.init();
        NEICompat.init();
        EtFuturumCompat.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ElementalHandler());

        StructureCompat.init();
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
        if (WireNetHandler.INSTANCE == null) {
            WireNetHandler.INSTANCE = new WireNetHandler();
        }

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            World world = OmoshiroiKamo.proxy.getEntityWorld();
            if (!world.isRemote) {
                Logger.info("WorldData loading");
                WireNetSaveData worldData = (WireNetSaveData) world
                    .loadItemData(WireNetSaveData.class, WireNetSaveData.dataName);
                if (worldData == null) {
                    Logger.info("WorldData not found");
                    worldData = new WireNetSaveData(WireNetSaveData.dataName);
                    world.setItemData(WireNetSaveData.dataName, worldData);
                } else {
                    Logger.info("WorldData retrieved");
                }
                WireNetSaveData.setInstance(world.provider.dimensionId, worldData);
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

    public void callAssembleResourcePack(FMLPreInitializationEvent event) {
        ResourePackGen.assembleResourcePack(event);
    }

    protected void onServerTick() {
        ++serverTickCount;
    }

    protected void onClientTick() {}

    public long getTickCount() {
        return serverTickCount;
    }

    public final class TickTimer {

        @SubscribeEvent
        public void onTick(TickEvent.ServerTickEvent evt) {
            if (evt.phase == TickEvent.Phase.END) {
                onServerTick();
            }
        }

        @SubscribeEvent
        public void onTick(TickEvent.ClientTickEvent evt) {
            if (evt.phase == TickEvent.Phase.END) {
                onClientTick();
            }
        }
    }
}
