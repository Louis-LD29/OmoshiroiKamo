package louis.omoshiroikamo.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Configuration;

import com.enderio.core.common.event.ConfigFileChangedEvent;
import com.enderio.core.common.util.ResourcePackAssembler;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.api.energy.WireType;
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
import louis.omoshiroikamo.common.util.lib.LibResources;

public class Config {

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

    public static final List<Section> sections;

    static {
        sections = new ArrayList<Section>();
    }

    public static Configuration config;
    public static final Section sectionMana = new Section("Mana", "mana");
    public static final Section sectionPersonal = new Section("Personal Settings", "personal");
    public static final Section sectionIE = new Section("Immersive Engineering Settings", "ie");
    public static final Section sectionMaterial = new Section("Material Settings", "material");
    public static final Section sectionFluid = new Section("Fluid Settings", "fluid");
    public static final Section sectionOre = new Section("Ore Settings", "ore");
    public static final Section sectionDamageIndicators = new Section("Damage Indicators Settings", "damage_indicator");

    public static File configDirectory;
    public static int manaPowerStorageBase = 50000;
    public static int manaUpgradeDiamondCost = 10;
    public static int PowerUserPerTickRF = 20;
    public static boolean addDurabilityTootip = true;
    public static boolean addFuelTooltipsToAllFluidContainers = true;
    public static boolean renderDurabilityBar = true;
    public static boolean renderChargeBar = true;
    public static boolean renderPufferFish = true;
    public static boolean increasedRenderboxes = true;
    public static boolean validateConnections = true;
    public static int[] cableLength = new int[] { 16, 16, 32, 32, 32 };
    public static double[] cableLossRatio = new double[] { 0.05, 0.025, 0.025, 1.0, 1.0 };
    public static int[] cableTransferRate = new int[] { 2048, 8192, 32768, 0, 0 };
    public static int[] cableColouration = new int[] { 13926474, 15576418, 7303023, 9862765, 7303023 };
    public static String[] materialCustom = new String[] {};
    public static String[] oreCustom = new String[] {};
    public static boolean showDamageParticles = true;
    public static boolean useWDMLA = true;
    public static int damageColor = 0xFFFFFF;
    public static int healColor = 0x33FF33;

    public static final Map<String, MaterialConfig> materialConfigs = new HashMap<>();
    public static final Map<String, FluidConfig> fluidConigs = new HashMap<>();
    public static final Map<String, OreConfig> oreConfigs = new HashMap<>();

    private static ResourcePackAssembler assembler;

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

    public static void assembleResourcePack() {
        assembler = new ResourcePackAssembler(
            new File(configDirectory, LibMisc.MOD_NAME + " Resourcepack"),
            LibMisc.MOD_NAME + " Resourcepack",
            LibMisc.MOD_ID);

        addIcons(assembler);
        addLangs(assembler);

        assembler.assemble()
            .inject();
    }

