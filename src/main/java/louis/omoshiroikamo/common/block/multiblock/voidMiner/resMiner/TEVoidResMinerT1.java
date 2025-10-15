package louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner;

import static louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.STRUCTURE_DEFINITION_TIER_1;
import static louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.item.IFocusableRegistry;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.recipes.voidMiner.VoidMinerRecipes;
import louis.omoshiroikamo.config.block.VoidMinerConfig;

public class TEVoidResMinerT1 extends TEVoidMiner {

    public TEVoidResMinerT1() {}

    @Override
    protected IStructureDefinition<TEVoidResMinerT1> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_1;
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
        return "tier1";
    }

    @Override
    public int getTier() {
        return 1;
    }

    public int getEnergyCostPerDuration() {
        return VoidMinerConfig.energyCostResTier1;
    }

    public int getBaseDuration() {
        return VoidMinerConfig.tickResTier1;
    }

    public int getMinDuration() {
        return VoidMinerConfig.tickResTier1;
    }

    public int getMaxDuration() {
        return VoidMinerConfig.tickResTier1;
    }
}
