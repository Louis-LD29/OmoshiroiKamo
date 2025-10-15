package louis.omoshiroikamo.common.block.multiblock.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.api.multiblock.IModifierBlock;

public class ModifierHandler {

    private List<IModifierBlock> modifiers = new ArrayList<>();
    private HashMap<String, Float> attributeTotals = new HashMap<>();
    private boolean hasCalculated = false;

    public void setModifiers(List<IModifierBlock> modifiers) {
        this.modifiers = modifiers;
    }

    public void calculateAttributeMultipliers() {
        HashMap<String, Float> attributeFactors = new HashMap<>();
        HashMap<String, IModifierAttribute> baseAttributes = new HashMap<>();

        for (IModifierBlock mod : this.modifiers) {
            List<IModifierAttribute> attributes = mod.getAttributes();
            if (attributes != null) {
                for (IModifierAttribute attr : attributes) {
                    boolean hasAttribute = false;

                    for (String atF : attributeFactors.keySet()) {
                        if (atF.equalsIgnoreCase(attr.getAttributeName())) {
                            hasAttribute = true;
                        }
                    }

                    float currFactor = 0.0F;
                    if (hasAttribute) {
                        currFactor = attributeFactors.get(attr.getAttributeName());
                    } else {
                        baseAttributes.put(attr.getAttributeName(), attr);
                    }

                    currFactor += attr.getModificationFactor();
                    attributeFactors.put(attr.getAttributeName(), currFactor);
                }
            }
        }

        for (String fact : attributeFactors.keySet()) {
            IModifierAttribute ma = baseAttributes.get(fact);
            float totalMultiplier = ma.getMultiplier(attributeFactors.get(fact));
            this.attributeTotals.put(fact, totalMultiplier);
        }

        hasCalculated = true;
    }

    public boolean hasAttribute(String attributeName) {
        for (String modName : this.attributeTotals.keySet()) {
            if (attributeName.equalsIgnoreCase(modName)) {
                return true;
            }
        }
        return false;
    }

    public float getAttributeMultiplier(String attributeName) {
        for (String modName : this.attributeTotals.keySet()) {
            if (attributeName.equalsIgnoreCase(modName)) {
                return this.attributeTotals.get(modName);
            }
        }
        return 1.0F;
    }
}
