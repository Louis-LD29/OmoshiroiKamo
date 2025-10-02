package louis.omoshiroikamo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Configuration;

import com.enderio.core.common.event.ConfigFileChangedEvent;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.fluid.FluidEntry;
import louis.omoshiroikamo.api.fluid.FluidRegistry;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.api.ore.OreEntry;
import louis.omoshiroikamo.api.ore.OreRegistry;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.util.helper.Logger;
import louis.omoshiroikamo.common.util.lang.LangSectionInserter;
import louis.omoshiroikamo.common.util.lib.LibMisc;

public class Config {

    public static final List<Section> sections;

    static {
        sections = new ArrayList<Section>();
    }

    public static File configDirectory;
    public static Configuration config;

    public static final Section sectionMaterial = new Section("Material Settings", "material");
    public static final Section sectionFluid = new Section("Fluid Settings", "fluid");
    public static final Section sectionOre = new Section("Ore Settings", "ore");

    public static String[] materialCustom = new String[] {};
    public static String[] oreCustom = new String[] {};

    public static final Map<String, MaterialConfig> materialConfigs = new HashMap<>();
    public static final Map<String, FluidConfig> fluidConigs = new HashMap<>();
    public static final Map<String, OreConfig> oreConfigs = new HashMap<>();

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

        // Material
        materialCustom = config.get(
            sectionMaterial.name,
            "materialCustom",
            materialCustom,
            "List of custom material names to register on next game load. "
                + "Each material will be initialized with predefined or config-based properties. Requires game restart.")
            .getStringList();

        for (String name : materialCustom) {
            if (!MaterialRegistry.contains(name)) {
                MaterialEntry entry = new MaterialEntry(name);
                MaterialRegistry.register(entry);
            }
        }

        Set<String> materialDefined = new HashSet<>();
        for (MaterialEntry entry : MaterialRegistry.all()) {
            materialDefined.add(
                entry.getName()
                    .toLowerCase());
        }

        Set<String> categoriesMaterialToRemove = new HashSet<>();
        for (String category : config.getCategoryNames()) {
            if (category.startsWith("material settings") && !category.equals("material settings")) {
                String name = category.substring("material settings".length() + 1);
                if (!materialDefined.contains(name)) {
                    categoriesMaterialToRemove.add(category);
                }
            }
        }

        for (String cat : categoriesMaterialToRemove) {
            Logger.info(cat);
            config.removeCategory(config.getCategory(cat));
        }

        LangSectionInserter.insertCustomMaterialsLang(materialCustom);

        materialConfigs.clear();
        for (MaterialEntry entry : MaterialRegistry.all()) {
            materialConfigs.put(
                entry.getName()
                    .toLowerCase(),
                MaterialConfig.loadFromConfig(config, entry));
        }
        // Fluid
        fluidConigs.clear();
        for (FluidEntry entry : FluidRegistry.all()) {
            fluidConigs.put(
                entry.getName()
                    .toLowerCase(),
                FluidConfig.loadFromConfig(config, entry));
        }

        // Ore
        // oreCustom = config.get(
        // sectionOre.name,
        // "oreCustom",
        // oreCustom,
        // "List of custom ore names to register on next game load. "
        // + "Each material will be initialized with predefined or config-based properties. Requires game restart.")
        // .getStringList();
        //
        // for (String name : oreCustom) {
        // if (!OreRegistry.contains(name)) {
        // OreEntry entry = new OreEntry(name);
        // OreRegistry.register(entry);
        // }
        // }
        //
        // Set<String> oreDefined = new HashSet<>();
        // for (OreEntry entry : OreRegistry.all()) {
        // oreDefined.add(entry.getName());
        // }
        //
        // Set<String> categoriesOreToRemove = new HashSet<>();
        // for (String category : config.getCategoryNames()) {
        // if (category.startsWith("ore settings") && !category.equals("ore settings")) {
        // String name = category.substring("ore settings".length() + 1);
        // if (!oreDefined.contains(name.toLowerCase())) {
        // categoriesOreToRemove.add(category);
        // }
        // }
        // }
        //
        // for (String cat : categoriesOreToRemove) {
        // config.removeCategory(config.getCategory(cat));
        // }
        // LangSectionInserter.insertCustomMaterialsLang(oreCustom);
        oreConfigs.clear();
        for (OreEntry entry : OreRegistry.all()) {
            oreConfigs.put(
                entry.getName()
                    .toLowerCase(),
                OreConfig.loadFromConfig(config, entry));
        }

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
