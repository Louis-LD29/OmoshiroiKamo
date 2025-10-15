package louis.omoshiroikamo.common.block.multiblock.modifier;

import louis.omoshiroikamo.api.multiblock.IModifierAttribute;

public class AttributeSpeed implements IModifierAttribute {

    private float modificationFactor;

    public AttributeSpeed() {
        this(1.0F);
    }

    public AttributeSpeed(float modificationFactor) {
        this.modificationFactor = modificationFactor;
    }

    public String getAttributeName() {
        return "speed";
    }

    public float getMultiplier(float totalModificationFactor) {
        return (float) Math.pow(0.7, (double) totalModificationFactor);
    }

    public float getModificationFactor() {
        return this.modificationFactor;
    }
}