    private static void addIcons(ResourcePackAssembler assembler) {
        File iconDir = new File(configDirectory, "icons");
        File materialFluidDir = new File(configDirectory, LibResources.PREFIX_MATERIAL_FLUID_ICONS);
        File fluidDir = new File(configDirectory, LibResources.PREFIX_FLUID_ICONS);
        File oreDir = new File(configDirectory, LibResources.PREFIX_ORE_ICONS);
        if (!iconDir.exists()) iconDir.mkdirs();

        File[] iconFiles = iconDir.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] materialFluidFiles = materialFluidDir
            .listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] fluidFiles = fluidDir.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] oreFiles = oreDir.listFiles((dir, name) -> name.endsWith(".png"));

        if (iconFiles != null) {
            for (File f : iconFiles) {
                assembler.addIcon(f);
            }
        }
        if (materialFluidFiles != null) {
            for (File f : materialFluidFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }

        if (fluidFiles != null) {
            for (File f : fluidFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }
        if (oreFiles != null) {
            for (File f : oreFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }
    }

    private static void addLangs(ResourcePackAssembler assembler) {
        File langDir = new File(configDirectory, "lang");
        if (!langDir.exists()) langDir.mkdirs();

        File[] langFiles = langDir.listFiles((dir, name) -> name.endsWith(".lang"));
        if (langFiles != null) {
            for (File f : langFiles) {
                assembler.addLang(f);
            }
        }
    }

    public static void processConfig(Configuration config) {
        manaPowerStorageBase = config
            .get(
                sectionMana.name,
                "manaPowerStorageBase",
                manaPowerStorageBase,
                "Base amount of power stored by mana items.")
            .getInt(manaPowerStorageBase);

        manaUpgradeDiamondCost = config
            .get(
                sectionMana.name,
                "manaUpgradeDiamondCost",
                manaUpgradeDiamondCost,
                "Number of levels required for the 'Empowered.")
            .getInt(manaUpgradeDiamondCost);

        addDurabilityTootip = config
            .get(
                sectionPersonal.name,
                "addDurabilityTootip",
                addFuelTooltipsToAllFluidContainers,
                "If true, adds durability tooltips to tools and armor")
            .getBoolean(addDurabilityTootip);

        renderDurabilityBar = config
            .get(
                sectionPersonal.name,
                "renderDurabilityBar",
                renderDurabilityBar,
                "If true, render the bar when an item is damaged")
            .getBoolean(renderDurabilityBar);

        renderChargeBar = config
            .get(
                sectionPersonal.name,
                "renderChargeBar",
                renderChargeBar,
                "If true, render the bar when an item has RF")
            .getBoolean(renderChargeBar);

        renderPufferFish = config
            .get(
                sectionPersonal.name,
                "renderPufferFish",
                renderPufferFish,
                "If true, render Pufferfish with 3d model with some things. Requires game restart.")
            .getBoolean(renderPufferFish);

        useWDMLA = config.get(sectionPersonal.name, "useWDMLA", useWDMLA, "If true, use WDMLA. Requires game restart.")
            .getBoolean(useWDMLA);

        // Damage Indicator

        showDamageParticles = config
            .get(
                sectionDamageIndicators.name,
                "showDamageParticles",
                showDamageParticles,
                "If true, render damage particles")
            .getBoolean(showDamageParticles);

        // IE

        increasedRenderboxes = config
            .get(
                sectionIE.name,
                "increasedRenderboxes",
                increasedRenderboxes,
                "If true, render increased Renderboxes (useful for long wires or custom visuals)")
            .getBoolean(increasedRenderboxes);

        validateConnections = config
            .get(
                sectionIE.name,
                "validateConnections",
                validateConnections,
                "If true, validate cable connections on load (may affect performance with many wires)")
            .getBoolean(validateConnections);

        int wireCount = WireType.uniqueNames.length;

        if (cableLength == null || cableLength.length < wireCount) {
            cableLength = new int[] { 16, 16, 32, 32, 32 };
        }
        cableLength = config
            .get(
                sectionIE.name,
                "cableLength",
                cableLength,
                "The maximum length cables can have. Format: " + Arrays.toString(WireType.uniqueNames))
            .getIntList();

        if (cableTransferRate == null || cableTransferRate.length < wireCount) {
            cableTransferRate = new int[] { 2048, 8192, 32768, 0, 0 };
        }
        cableTransferRate = config
            .get(
                sectionIE.name,
                "cableTransferRate",
                cableTransferRate,
                "Cable transfer rate per tier. Format: " + Arrays.toString(WireType.uniqueNames))
            .getIntList();

        if (cableLossRatio == null || cableLossRatio.length < wireCount) {
            cableLossRatio = new double[] { 0.05, 0.025, 0.025, 1.0, 1.0 };
        }
        cableLossRatio = config
            .get(
                sectionIE.name,
                "cableLossRatio",
                cableLossRatio,
                "Cable energy loss per 16 blocks. Format: " + Arrays.toString(WireType.uniqueNames))
            .getDoubleList();

        if (cableColouration == null || cableColouration.length < wireCount) {
            cableColouration = new int[] { 0xD48040, 0xEDC36C, 0x6C6C6C, 0x969696, 0x6F6F6F };
        }
        cableColouration = config
            .get(
                sectionIE.name,
                "cableColouration",
                cableColouration,
                "Cable color RGB (int). Format: " + Arrays.toString(WireType.uniqueNames))
            .getIntList();

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

    public static void init() {}

    public static void postInit() {}

    private Config() {}
}
