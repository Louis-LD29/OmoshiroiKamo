package ruiseki.omoshiroikamo.api.multiblock;

public interface IModifierAttribute {

    String getAttributeName();

    float getMultiplier(float var1);

    default float getModificationFactor() {
        return 1.0F;
    }
}
