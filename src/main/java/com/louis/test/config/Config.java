package com.louis.test.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraftforge.common.config.Configuration;

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

    public static boolean addDurabilityTootip = true;

    public static int manaUpgradeDiamondCost = 10;
    public static int manaPowerStorageBase = 50000;




    public static String greeting = "Hello World";

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
