package louis.omoshiroikamo.common.block.multiblock.modifier;

import louis.omoshiroikamo.api.multiblock.AttributeTotalLevel;
import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.api.multiblock.OKModifierAttributes;

public class ModifierAttributes {

    public static IModifierAttribute SPEED;
    public static IModifierAttribute ACCURACY;
    public static IModifierAttribute PIEZO;
    public static IModifierAttribute P_SPEED;
    public static IModifierAttribute NULL;

    public static void init() {
        OKModifierAttributes.SPEED = SPEED = new AttributeSpeed();
        OKModifierAttributes.ACCURACY = ACCURACY = new AttributeAccuracy();
        OKModifierAttributes.PIEZO = PIEZO = new AttributeTotalLevel("piezo");
        OKModifierAttributes.NULL = NULL = new AttributeTotalLevel("null");
        OKModifierAttributes.P_SPEED = P_SPEED = new AttributeTotalLevel("p_speed");
    }
}
