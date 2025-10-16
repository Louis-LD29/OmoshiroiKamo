package louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon;

import static louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.STRUCTURE_DEFINITION_TIER_4;
import static louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.STRUCTURE_TIER_4;
import static louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure.TIER_OFFSET;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.common.block.multiblock.modifier.ModifierAttributes;

public class TENanoBotBeaconT4 extends TENanoBotBeacon {

    public TENanoBotBeaconT4() {
        super(400000);
    }

    @Override
    protected int maxPotionLevel(String attribute) {
        if (attribute == ModifierAttributes.P_SATURATION.getAttributeName()) {
            return 2;
        }
        if (attribute == ModifierAttributes.P_SPEED.getAttributeName()) {
            return 4;
        }
        if (attribute == ModifierAttributes.P_HASTE.getAttributeName()) {
            return 4;
        }
        if (attribute == ModifierAttributes.P_STRENGTH.getAttributeName()) {
            return 3;
        }
        if (attribute == ModifierAttributes.P_REGEN.getAttributeName()) {
            return 2;
        }
        if (attribute == ModifierAttributes.P_RESISTANCE.getAttributeName()) {
            return 3;
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
            return 4;
        }
        return 0;
    }

    @Override
    protected IStructureDefinition<TENanoBotBeaconT4> getStructureDefinition() {
        return STRUCTURE_DEFINITION_TIER_4;
    }

    @Override
    public int[][] getOffSet() {
        return TIER_OFFSET;
    }

    @Override
    protected String getStructurePieceName() {
        return STRUCTURE_TIER_4;
    }

    @Override
    public int getTier() {
        return 4;
    }
}
