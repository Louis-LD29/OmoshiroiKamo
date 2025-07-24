package louis.omoshiroikamo.common.ore;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

import louis.omoshiroikamo.api.ore.OreEntry;
import louis.omoshiroikamo.api.ore.OreRegistry;

public class OreRegister {

    public static final Map<OreEntry, Block> BLOCKS = new HashMap<>();

    public static void init() {
        for (OreEntry entry : OreRegistry.all()) {
            BlockOreOK oreBlock = BlockOreOK.create(entry);
            BLOCKS.put(entry, oreBlock);
        }
    }

    public static Block getBlock(OreEntry entry) {
        return BLOCKS.get(entry);
    }
}
