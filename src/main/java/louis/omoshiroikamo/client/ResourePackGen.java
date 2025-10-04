package louis.omoshiroikamo.client;

import java.io.File;
import java.util.Locale;

import com.enderio.core.common.util.ResourcePackAssembler;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import louis.omoshiroikamo.client.fluid.FluidMaterialTexture;
import louis.omoshiroikamo.client.fluid.FluidTexture;
import louis.omoshiroikamo.client.ore.OreTexture;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ResourePackGen {

    public static File configDirectory;

    public static void applyAllTexture(FMLPreInitializationEvent event) {
        configDirectory = new File(event.getModConfigurationDirectory(), LibMisc.MOD_ID);
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }
        FluidTexture.applyAll(configDirectory);
        FluidMaterialTexture.applyAll(configDirectory);
        OreTexture.applyAll(configDirectory);
    }

    public static void assembleResourcePack(FMLPreInitializationEvent event) {

        configDirectory = new File(event.getModConfigurationDirectory(), LibMisc.MOD_ID);
        if (!configDirectory.exists()) {
            configDirectory.mkdir();
        }

        ResourcePackAssembler assembler = new ResourcePackAssembler(
            new File(configDirectory, LibMisc.MOD_NAME + " Resourcepack"),
            LibMisc.MOD_NAME + " Resourcepack",
            LibMisc.MOD_ID);

        addIcons(assembler);
        addLangs(assembler);

        assembler.assemble()
            .inject();
    }

    private static void addIcons(ResourcePackAssembler assembler) {
        File iconDir = new File(configDirectory, "icons");
        File materialFluidDir = new File(configDirectory, LibResources.PREFIX_MATERIAL_FLUID_ICONS);
        File fluidDir = new File(configDirectory, LibResources.PREFIX_FLUID_ICONS);
        File oreDir = new File(configDirectory, LibResources.PREFIX_ORE_ICONS);
        if (!iconDir.exists()) {
            iconDir.mkdirs();
        }

        File[] iconFiles = iconDir.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] materialFluidFiles = materialFluidDir
            .listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] fluidFiles = fluidDir.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".mcmeta"));
        File[] oreFiles = oreDir.listFiles((dir, name) -> name.endsWith(".png"));

        if (iconFiles != null) {
            for (File f : iconFiles) {
                assembler.addIcon(f);
            }
        }
        if (materialFluidFiles != null) {
            for (File f : materialFluidFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }

        if (fluidFiles != null) {
            for (File f : fluidFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }
        if (oreFiles != null) {
            for (File f : oreFiles) {
                assembler.addCustomFile("assets/" + LibMisc.MOD_ID.toLowerCase(Locale.ROOT) + "/textures/blocks", f);
            }
        }
    }

    private static void addLangs(ResourcePackAssembler assembler) {
        File langDir = new File(configDirectory, "lang");
        if (!langDir.exists()) {
            langDir.mkdirs();
        }

        File[] langFiles = langDir.listFiles((dir, name) -> name.endsWith(".lang"));
        if (langFiles != null) {
            for (File f : langFiles) {
                assembler.addLang(f);
            }
        }
    }
}
