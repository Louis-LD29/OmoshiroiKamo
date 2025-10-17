package ruiseki.omoshiroikamo.common.block.multiblock.modifier;

import ruiseki.omoshiroikamo.api.multiblock.AttributeTotalLevel;
import ruiseki.omoshiroikamo.api.multiblock.IModifierAttribute;
import ruiseki.omoshiroikamo.api.multiblock.OKModifierAttributes;

public class ModifierAttributes {

    public static IModifierAttribute SPEED;
    public static IModifierAttribute ACCURACY;
    public static IModifierAttribute PIEZO;
    public static IModifierAttribute E_FLIGHT_CREATIVE;
    public static IModifierAttribute P_NIGHT_VISION;
    public static IModifierAttribute P_SPEED;
    public static IModifierAttribute P_HASTE;
    public static IModifierAttribute P_STRENGTH;
    public static IModifierAttribute P_WATER_BREATHING;
    public static IModifierAttribute P_REGEN;
    public static IModifierAttribute P_SATURATION;
    public static IModifierAttribute P_RESISTANCE;
    public static IModifierAttribute P_JUMP_BOOST;
    public static IModifierAttribute P_FIRE_RESISTANCE;
    public static IModifierAttribute NULL;

    public static void init() {
        OKModifierAttributes.SPEED = SPEED = new AttributeSpeed();
        OKModifierAttributes.ACCURACY = ACCURACY = new AttributeAccuracy();
        OKModifierAttributes.PIEZO = PIEZO = new AttributeTotalLevel("piezo");
        OKModifierAttributes.NULL = NULL = new AttributeTotalLevel("null");
        OKModifierAttributes.E_FLIGHT_CREATIVE = E_FLIGHT_CREATIVE = new AttributeTotalLevel("e_flight_creative");
        OKModifierAttributes.P_NIGHT_VISION = P_NIGHT_VISION = new AttributeTotalLevel("p_night_vision");
        OKModifierAttributes.P_SPEED = P_SPEED = new AttributeTotalLevel("p_speed");
        OKModifierAttributes.P_HASTE = P_HASTE = new AttributeTotalLevel("p_haste");
        OKModifierAttributes.P_STRENGTH = P_STRENGTH = new AttributeTotalLevel("p_strength");
        OKModifierAttributes.P_WATER_BREATHING = P_WATER_BREATHING = new AttributeTotalLevel("p_water_breathing");
        OKModifierAttributes.P_REGEN = P_REGEN = new AttributeTotalLevel("p_regen");
        OKModifierAttributes.P_SATURATION = P_SATURATION = new AttributeTotalLevel("p_saturation");
        OKModifierAttributes.P_RESISTANCE = P_RESISTANCE = new AttributeTotalLevel("p_resistance");
        OKModifierAttributes.P_JUMP_BOOST = P_JUMP_BOOST = new AttributeTotalLevel("p_jump_boost");
        OKModifierAttributes.P_FIRE_RESISTANCE = P_FIRE_RESISTANCE = new AttributeTotalLevel("p_fire_resistance");
    }
}
