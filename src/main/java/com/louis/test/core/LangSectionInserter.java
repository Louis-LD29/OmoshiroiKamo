package com.louis.test.core;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import com.google.common.base.Charsets;
import com.louis.test.api.MaterialEntry;
import com.louis.test.api.MaterialRegistry;
import com.louis.test.common.config.Config;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LangSectionInserter {

    private static final String LANG_PATH = "src/main/resources/assets/test/lang/en_US.lang";

    // Section headers to inject entries under
    private static final Map<String, SectionGenerator> SECTIONS = new LinkedHashMap<>();

    static {
        SECTIONS.put(
            "#Item Wire Coil",
            material -> "item.itemWireCoil." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Wire Coil (" + material.name + ")");
        SECTIONS.put(
            "#Item Material Ingot",
            material -> "item.itemMaterial.ingot." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=" + material.name + " Ingot");
        SECTIONS.put(
            "#Item Material Nugget",
            material -> "item.itemMaterial.nugget." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=" + material.name + " Nugget");
        SECTIONS.put(
            "#Item Material Plate",
            material -> "item.itemMaterial.plate." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=" + material.name + " Plate");
        SECTIONS.put(
            "#Item Material Rod",
            material -> "item.itemMaterial.rod." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=" + material.name + " Rod");
        SECTIONS.put(
            "#Item Material Dust",
            material -> "item.itemMaterial.dust." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=" + material.name + " Dust");

        SECTIONS.put(
            "#Block of Material",
            material -> "tile.blockMaterial." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Block of " + material.name);
        SECTIONS.put(
            "#Energy Input",
            material -> "tile.blockEnergyInOut.input." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Energy Input (" + material.name + ")");
        SECTIONS.put(
            "#Energy Output",
            material -> "tile.blockEnergyInOut.output." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Energy Output (" + material.name + ")");
        SECTIONS.put(
            "#Fluid Input",
            material -> "tile.blockFluidInOut.input." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Fluid Input (" + material.name + ")");
        SECTIONS.put(
            "#Fluid Output",
            material -> "tile.blockFluidInOut.output." + material.name.toLowerCase(Locale.ROOT)
                .replace(" ", "_") + ".name=Fluid Output (" + material.name + ")");

    }

    public static void main(String[] args) throws IOException {
        File langFile = new File(LANG_PATH);

        // Đọc toàn bộ nội dung file hiện có
        List<String> existingLines = langFile.exists() ? Files.readAllLines(langFile.toPath()) : new ArrayList<>();

        // Loại bỏ khối cũ giữa # BEGIN AUTOGEN và # END AUTOGEN
        List<String> cleaned = new ArrayList<>();
        boolean insideAutogen = false;

        for (String line : existingLines) {
            if (line.trim()
                .equals("# BEGIN AUTOGEN")) {
                insideAutogen = true;
                continue;
            }
            if (line.trim()
                .equals("# END AUTOGEN")) {
                insideAutogen = false;
                continue;
            }
            if (!insideAutogen) {
                cleaned.add(line);
            }
        }

        // Tạo khối mới
        List<String> autogenBlock = new ArrayList<>();
        autogenBlock.add("# BEGIN AUTOGEN");

        Set<String> addedKeys = new HashSet<>();

        for (Map.Entry<String, SectionGenerator> section : SECTIONS.entrySet()) {
            autogenBlock.add(section.getKey());

            for (MaterialEntry material : MaterialRegistry.all()) {
                String line = section.getValue()
                    .generate(material);
                String key = line.substring(0, line.indexOf("="));
                if (addedKeys.add(key)) {
                    autogenBlock.add(line);
                }
            }

            autogenBlock.add(""); // dòng trống ngăn cách section
        }

        autogenBlock.add("# END AUTOGEN");

        // Gắn block vào cuối
        if (!cleaned.isEmpty() && !cleaned.get(cleaned.size() - 1)
            .isEmpty()) {
            cleaned.add(""); // đảm bảo có dòng trắng
        }
        cleaned.addAll(autogenBlock);

        // Ghi lại toàn bộ file
        try (PrintWriter out = new PrintWriter(langFile)) {
            for (String l : cleaned) {
                out.println(l);
            }
        }

        System.out.println("✅ Synced lang file with updated sections. Missing ones added, obsolete ones removed.");
    }

    public static void insertCustomMaterialsLang(String[] materialNames) {
        try {
            File file = new File(Config.configDirectory, "en_US.lang");

            List<String> existingLines = file.exists() ? Files.readAllLines(file.toPath()) : new ArrayList<>();

            // Tập hợp material hợp lệ
            List<MaterialEntry> materials = new ArrayList<>();
            for (String name : materialNames) {
                MaterialEntry entry = MaterialRegistry.get(name);
                if (entry != null) {
                    materials.add(entry);
                }
            }

            // Xây dựng khối lang mới
            List<String> newLangBlock = new ArrayList<>();
            newLangBlock.add("# BEGIN AUTOGEN (Custom Materials)");
            for (Map.Entry<String, SectionGenerator> section : SECTIONS.entrySet()) {
                newLangBlock.add(section.getKey());
                for (MaterialEntry mat : materials) {
                    newLangBlock.add(
                        section.getValue()
                            .generate(mat));
                }
                newLangBlock.add(""); // dòng trống giữa các section
            }
            newLangBlock.add("# END AUTOGEN");

            // Gỡ toàn bộ block cũ (nếu có)
            List<String> cleaned = new ArrayList<>();
            boolean insideAuto = false;
            for (String line : existingLines) {
                if (line.trim()
                    .equals("# BEGIN AUTOGEN (Custom Materials)")) {
                    insideAuto = true;
                    continue;
                }
                if (line.trim()
                    .equals("# END AUTOGEN")) {
                    insideAuto = false;
                    continue;
                }
                if (!insideAuto) {
                    cleaned.add(line);
                }
            }

            // Gắn block mới vào cuối
            if (!cleaned.isEmpty() && !cleaned.get(cleaned.size() - 1)
                .isEmpty()) {
                cleaned.add(""); // đảm bảo có dòng trắng ngăn cách
            }
            cleaned.addAll(newLangBlock);

            // Ghi vào file
            try (PrintWriter out = new PrintWriter(file)) {
                for (String l : cleaned) {
                    out.println(l);
                }
            }

            System.out.println("✅ Synced config lang with custom materials: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadExternalLangFile(File file) {
        if (!file.exists()) return;

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), Charsets.UTF_8)) {
            Properties props = new Properties();
            props.load(reader);

            HashMap<String, String> langMap = new HashMap<>();
            for (String key : props.stringPropertyNames()) {
                langMap.put(key, props.getProperty(key));
            }

            LanguageRegistry.instance()
                .injectLanguage("en_US", langMap);
            System.out.println("✅ Injected " + langMap.size() + " entries from " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface SectionGenerator {

        String generate(MaterialEntry material);
    }
}
