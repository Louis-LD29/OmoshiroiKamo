package louis.omoshiroikamo.client.texture.fluid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.common.fluid.FluidMaterialRegister;
import louis.omoshiroikamo.common.util.lib.LibResources;

@SideOnly(Side.CLIENT)
public class FluidMaterialTexture {

    private static BufferedImage baseStill, baseFlow;
    public static File CONFIG_FLUID_DIR;

    public static void applyAll(File configDirectory) {
        CONFIG_FLUID_DIR = new File(configDirectory.getAbsolutePath() + "/" + LibResources.PREFIX_MATERIAL_FLUID_ICONS);
        initBaseTextures();
        for (Map.Entry<MaterialEntry, Fluid> entry : FluidMaterialRegister.FLUIDS.entrySet()) {
            apply(entry.getValue(), entry.getKey());
        }
        cleanUnusedTextures();
    }

    public static void apply(Fluid fluid, MaterialEntry entry) {
        String name = fluid.getName()
            .replace(".molten", ""); // Safer
        int color = entry.getColor();
        String tinkerBaseName = "liquid_" + name;

        try {
            applyTexture(tinkerBaseName, baseStill, color, true, 2);
            applyTexture(tinkerBaseName + "_flow", baseFlow, color, false, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void applyTexture(String baseName, BufferedImage baseImage, int color, boolean reverse,
                                     int frametime) throws IOException {

        File imageFile = new File(CONFIG_FLUID_DIR, baseName + ".png");
        File colorFile = new File(CONFIG_FLUID_DIR, baseName + ".color.txt");
        File mcmetaFile = new File(imageFile.getAbsolutePath() + ".mcmeta");

        String currentColor = String.format("%06X", color)
            .toUpperCase();
        boolean shouldRegen = true;

        if (imageFile.exists() && colorFile.exists()) {
            String savedColor = Files.readAllLines(colorFile.toPath())
                .get(0);
            if (savedColor.equals(currentColor)) {
                shouldRegen = false;
            } else {
                imageFile.delete();
                mcmetaFile.delete();
            }
        }

        if (shouldRegen) {
            BufferedImage tinted = tint(baseImage, color);
            ImageIO.write(tinted, "png", imageFile);
            writeMcmetaFile(imageFile, tinted.getHeight() / 16, reverse, frametime);
            try (FileWriter writer = new FileWriter(colorFile)) {
                writer.write(currentColor);
            }
        }
    }

    private static void initBaseTextures() {
        if (baseStill != null && baseFlow != null) {
            return;
        }

        try {
            ResourceLocation stillLoc = new ResourceLocation(
                LibResources.PREFIX_MOD + "textures/blocks/liquid_base.png");
            ResourceLocation flowLoc = new ResourceLocation(
                LibResources.PREFIX_MOD + "textures/blocks/liquid_base_flow.png");

            baseStill = ImageIO.read(
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(stillLoc)
                    .getInputStream());
            baseFlow = ImageIO.read(
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(flowLoc)
                    .getInputStream());

            if (!CONFIG_FLUID_DIR.exists()) {
                CONFIG_FLUID_DIR.mkdirs();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load base textures", e);
        }
    }

    private static BufferedImage tint(BufferedImage base, int color) {
        BufferedImage result = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        float rTint = ((color >> 16) & 0xFF) / 255.0f;
        float gTint = ((color >> 8) & 0xFF) / 255.0f;
        float bTint = (color & 0xFF) / 255.0f;

        for (int x = 0; x < base.getWidth(); x++) {
            for (int y = 0; y < base.getHeight(); y++) {
                int rgba = base.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xFF;
                int r = (int) (((rgba >> 16) & 0xFF) * rTint);
                int g = (int) (((rgba >> 8) & 0xFF) * gTint);
                int b = (int) ((rgba & 0xFF) * bTint);
                int tinted = (alpha << 24) | (r << 16) | (g << 8) | b;
                result.setRGB(x, y, tinted);
            }
        }
        return result;
    }

    private static void writeMcmetaFile(File pngFile, int frameCount, boolean reverse, int frametime)
        throws IOException {
        File mcmeta = new File(pngFile.getAbsolutePath() + ".mcmeta");
        if (mcmeta.exists()) {
            return;
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"animation\": {\n");
        json.append("    \"frametime\": ")
            .append(frametime);

        if (reverse) {
            json.append(",\n    \"frames\": [\n");
            json.append("      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,\n");
            json.append("      18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1\n");
            json.append("    ]");
        }

        json.append("\n  }\n}");

        try (FileWriter writer = new FileWriter(mcmeta)) {
            writer.write(json.toString());
        }
    }

    public static void cleanUnusedTextures() {
        if (!CONFIG_FLUID_DIR.exists()) {
            return;
        }

        Set<String> validNames = new HashSet<>();
        for (Map.Entry<MaterialEntry, Fluid> entry : FluidMaterialRegister.FLUIDS.entrySet()) {
            String name = entry.getValue()
                .getName()
                .replace(".molten", "");
            String base = "liquid_" + name;

            validNames.add(base + ".png");
            validNames.add(base + "_flow.png");
            validNames.add(base + ".png.mcmeta");
            validNames.add(base + "_flow.png.mcmeta");
            validNames.add(base + ".color.txt");
            validNames.add(base + "_flow.color.txt");
        }

        File[] files = CONFIG_FLUID_DIR.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            if (!validNames.contains(file.getName())) {
                file.delete();
            }
        }
    }
}
