package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_DEFINITION_TIER_1;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.STRUCTURE_TIER_1;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import ruiseki.omoshiroikamo.api.item.IFocusableRegistry;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import ruiseki.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import ruiseki.omoshiroikamo.config.block.VoidMinerConfig;

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
        return STRUCTURE_TIER_1;
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
