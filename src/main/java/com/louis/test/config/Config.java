package com.louis.test.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.louis.test.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import com.enderio.core.common.event.ConfigFileChangedEvent;

import com.louis.test.core.network.PacketHandler;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

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

    public static File configDirectory;

    public static int manaPowerStorageBase = 50000;
    public static int manaUpgradeDiamondCost = 10;

    public static boolean addDurabilityTootip = true;
    public static boolean addFuelTooltipsToAllFluidContainers = true;
    public static boolean renderDurabilityBar = true;
    public static boolean renderChargeBar = true;

    public static void load(FMLPreInitializationEvent event) {
        PacketHandler.INSTANCE
            .registerMessage(PacketConfigSync.class, PacketConfigSync.class, PacketHandler.nextID(), Side.CLIENT);

        FMLCommonHandler.instance().bus().register(new Config());
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

    public static void processConfig(Configuration config) {
        manaPowerStorageBase = config.get(
            sectionMana.name,
            "manaPowerStorageBase",
            manaPowerStorageBase,
            "Base amount of power stored by mana items.").getInt(manaPowerStorageBase);

        manaUpgradeDiamondCost = config.get(
            sectionMana.name,
            "manaUpgradeDiamondCost",
            manaUpgradeDiamondCost,
            "Number of levels required for the 'Empowered.").getInt(manaUpgradeDiamondCost);

        addDurabilityTootip = config.get(
            sectionPersonal.name,
            "addDurabilityTootip",
            addFuelTooltipsToAllFluidContainers,
            "If true, adds durability tooltips to tools and armor").getBoolean(addDurabilityTootip);

        renderDurabilityBar = config.get(
            sectionPersonal.name,
            "renderDurabilityBar",
            renderDurabilityBar,
            "If true, render the bar when an item is damaged").getBoolean(renderDurabilityBar);

        renderChargeBar = config.get(
            sectionPersonal.name,
            "renderChargeBar",
            renderChargeBar,
            "If true, render the bar when an item has RF").getBoolean(renderChargeBar);

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

    private Config() {}
}
