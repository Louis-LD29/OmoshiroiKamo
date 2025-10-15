package louis.omoshiroikamo.common.recipes.voidMiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.enderio.core.common.util.DyeColor;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import cpw.mods.fml.common.registry.GameData;
import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.api.item.WeightedItemStack;
import louis.omoshiroikamo.api.item.WeightedOreStack;
import louis.omoshiroikamo.api.item.WeightedStackBase;

public class FocusableHandler {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .create();

    public static void saveRegistryDefaultsToJson(File file, FocusableList defaults) {
        try {
            file.getParentFile()
                .mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(GSON.toJson(defaults));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FocusableList loadRegistryFromJson(File file, IFocusableRegistry registry) {
        FocusableList loaded = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            if (reader.ready()) {
                loaded = GSON.fromJson(reader, FocusableList.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (loaded != null) {
            loadIntoRegistry(loaded, registry);
        }
        return loaded;
    }

    public static void loadIntoRegistry(FocusableList entries, IFocusableRegistry registry) {
        if (entries == null || registry == null) {
            return;
        }

        for (FocusableEntry entry : entries.getEntries()) {
            WeightedStackBase ws = entry.getRegistryEntry();
            if (ws == null) {
                continue;
            }

            ItemStack mainStack = ws.getMainStack();
            if (mainStack != null && ws.getWeight() > 0) {
                DyeColor preferred = entry.getFocusColor();
                if (preferred != null) {
                    registry.addResource(ws, preferred);
                }
            }
        }
    }

    public static class FocusableList {

        private final ArrayList<FocusableItem> items = new ArrayList<>();
        private final ArrayList<FocusableBlock> blocks = new ArrayList<>();
        private final ArrayList<FocusableOre> oreDict = new ArrayList<>();

        public ArrayList<FocusableEntry> getEntries() {
            ArrayList<FocusableEntry> list = new ArrayList<>();
            list.addAll(items);
            list.addAll(blocks);
            list.addAll(oreDict);
            return list;
        }

        public void addEntry(FocusableItem itm) {
            items.add(itm);
        }

        public void addEntry(FocusableBlock blk) {
            blocks.add(blk);
        }

        public void addEntry(FocusableOre od) {
            oreDict.add(od);
        }

        public boolean hasEntry(FocusableItem itm) {
            if (itm == null) {
                return false;
            }
            return items.stream()
                .anyMatch(
                    fi -> fi != null && fi.getIDWithMeta()
                        .equalsIgnoreCase(itm.getIDWithMeta()));
        }

        public boolean hasEntry(FocusableBlock blk) {
            if (blk == null) {
                return false;
            }
            return blocks.stream()
                .anyMatch(
                    fi -> fi != null && fi.getIDWithMeta()
                        .equalsIgnoreCase(blk.getIDWithMeta()));
        }

        public boolean hasEntry(FocusableOre od) {
            if (od == null) {
                return false;
            }
            return oreDict.stream()
                .anyMatch(
                    fi -> fi != null && fi.getOreName()
                        .equalsIgnoreCase(od.getOreName()));
        }
    }

    // ------------------------------
    // Enums
    // ------------------------------

    public enum EnumFocusColor {

        @SerializedName("white")
        WHITE(DyeColor.WHITE),
        @SerializedName("orange")
        ORANGE(DyeColor.ORANGE),
        @SerializedName("magenta")
        MAGENTA(DyeColor.MAGENTA),
        @SerializedName("light_blue")
        LIGHT_BLUE(DyeColor.LIGHT_BLUE),
        @SerializedName("yellow")
        YELLOW(DyeColor.YELLOW),
        @SerializedName("lime")
        LIME(DyeColor.LIME),
        @SerializedName("pink")
        PINK(DyeColor.PINK),
        @SerializedName("gray")
        GRAY(DyeColor.GRAY),
        @SerializedName("light_gray")
        SILVER(DyeColor.SILVER),
        @SerializedName("cyan")
        CYAN(DyeColor.CYAN),
        @SerializedName("purple")
        PURPLE(DyeColor.PURPLE),
        @SerializedName("blue")
        BLUE(DyeColor.BLUE),
        @SerializedName("brown")
        BROWN(DyeColor.BROWN),
        @SerializedName("green")
        GREEN(DyeColor.GREEN),
        @SerializedName("red")
        RED(DyeColor.RED),
        @SerializedName("black")
        BLACK(DyeColor.BLACK);

        private final DyeColor dyeColor;

        EnumFocusColor(DyeColor color) {
            this.dyeColor = color;
        }

        public DyeColor getColor() {
            return dyeColor;
        }

        public static EnumFocusColor getFromDye(DyeColor dye) {
            for (EnumFocusColor focus : values()) {
                if (focus.dyeColor == dye) {
                    return focus;
                }
            }
            return WHITE;
        }
    }

    public enum FocusableType {
        @SerializedName("ore")
        ORE,
        @SerializedName("item")
        ITEM,
        @SerializedName("block")
        BLOCK
    }

    public abstract static class FocusableEntry {

        public abstract WeightedStackBase getRegistryEntry();

        public abstract DyeColor getFocusColor();
    }

    public static class FocusableOre extends FocusableEntry {

        public String oreName;
        public EnumFocusColor focusColor;
        public int weight;

        public FocusableOre() {}

        public FocusableOre(String ore, DyeColor focusColor, int weight) {
            this.oreName = ore;
            this.focusColor = EnumFocusColor.getFromDye(focusColor);
            this.weight = weight;
        }

        @Override
        public WeightedStackBase getRegistryEntry() {
            return (!Strings.isNullOrEmpty(oreName) && weight > 0) ? new WeightedOreStack(oreName, weight) : null;
        }

        @Override
        public DyeColor getFocusColor() {
            return focusColor.getColor();
        }

        public String getOreName() {
            return oreName;
        }
    }

    public static class FocusableItem extends FocusableEntry {

        public String id;
        public int meta;
        public EnumFocusColor focusColor;
        public int weight;
        public boolean isOreDict = false;

        public FocusableItem() {}

        public FocusableItem(String id, int meta, DyeColor focusColor, int weight) {
            this.id = id;
            this.meta = meta;
            this.focusColor = EnumFocusColor.getFromDye(focusColor);
            this.weight = weight;
            this.isOreDict = false;
        }

        public FocusableItem(String oreName, DyeColor focusColor, int weight) {
            this.id = oreName;
            this.meta = 0;
            this.focusColor = EnumFocusColor.getFromDye(focusColor);
            this.weight = weight;
            this.isOreDict = true;
        }

        @Override
        public WeightedStackBase getRegistryEntry() {
            if (Strings.isNullOrEmpty(id) || meta < 0 || weight <= 0) {
                return null;
            }

            if (isOreDict) {
                List<ItemStack> ores = OreDictionary.getOres(id);
                if (ores == null || ores.isEmpty()) {
                    return null;
                }
                return new WeightedItemStack(
                    ores.get(0)
                        .copy(),
                    weight);
            }

            Item item = GameData.getItemRegistry()
                .getObject(id);
            if (item == null) {
                return null;
            }
            return new WeightedItemStack(new ItemStack(item, 1, meta), weight);
        }

        @Override
        public DyeColor getFocusColor() {
            return focusColor.getColor();
        }

        public String getIDWithMeta() {
            return id + ":" + meta;
        }
    }

    public static class FocusableBlock extends FocusableEntry {

        public String id;
        public int meta;
        public EnumFocusColor focusColor;
        public int weight;
        public boolean isOreDict = false;

        public FocusableBlock() {}

        public FocusableBlock(String id, int meta, DyeColor focusColor, int weight) {
            this.id = id;
            this.meta = meta;
            this.focusColor = EnumFocusColor.getFromDye(focusColor);
            this.weight = weight;
            this.isOreDict = false;
        }

        public FocusableBlock(String oreName, DyeColor focusColor, int weight) {
            this.id = oreName;
            this.meta = 0;
            this.focusColor = EnumFocusColor.getFromDye(focusColor);
            this.weight = weight;
            this.isOreDict = true;
        }

        @Override
        public WeightedStackBase getRegistryEntry() {
            if (Strings.isNullOrEmpty(id) || meta < 0 || weight <= 0) {
                return null;
            }
            if (isOreDict) {
                List<ItemStack> ores = OreDictionary.getOres(id);
                if (ores == null || ores.isEmpty()) {
                    return null;
                }
                return new WeightedItemStack(
                    ores.get(0)
                        .copy(),
                    weight);
            }

            Block block = GameData.getBlockRegistry()
                .getObject(id);
            if (block == null) {
                return null;
            }
            return new WeightedItemStack(new ItemStack(block, 1, meta), weight);
        }

        @Override
        public DyeColor getFocusColor() {
            return focusColor.getColor();
        }

        public String getIDWithMeta() {
            return id + ":" + meta;
        }
    }
}
