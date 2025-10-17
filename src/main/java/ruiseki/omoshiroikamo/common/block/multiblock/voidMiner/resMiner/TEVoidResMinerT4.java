package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner;

import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.STRUCTURE_DEFINITION_TIER_4;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.STRUCTURE_TIER_4;
import static ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import ruiseki.omoshiroikamo.api.item.IFocusableRegistry;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import ruiseki.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import ruiseki.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidResMinerT4 extends TEVoidMiner {

    public TEVoidResMinerT4() {}

    @Override
    protected IStructureDefinition<TEVoidResMinerT4> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_4;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    @Override
    public IFocusableRegistry getRegistry() {
        return VoidMinerRecipes.voidResMinerRegistry;
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
        return VoidMinerConfig.energyCostResTier4;
    }

    @Override
    public int getBaseDuration() {
        return VoidMinerConfig.tickResTier4;
    }

    @Override
    public int getMinDuration() {
        return VoidMinerConfig.tickResTier4;
    }

    @Override
    public int getMaxDuration() {
        return VoidMinerConfig.tickResTier4;
    }
}
