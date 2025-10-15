package louis.omoshiroikamo.api.multiblock;

public class AttributeEnergyCostFixed implements IModifierAttribute {

    private int energyCost;

    public AttributeEnergyCostFixed(int energyCost) {
        this.energyCost = energyCost;
    }

    public String getAttributeName() {
        return "energycost_fixed";
    }

    public float getModificationFactor() {
        return (float) this.energyCost;
    }

    public float getMultiplier(float totalModificationFactor) {
        return totalModificationFactor;
    }
}
