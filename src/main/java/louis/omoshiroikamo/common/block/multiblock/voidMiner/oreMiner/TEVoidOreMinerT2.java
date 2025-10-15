package louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_DEFINITION_TIER_2;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import louis.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidOreMinerT2 extends TEVoidMiner {

    public TEVoidOreMinerT2() {}

    @Override
    protected IStructureDefinition<TEVoidOreMinerT2> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_2;
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
        return "tier2";
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostOreTier2;
    }

    @Override
    public int getBaseDuration() {
        return VoidMinerConfig.tickOreTier2;
    }

    @Override
    public int getMinDuration() {
        return VoidMinerConfig.tickOreTier2;
    }

    @Override
    public int getMaxDuration() {
        return VoidMinerConfig.tickOreTier2;
    }
}
