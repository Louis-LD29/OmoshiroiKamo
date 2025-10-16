package louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_DEFINITION_TIER_3;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_TIER_3;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import louis.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidOreMinerT3 extends TEVoidMiner {

    public TEVoidOreMinerT3() {}

    @Override
    protected IStructureDefinition<TEVoidOreMinerT3> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_3;
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
        return STRUCTURE_TIER_3;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostOreTier3;
    }

    @Override
    public int getBaseDuration() {
        return VoidMinerConfig.tickOreTier3;
    }

    @Override
    public int getMinDuration() {
        return VoidMinerConfig.tickOreTier3;
    }

    @Override
    public int getMaxDuration() {
        return VoidMinerConfig.tickOreTier3;
    }
}
