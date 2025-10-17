package ruiseki.omoshiroikamo.api.multiblock;

public class AttributeBoolean implements IModifierAttribute {

    private final String attributeName;

    public AttributeBoolean(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public float getMultiplier(float totalModificationFactor) {
        return 1.0F;
    }
}
