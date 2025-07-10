package com.louis.test.common.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import com.louis.test.core.lib.LibMisc;
import com.louis.test.core.network.PacketHandler;

import blusunrize.immersiveengineering.api.energy.WireType;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class Config {

    public static final List<Section> sections = new ArrayList<>();
    public static final Section sectionMana = new Section("Mana", "mana");
    public static final Section sectionPersonal = new Section("Personal Settings", "personal");
    public static final Section sectionIE = new Section("Immersive Engineering Settings", "ie");
    public static Configuration config;
    public static File configDirectory;
    public static int manaPowerStorageBase = 50000;
    public static int manaUpgradeDiamondCost = 10;
    public static int PowerUserPerTickRF = 20;
    public static boolean addDurabilityTootip = true;
    public static boolean addFuelTooltipsToAllFluidContainers = true;
    public static boolean renderDurabilityBar = true;
    public static boolean renderChargeBar = true;
    public static boolean increasedRenderboxes = true;
    public static boolean validateConnections = true;
    public static int[] cableLength = new int[] { 16, 16, 32, 32, 32 };
    public static double[] cableLossRatio = new double[] { 0.05, 0.025, 0.025, 1.0, 1.0 };
    public static int[] cableTransferRate = new int[] { 2048, 4096, 8192, 0, 0 };
    public static int[] cableColouration = new int[] { 13926474, 15576418, 7303023, 9862765, 7303023 };

    private Config() {}

    public static void preInit(FMLPreInitializationEvent event) {
        PacketHandler.INSTANCE
            .registerMessage(PacketConfigSync.class, PacketConfigSync.class, PacketHandler.nextID(), Side.CLIENT);

        FMLCommonHandler.instance()
            .bus()
            .register(new Config());
        configDirectory = new File(event.getModConfigurationDirectory(), LibMisc.DOMAIN);
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }

        File configFile = new File(configDirectory, LibMisc.MOD_ID + ".cfg");
        config = new Configuration(configFile);
        syncConfig(false);
    }

    public static void syncConfig(boolean load) {
        try {
            if (load) {
                config.load();
            }
            Config.processConfig(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (config.hasChanged()) {
                config.save();
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

        // cableLossRatio
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

    }

    public static void init() {}

    public static void postInit() {}

    public static ItemStack getStackForString(String s) {
        String[] nameAndMeta = s.split(";");
        int meta = nameAndMeta.length == 1 ? 0 : Integer.parseInt(nameAndMeta[1]);
        String[] data = nameAndMeta[0].split(":");
        ItemStack stack = GameRegistry.findItemStack(data[0], data[1], 1);
        if (stack == null) {
            return null;
        }
        stack.setItemDamage(meta);
        return stack;
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.modID.equals(LibMisc.MOD_ID)) {
            syncConfig(false);
            init();
            postInit();
        }
    }

    @SubscribeEvent
    public void onConfigFileChanged(ConfigFileChangedEvent event) {
        if (event.modID.equals(LibMisc.MOD_ID)) {
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
}
