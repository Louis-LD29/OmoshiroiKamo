package ruiseki.omoshiroikamo.common.ore;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

import ruiseki.omoshiroikamo.api.ore.OreEntry;
import ruiseki.omoshiroikamo.api.ore.OreRegistry;

public class OreRegister {

    public static final Map<OreEntry, Block> BLOCKS = new HashMap<>();

    public static void init() {
        for (OreEntry entry : OreRegistry.all()) {
            BlockOreOK oreBlock = BlockOreOK.create(entry);
            BLOCKS.put(entry, oreBlock);
            String name = oreBlock.getUnlocalizedName();
        }
    }

    public static Block getBlock(OreEntry entry) {
        return BLOCKS.get(entry);
    }
}
