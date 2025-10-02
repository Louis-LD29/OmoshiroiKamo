package louis.omoshiroikamo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Configuration;

import com.enderio.core.common.event.ConfigFileChangedEvent;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.util.helper.Logger;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class Config {

    public static final List<Section> sections;

    static {
        sections = new ArrayList<Section>();
    }

    public static File configDirectory;
    public static Configuration config;

    public static void syncConfig(boolean load) {
        try {
            if (load) {
                config.load();
            }
            Config.processConfig(config);
        } catch (Exception e) {
            Logger.error("Mod has a problem loading it's configuration");
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void processConfig(Configuration config) {

    }

    public static class Section {

        public final String name;
        public final String lang;

        public Section(String name, String lang) {
            this.name = name;
            this.lang = lang;
            register();
        }

        private void register() {
            sections.add(this);
        }

        public String lc() {
            return name.toLowerCase(Locale.US);
        }
    }

    public static void preInit(FMLPreInitializationEvent event) {
        PacketHandler.INSTANCE
            .registerMessage(PacketConfigSync.class, PacketConfigSync.class, PacketHandler.nextID(), Side.CLIENT);

        FMLCommonHandler.instance()
            .bus()
            .register(new Config());
        configDirectory = new File(event.getModConfigurationDirectory(), LibMisc.MOD_ID);
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }
        File configFile = new File(configDirectory, LibMisc.MOD_ID + ".cfg");
        config = new Configuration(configFile);
        syncConfig(false);

        Logger.info("Loaded Config");
    }

    public static void init() {}

    public static void postInit() {}

    private Config() {}

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.modID.equals(LibMisc.MOD_ID)) {
            Logger.info("Updating config...");
            syncConfig(false);
            init();
            postInit();
        }
    }

    @SubscribeEvent
    public void onConfigFileChanged(ConfigFileChangedEvent event) {
        if (event.modID.equals(LibMisc.MOD_ID)) {
            Logger.info("Updating config...");
            syncConfig(true);
            event.setSuccessful();
            init();
            postInit();
        }
    }

    @SubscribeEvent
    public void onPlayerLoggon(PlayerLoggedInEvent evt) {
        PacketHandler.INSTANCE.sendTo(new PacketConfigSync(), (EntityPlayerMP) evt.player);
    }
}
