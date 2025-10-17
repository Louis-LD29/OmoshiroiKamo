package ruiseki.omoshiroikamo.api.multiblock;

public class AttributeEnergyCost implements IModifierAttribute {

    private float factor;

    public AttributeEnergyCost() {
        this(1.0F);
    }

    public AttributeEnergyCost(float factor) {
        this.factor = factor;
    }

    public String getAttributeName() {
        return "energycost";
    }

    public float getModificationFactor() {
        return this.factor;
    }

    public float getMultiplier(float totalModificationFactor) {
        return (float) Math.pow(1.12, (double) totalModificationFactor);
    }
}
