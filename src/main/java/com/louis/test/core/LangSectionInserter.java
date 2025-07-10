package com.louis.test.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

import com.louis.test.api.enums.Material;

public class LangSectionInserter {

    private static final String LANG_PATH = "src/main/resources/assets/test/lang/en_US.lang";

    // Section headers to inject entries under
    private static final Map<String, SectionGenerator> SECTIONS = new LinkedHashMap<>();

    static {
        SECTIONS.put(
            "#Item Wire Coil",
            material -> "item.itemWireCoil." + material.name()
                .toLowerCase() + ".name=Wire Coil (" + material.getDisplayName() + ")");
        SECTIONS.put(
            "#Item Material Ingot",
            material -> "item.itemMaterial.ingot." + material.name()
                .toLowerCase() + ".name=" + material.getDisplayName() + " Ingot");
        SECTIONS.put(
            "#Item Material Nugget",
            material -> "item.itemMaterial.nugget." + material.name()
                .toLowerCase() + ".name=" + material.getDisplayName() + " Nugget");
        SECTIONS.put(
            "#Item Material Plate",
            material -> "item.itemMaterial.plate." + material.name()
                .toLowerCase() + ".name=" + material.getDisplayName() + " Plate");
        SECTIONS.put(
            "#Item Material Rod",
            material -> "item.itemMaterial.rod." + material.name()
                .toLowerCase() + ".name=" + material.getDisplayName() + " Rod");
        SECTIONS.put(
            "#Item Material Dust",
            material -> "item.itemMaterial.dust." + material.name()
                .toLowerCase() + ".name=" + material.getDisplayName() + " Dust");

        SECTIONS.put(
            "#Block of Material",
            material -> "tile.blockMaterial." + material.name()
                .toLowerCase() + ".name=Block of " + material.getDisplayName());
        SECTIONS.put(
            "#Energy Input",
            material -> "tile.blockEnergyInOut.input." + material.name()
                .toLowerCase() + ".name=Energy Input (" + material.getDisplayName() + ")");
        SECTIONS.put(
            "#Energy Output",
            material -> "tile.blockEnergyInOut.output." + material.name()
                .toLowerCase() + ".name=Energy Output (" + material.getDisplayName() + ")");
        SECTIONS.put(
            "#Fluid Input",
            material -> "tile.blockFluidInOut.input." + material.name()
                .toLowerCase() + ".name=Fluid Input (" + material.getDisplayName() + ")");
        SECTIONS.put(
            "#Fluid Output",
            material -> "tile.blockFluidInOut.output." + material.name()
                .toLowerCase() + ".name=Fluid Output (" + material.getDisplayName() + ")");

    }

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>(Files.readAllLines(new File(LANG_PATH).toPath()));
        Set<String> existingKeys = new HashSet<>();

        // Collect all existing keys
        for (String line : lines) {
            if (line.contains("=")) {
                existingKeys.add(line.substring(0, line.indexOf('=')));
            }
        }

        // Output lines to be re-written
        List<String> result = new ArrayList<>();
        Iterator<String> it = lines.iterator();

        while (it.hasNext()) {
            String line = it.next();
            result.add(line);

            if (SECTIONS.containsKey(line.trim())) {
                SectionGenerator generator = SECTIONS.get(line.trim());
                for (Material material : Material.values()) {
                    String entry = generator.generate(material);
                    String key = entry.substring(0, entry.indexOf('='));
                    if (!existingKeys.contains(key)) {
                        result.add(entry);
                        existingKeys.add(key);
                    }
                }
            }
        }

        // Write back
        try (PrintWriter out = new PrintWriter(LANG_PATH)) {
            for (String l : result) out.println(l);
        }

        System.out.println("✅ Updated lang file with material entries.");
    }

    interface SectionGenerator {

        String generate(Material material);
    }
}
