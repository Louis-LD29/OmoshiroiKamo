package ruiseki.omoshiroikamo.client.texture.fluid;

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
import ruiseki.omoshiroikamo.api.fluid.FluidEntry;
import ruiseki.omoshiroikamo.common.fluid.FluidRegister;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

@SideOnly(Side.CLIENT)
public class FluidTexture {

    private static BufferedImage baseStill, baseFlow;
    public static File CONFIG_FLUID_DIR;

    public static void applyAll(File configDirectory) {
        CONFIG_FLUID_DIR = new File(configDirectory.getAbsolutePath() + "/" + LibResources.PREFIX_FLUID_ICONS);
        initBaseTextures();
        for (Map.Entry<FluidEntry, Fluid> entry : FluidRegister.FLUIDS.entrySet()) {
            apply(entry.getValue(), entry.getKey());
        }
        cleanUnusedTextures();
    }

    public static void apply(Fluid fluid, FluidEntry entry) {
        String name = fluid.getName();
        int color = entry.getColor();
        String baseName = "fluid_" + name;

        try {
            applyTexture(baseName, baseStill, baseFlow, color, true, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void applyTexture(String baseName, BufferedImage stillBase, BufferedImage flowBase, int color,
        boolean writeFrametime, int frametime) throws IOException {
        File stillFile = new File(CONFIG_FLUID_DIR, baseName + ".png");
        File flowFile = new File(CONFIG_FLUID_DIR, baseName + "_flow.png");
        File mcmetaStill = new File(stillFile.getAbsolutePath() + ".mcmeta");
        File mcmetaFlow = new File(flowFile.getAbsolutePath() + ".mcmeta");
        File colorFile = new File(CONFIG_FLUID_DIR, baseName + ".color.txt");

        String currentColor = String.format("%06X", color)
            .toUpperCase();
        boolean shouldRegen = true;

        if (stillFile.exists() && flowFile.exists() && colorFile.exists()) {
            String savedColor = Files.readAllLines(colorFile.toPath())
                .get(0);
            if (savedColor.equals(currentColor)) {
                shouldRegen = false;
            } else {
                stillFile.delete();
                flowFile.delete();
                mcmetaStill.delete();
                mcmetaFlow.delete();
            }
        }

        if (shouldRegen) {
            BufferedImage still = tint(stillBase, color);
            BufferedImage flow = tint(flowBase, color);

            ImageIO.write(still, "png", stillFile);
            ImageIO.write(flow, "png", flowFile);

            writeMcmetaFile(stillFile, writeFrametime, frametime);
            writeMcmetaFile(flowFile, false, 0);

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
                LibResources.PREFIX_MOD + "textures/blocks/fluid_base.png");
            ResourceLocation flowLoc = new ResourceLocation(
                LibResources.PREFIX_MOD + "textures/blocks/fluid_base_flow.png");

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

    private static void writeMcmetaFile(File pngFile, boolean writeFrametime, int frametime) throws IOException {
        File mcmeta = new File(pngFile.getAbsolutePath() + ".mcmeta");
        if (mcmeta.exists()) {
            return;
        }

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"animation\": ");

        if (writeFrametime) {
            json.append("{\n    \"frametime\": ")
                .append(frametime)
                .append("\n  }");
        } else {
            json.append("{}");
        }

        json.append("\n}");

        try (FileWriter writer = new FileWriter(mcmeta)) {
            writer.write(json.toString());
        }
    }

    public static void cleanUnusedTextures() {
        if (!CONFIG_FLUID_DIR.exists()) {
            return;
        }

        Set<String> validNames = new HashSet<>();
        for (Map.Entry<FluidEntry, Fluid> entry : FluidRegister.FLUIDS.entrySet()) {
            String name = entry.getValue()
                .getName();
            String base = "fluid_" + name;

            validNames.add(base + ".png");
            validNames.add(base + "_flow.png");
            validNames.add(base + ".png.mcmeta");
            validNames.add(base + "_flow.png.mcmeta");
            validNames.add(base + ".color.txt");
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
