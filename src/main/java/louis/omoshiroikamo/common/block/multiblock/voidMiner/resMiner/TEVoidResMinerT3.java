package louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner;

import static louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.STRUCTURE_DEFINITION_TIER_3;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import louis.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidResMinerT3 extends TEVoidMiner {

    public TEVoidResMinerT3() {}

    @Override
    protected IStructureDefinition<TEVoidResMinerT3> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_3;
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
        return "tier3";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostResTier3;
    }

    @Override
    public int getBaseDuration() {
        return VoidMinerConfig.tickResTier3;
    }

    @Override
    public int getMinDuration() {
        return VoidMinerConfig.tickResTier3;
    }

    @Override
    public int getMaxDuration() {
        return VoidMinerConfig.tickResTier3;
    }
}
