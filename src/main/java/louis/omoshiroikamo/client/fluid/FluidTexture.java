package louis.omoshiroikamo.client.fluid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.fluid.FluidEntry;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.fluid.FluidRegister;

@SideOnly(Side.CLIENT)
public class FluidTexture {

    private static BufferedImage baseStill, baseFlow;
    public static final File CONFIG_FLUID_DIR = new File(
        Config.configDirectory.getAbsolutePath() + "/" + LibResources.PREFIX_FLUID_ICONS);

    public static void applyAll() {
        initBaseTextures();
        for (Map.Entry<FluidEntry, Fluid> entry : FluidRegister.FLUIDS.entrySet()) {
            apply(entry.getValue(), entry.getKey());
        }
        cleanUnusedTextures();
    }

    public static void apply(Fluid fluid, FluidEntry entry) {
        String name = fluid.getName(); // Safer
        int color = entry.getColor();

        try {
            String tinkerBaseName = "fluid_" + name;
            File stillFile = new File(CONFIG_FLUID_DIR, tinkerBaseName + ".png");
            File flowFile = new File(CONFIG_FLUID_DIR, tinkerBaseName + "_flow.png");

            if (!stillFile.exists()) {
                BufferedImage still = tint(baseStill, color);
                ImageIO.write(still, "png", stillFile);
                writeMcmetaFile(stillFile, true, 2);
            }

            if (!flowFile.exists()) {
                BufferedImage flow = tint(baseFlow, color);
                ImageIO.write(flow, "png", flowFile);
                writeMcmetaFile(flowFile, false, 0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initBaseTextures() {
        if (baseStill != null && baseFlow != null) return;

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
                boolean created = CONFIG_FLUID_DIR.mkdirs();
            } else {}

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

    private static void writeMcmetaFile(File pngFile, boolean reverse, int frametime) throws IOException {
        File mcmeta = new File(pngFile.getAbsolutePath() + ".mcmeta");
        if (mcmeta.exists()) return;

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"animation\": ");

        if (reverse) {
            // reverse == false → có frametime
            json.append("{\n");
            json.append("    \"frametime\": ")
                .append(frametime)
                .append("\n");
            json.append("  }");
        } else {
            // reverse == true → không có frametime
            json.append("{}");
        }

        json.append("\n}");

        try (FileWriter writer = new FileWriter(mcmeta)) {
            writer.write(json.toString());
        }
    }

    public static void cleanUnusedTextures() {
        if (!CONFIG_FLUID_DIR.exists()) return;

        Set<String> validNames = new HashSet<>();
        for (Map.Entry<FluidEntry, Fluid> entry : FluidRegister.FLUIDS.entrySet()) {
            String name = entry.getValue()
                .getName();
            String base = "fluid_" + name;

            validNames.add(base + ".png");
            validNames.add(base + "_flow.png");
            validNames.add(base + ".png.mcmeta");
            validNames.add(base + "_flow.png.mcmeta");
        }

        File[] files = CONFIG_FLUID_DIR.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.isFile()) continue;
            if (!validNames.contains(file.getName())) {
                boolean deleted = file.delete();
            }
        }
    }
}
