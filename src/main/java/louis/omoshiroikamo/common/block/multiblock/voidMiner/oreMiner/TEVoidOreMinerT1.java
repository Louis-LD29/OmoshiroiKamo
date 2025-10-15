package louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_DEFINITION_TIER_1;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import louis.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidOreMinerT1 extends TEVoidMiner {

    public TEVoidOreMinerT1() {}

    @Override
    protected IStructureDefinition<TEVoidOreMinerT1> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_1;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    @Override
    public IFocusableRegistry getRegistry() {
        return VoidMinerRecipes.voidOreMinerRegistry;
    }

    @Override
    protected String getStructurePieceName() {
        return "tier1";
    }

    @Override
    public int getTier() {
        return 1;
    }

    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostOreTier1;
    }

    public int getBaseDuration() {
        return VoidMinerConfig.tickOreTier1;
    }

    public int getMinDuration() {
        return VoidMinerConfig.tickOreTier1;
    }

    public int getMaxDuration() {
        return VoidMinerConfig.tickOreTier1;
    }
}
