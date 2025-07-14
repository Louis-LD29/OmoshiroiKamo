package com.louis.test.common.core.lang;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.louis.test.api.material.MaterialEntry;
import com.louis.test.api.material.MaterialRegistry;
import com.louis.test.common.config.Config;
import com.louis.test.common.core.helper.Logger;

public class LangSectionInserter {

    private static final String LANG_PATH = "src/main/resources/assets/test/lang/en_US.lang";

    private static final Map<String, SectionGenerator> SECTIONS = new LinkedHashMap<>();

    static {
        SECTIONS.put(
            "#Item Wire Coil",
            material -> "item.itemWireCoil." + material.getUnlocalizedName()
                + ".name=Wire Coil ("
                + material.getName()
                + ")");
        SECTIONS.put(
            "#Item Material Ingot",
            material -> "item.itemMaterial.ingot." + material.getUnlocalizedName()
                + ".name="
                + material.getName()
                + " Ingot");
        SECTIONS.put(
            "#Item Material Nugget",
            material -> "item.itemMaterial.nugget." + material.getUnlocalizedName()
                + ".name="
                + material.getName()
                + " Nugget");
        SECTIONS.put(
            "#Item Material Plate",
            material -> "item.itemMaterial.plate." + material.getUnlocalizedName()
                + ".name="
                + material.getName()
                + " Plate");
        SECTIONS.put(
            "#Item Material Rod",
            material -> "item.itemMaterial.rod." + material.getUnlocalizedName()
                + ".name="
                + material.getName()
                + " Rod");
        SECTIONS.put(
            "#Item Material Dust",
            material -> "item.itemMaterial.dust." + material.getUnlocalizedName()
                + ".name="
                + material.getName()
                + " Dust");

        SECTIONS.put(
            "#Item Bucket Material",
            material -> "item.itemBucketMaterial." + material.getUnlocalizedName()
                + ".name=Molten "
                + material.getName()
                + " Bucket");

        SECTIONS.put(
            "#Block of Material",
            material -> "tile.blockMaterial." + material.getUnlocalizedName() + ".name=Block of " + material.getName());
        SECTIONS.put(
            "#Energy Input",
            material -> "tile.blockEnergyInOut.input." + material.getUnlocalizedName()
                + ".name=Energy Input ("
                + material.getName()
                + ")");
        SECTIONS.put(
            "#Energy Output",
            material -> "tile.blockEnergyInOut.output." + material.getUnlocalizedName()
                + ".name=Energy Output ("
                + material.getName()
                + ")");
        SECTIONS.put(
            "#Fluid Input",
            material -> "tile.blockFluidInOut.input." + material.getUnlocalizedName()
                + ".name=Fluid Input ("
                + material.getName()
                + ")");
        SECTIONS.put(
            "#Fluid Output",
            material -> "tile.blockFluidInOut.output." + material.getUnlocalizedName()
                + ".name=Fluid Output ("
                + material.getName()
                + ")");

        SECTIONS.put(
            "#Block Fluid Material",
            material -> "tile.fluid." + StringUtils.uncapitalize(material.getUnlocalizedName())
                + ".molten.name=Molten "
                + material.getName());
        SECTIONS.put(
            "#Fluid Material",
            material -> "fluid.molten." + StringUtils.uncapitalize(material.getUnlocalizedName())
                + "=Molten "
                + material.getName());

        SECTIONS.put(
            "#Tic Material",
            material -> "material." + StringUtils.uncapitalize(material.getUnlocalizedName())
                + "="
                + material.getName());

    }

    public static void main(String[] args) throws IOException {
        File langFile = new File(LANG_PATH);

        List<String> existingLines = langFile.exists() ? Files.readAllLines(langFile.toPath()) : new ArrayList<>();

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

        List<String> autogenBlock = new ArrayList<>();
        autogenBlock.add("# BEGIN AUTOGEN");

        Set<String> addedKeys = new HashSet<>();

        MaterialRegistry.init();
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

            autogenBlock.add("");
        }

        autogenBlock.add("# END AUTOGEN");

        if (!cleaned.isEmpty() && !cleaned.get(cleaned.size() - 1)
            .isEmpty()) {
            cleaned.add("");
        }
        cleaned.addAll(autogenBlock);

        try (PrintWriter out = new PrintWriter(langFile)) {
            for (String l : cleaned) {
                out.println(l);
            }
        }

        Logger.info(
            "[LangSectionInserter] Synced lang file with updated sections. Missing ones added, obsolete ones removed.");
    }

    public static void insertCustomMaterialsLang(String[] materialNames) {
        try {
            File file = new File(Config.configDirectory.getAbsolutePath() + "/lang", "en_US.lang");

            List<String> existingLines = file.exists() ? Files.readAllLines(file.toPath()) : new ArrayList<>();

            List<MaterialEntry> materials = new ArrayList<>();
            for (String name : materialNames) {
                MaterialEntry entry = MaterialRegistry.get(name);
                if (entry != null) {
                    materials.add(entry);
                }
            }

            List<String> newLangBlock = new ArrayList<>();
            newLangBlock.add("# BEGIN AUTOGEN (Custom Materials)");
            for (Map.Entry<String, SectionGenerator> section : SECTIONS.entrySet()) {
                newLangBlock.add(section.getKey());
                for (MaterialEntry mat : materials) {
                    newLangBlock.add(
                        section.getValue()
                            .generate(mat));
                }
                newLangBlock.add("");
            }
            newLangBlock.add("# END AUTOGEN");

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

            if (!cleaned.isEmpty() && !cleaned.get(cleaned.size() - 1)
                .isEmpty()) {
                cleaned.add("");
            }
            cleaned.addAll(newLangBlock);

            try (PrintWriter out = new PrintWriter(file)) {
                for (String l : cleaned) {
                    out.println(l);
                }
            }

            Logger.info("[LangSectionInserter] Synced config lang with custom materials: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface SectionGenerator {

        String generate(MaterialEntry material);
    }
}
