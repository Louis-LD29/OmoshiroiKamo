package louis.omoshiroikamo.client.ore;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import louis.omoshiroikamo.api.ore.OreEntry;
import louis.omoshiroikamo.common.ore.OreRegister;
import louis.omoshiroikamo.common.util.helper.Logger;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class OreTexture {

    private static BufferedImage baseStone, oreMask;
    public static File CONFIG_ORE_DIR;

    public static void applyAll(File configDirectory) {
        CONFIG_ORE_DIR = new File(configDirectory.getAbsolutePath() + "/" + LibResources.PREFIX_ORE_ICONS);
        initBaseTextures();
        for (Map.Entry<OreEntry, Block> entry : OreRegister.BLOCKS.entrySet()) {
            apply(entry.getValue(), entry.getKey());
        }
        cleanUnusedTextures();
    }

    public static void apply(Block ore, OreEntry entry) {
        String name = "ore_" + entry.getUnlocalizedName();
        int color = entry.getColor();

        File outFile = new File(CONFIG_ORE_DIR, name + ".png");
        File colorFile = new File(CONFIG_ORE_DIR, name + ".color.txt");

        String currentColor = String.format("%06X", color)
            .toUpperCase();
        boolean shouldRegen = true;

        try {
            if (outFile.exists() && colorFile.exists()) {
                String savedColor = Files.readAllLines(colorFile.toPath())
                    .get(0);
                if (savedColor.equals(currentColor)) {
                    shouldRegen = false;
                } else {
                    outFile.delete();
                }
            }

            if (shouldRegen) {
                BufferedImage tintedOverlay = tint(oreMask, color);
                BufferedImage result = overlay(baseStone, tintedOverlay);

                ImageIO.write(result, "png", outFile);

                try (FileWriter writer = new FileWriter(colorFile)) {
                    writer.write(currentColor);
                }

                Logger.info("Generated ore texture: " + outFile.getName());
            }

        } catch (IOException e) {
            Logger.error("Failed to generate ore texture for: " + name);
            e.printStackTrace();
        }
    }

    private static void initBaseTextures() {
        if (baseStone != null && oreMask != null) {
            return;
        }

        try {
            baseStone = loadVanillaTexture("textures/blocks/stone.png");
            oreMask = loadModTexture("textures/blocks/base_ore.png");

            if (!CONFIG_ORE_DIR.exists()) {
                CONFIG_ORE_DIR.mkdirs();
            }
        } catch (IOException e) {
            Logger.error("Failed to load base ore textures.");
            throw new RuntimeException("Failed to load ore base/mask", e);
        }
    }

    private static BufferedImage loadVanillaTexture(String path) throws IOException {
        return ImageIO.read(
            Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(new ResourceLocation("minecraft", path))
                .getInputStream());
    }

    private static BufferedImage loadModTexture(String path) throws IOException {
        return ImageIO.read(
            Minecraft.getMinecraft()
                .getResourceManager()
                .getResource(new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), path))
                .getInputStream());
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

                if (alpha > 0) {
                    int r = (int) (((rgba >> 16) & 0xFF) * rTint);
                    int g = (int) (((rgba >> 8) & 0xFF) * gTint);
                    int b = (int) ((rgba & 0xFF) * bTint);
                    result.setRGB(x, y, (alpha << 24) | (r << 16) | (g << 8) | b);
                } else {
                    result.setRGB(x, y, 0);
                }
            }
        }
        return result;
    }

    private static BufferedImage overlay(BufferedImage base, BufferedImage overlay) {
        BufferedImage combined = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < base.getWidth(); x++) {
            for (int y = 0; y < base.getHeight(); y++) {
                int basePixel = base.getRGB(x, y);
                int overlayPixel = overlay.getRGB(x, y);

                int alpha = (overlayPixel >> 24) & 0xFF;
                combined.setRGB(x, y, alpha > 0 ? overlayPixel : basePixel);
            }
        }
        return combined;
    }

    public static void cleanUnusedTextures() {
        if (!CONFIG_ORE_DIR.exists()) {
            return;
        }

        Set<String> validNames = new HashSet<>();
        for (Map.Entry<OreEntry, Block> entry : OreRegister.BLOCKS.entrySet()) {
            String name = "ore_" + entry.getKey()
                .getUnlocalizedName();
            validNames.add(name + ".png");
            validNames.add(name + ".color.txt");
        }

        File[] files = CONFIG_ORE_DIR.listFiles();
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
