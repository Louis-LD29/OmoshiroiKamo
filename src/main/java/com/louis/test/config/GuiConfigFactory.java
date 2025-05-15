package com.louis.test.config;

import com.louis.test.Test;

import java.util.ArrayList;
import java.util.List;

import com.louis.test.lib.LibMisc;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiConfigFactory extends GuiConfig {
    public GuiConfigFactory(GuiScreen parentScreen) {
        super(
            parentScreen,
            getConfigElements(parentScreen),
            LibMisc.MOD_ID,
            false,
            false,
            LibMisc.lang.localize("config.title"));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = LibMisc.lang.addPrefix("config.");

        for (Config.Section section : Config.sections) {
            list.add(
                new ConfigElement<ConfigCategory>(
                    Config.config.getCategory(section.lc()).setLanguageKey(prefix + section.lang)));
        }

        return list;
    }
}
