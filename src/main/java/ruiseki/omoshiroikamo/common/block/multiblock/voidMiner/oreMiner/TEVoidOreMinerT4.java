package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_DEFINITION_TIER_4;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_TIER_4;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import ruiseki.omoshiroikamo.api.item.IFocusableRegistry;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import ruiseki.omoshiroikamo.common.recipe.voidMiner.VoidMinerRecipes;
import ruiseki.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidOreMinerT4 extends TEVoidMiner {

    public TEVoidOreMinerT4() {}

    @Override
    protected IStructureDefinition<TEVoidOreMinerT4> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_4;
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
        return STRUCTURE_TIER_4;
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostOreTier4;
    }

    @Override
    public int getBaseDuration() {
        return VoidMinerConfig.tickOreTier4;
    }

    @Override
    public int getMinDuration() {
        return VoidMinerConfig.tickOreTier4;
    }

    @Override
    public int getMaxDuration() {
        return VoidMinerConfig.tickOreTier4;
    }
}
