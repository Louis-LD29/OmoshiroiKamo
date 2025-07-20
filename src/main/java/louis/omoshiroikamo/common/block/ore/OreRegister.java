package louis.omoshiroikamo.common.block.ore;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.ore.OreEntry;
import louis.omoshiroikamo.api.ore.OreRegistry;

public class OreRegister {

    public static final Map<OreEntry, Block> BLOCKS = new HashMap<>();

    public static void init() {

        for (OreEntry entry : OreRegistry.all()) {
            String name = "ore_" + entry.getUnlocalizedName();
            BlockOre oreBlock = new BlockOreOK(entry);
            GameRegistry.registerBlock(oreBlock, name);
            BLOCKS.put(entry, oreBlock);
        }

    }

    public static Block getBlock(OreEntry entry) {
        return BLOCKS.get(entry);
    }
}
