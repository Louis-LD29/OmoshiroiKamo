package com.louis.test.client.fluid;

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

import com.louis.test.api.material.MaterialEntry;
import com.louis.test.common.config.Config;
import com.louis.test.common.core.helper.Logger;
import com.louis.test.common.core.lib.LibResources;
import com.louis.test.common.fluid.material.BlockFluidMaterial;
import com.louis.test.common.fluid.material.FluidMaterialRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FluidTextureApplier {

    private static BufferedImage baseStill, baseFlow;
    public static final File CONFIG_FLUID_DIR = new File(Config.configDirectory.getAbsolutePath() + "/icons");

    public static void applyAll() {
        initBaseTextures();
        for (Map.Entry<MaterialEntry, Fluid> entry : FluidMaterialRegistry.FLUIDS.entrySet()) {
            apply(entry.getValue(), entry.getKey());
        }
        cleanUnusedTextures();
    }

    public static void apply(Fluid fluid, MaterialEntry entry) {
        String name = fluid.getName()
            .replace(".molten", ""); // Safer
        int color = entry.getColor();

        if (!(fluid.getBlock() instanceof BlockFluidMaterial)) return;

        try {
            String tinkerBaseName = "liquid_" + name;
            File stillFile = new File(CONFIG_FLUID_DIR, tinkerBaseName + ".png");
            File flowFile = new File(CONFIG_FLUID_DIR, tinkerBaseName + "_flow.png");

            if (!stillFile.exists()) {
                BufferedImage still = tint(baseStill, color);
                ImageIO.write(still, "png", stillFile);
                writeMcmetaFile(stillFile, still.getHeight() / 16, true, 2);
                Logger.info("[FluidTextureApplier] Created still texture for: " + name);
            }

            if (!flowFile.exists()) {
                BufferedImage flow = tint(baseFlow, color);
                ImageIO.write(flow, "png", flowFile);
                writeMcmetaFile(flowFile, flow.getHeight() / 16, false, 3);
                Logger.info("[FluidTextureApplier] Created flow texture for: " + name);
            }

        } catch (IOException e) {
            Logger.error("[FluidTextureApplier] Failed to generate texture for fluid " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initBaseTextures() {
        if (baseStill != null && baseFlow != null) return;

        try {
            ResourceLocation stillLoc = new ResourceLocation(
                LibResources.PREFIX_MOD + "textures/blocks/liquid_base.png");
            ResourceLocation flowLoc = new ResourceLocation(
                LibResources.PREFIX_MOD + "textures/blocks/liquid_base_flow.png");

            Logger.info("[FluidTextureApplier] Loading liquid_base from: " + stillLoc);
            baseStill = ImageIO.read(
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(stillLoc)
                    .getInputStream());

            Logger.info("[FluidTextureApplier] Loading liquid_base_flow from: " + flowLoc);
            baseFlow = ImageIO.read(
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(flowLoc)
                    .getInputStream());

            if (!CONFIG_FLUID_DIR.exists()) {
                boolean created = CONFIG_FLUID_DIR.mkdirs();
                Logger.info(
                    "[FluidTextureApplier] Created icon folder: " + CONFIG_FLUID_DIR.getAbsolutePath()
                        + " -> "
                        + created);
            } else {
                Logger.info("[FluidTextureApplier] Using existing icon folder: " + CONFIG_FLUID_DIR.getAbsolutePath());
            }

        } catch (IOException e) {
            Logger.error("[FluidTextureApplier] Failed to load base textures: " + e.getMessage());
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
        if (mcmeta.exists()) return;

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
            Logger.info("[FluidTextureApplier] Wrote .mcmeta for: " + pngFile.getName());
        }
    }

    public static void cleanUnusedTextures() {
        if (!CONFIG_FLUID_DIR.exists()) return;

        Set<String> validNames = new HashSet<>();
        for (Map.Entry<MaterialEntry, Fluid> entry : FluidMaterialRegistry.FLUIDS.entrySet()) {
            String name = entry.getValue()
                .getName()
                .replace(".molten", "");
            String base = "liquid_" + name;

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
                Logger.info("[FluidTextureApplier] Removed unused texture: " + file.getName() + " -> " + deleted);
            }
        }
    }
}
