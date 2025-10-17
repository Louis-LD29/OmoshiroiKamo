package ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon;

import static ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.STRUCTURE_DEFINITION_TIER_1;
import static ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.STRUCTURE_TIER_1;
import static ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import ruiseki.omoshiroikamo.common.block.multiblock.modifier.ModifierAttributes;

public class TENanoBotBeaconT1 extends TENanoBotBeacon {

    public TENanoBotBeaconT1() {
        super(100000);
    }

    @Override
    protected int maxPotionLevel(String attribute) {
        if (attribute == ModifierAttributes.P_SATURATION.getAttributeName()) {
            return 0;
        }
        if (attribute == ModifierAttributes.P_SPEED.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_HASTE.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_STRENGTH.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_REGEN.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_RESISTANCE.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_FIRE_RESISTANCE.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_WATER_BREATHING.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_NIGHT_VISION.getAttributeName()) {
            return 1;
        }
        if (attribute == ModifierAttributes.P_JUMP_BOOST.getAttributeName()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected IStructureDefinition<TENanoBotBeaconT1> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_1;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    @Override
    protected String getStructurePieceName() {
        return STRUCTURE_TIER_1;
    }

    @Override
    public int getTier() {
        return 1;
    }
}
