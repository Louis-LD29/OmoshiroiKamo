package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure.STRUCTURE_DEFINITION_TIER_2;
import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.config.block.SolarArrayConfig;

public class TESolarArrayT2 extends TESolarArray {

    public TESolarArrayT2() {
        super(getEnergyGen());
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
    protected IStructureDefinition<TESolarArrayT2> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_2;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    public static int getEnergyGen() {
        return SolarArrayConfig.peakEnergyTier2;
    }

    @Override
    public int getEnergyPerTick() {
        return getEnergyGen();
    }
}
