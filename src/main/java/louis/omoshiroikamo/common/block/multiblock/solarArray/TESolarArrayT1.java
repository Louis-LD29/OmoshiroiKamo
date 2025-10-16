package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure.STRUCTURE_DEFINITION_TIER_1;
import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure.STRUCTURE_TIER_1;
import static louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.config.block.SolarArrayConfig;

public class TESolarArrayT1 extends TESolarArray {

    public TESolarArrayT1() {
        super(getEnergyGen());
    }

    @Override
    protected String getStructurePieceName() {
        return STRUCTURE_TIER_1;
    }

    @Override
    public int getEnergyPerTick() {
        return getEnergyGen();
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    protected IStructureDefinition<TESolarArrayT1> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_1;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    public static int getEnergyGen() {
        return SolarArrayConfig.peakEnergyTier1;
    }

}
