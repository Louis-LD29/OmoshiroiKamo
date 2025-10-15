package louis.omoshiroikamo.api.multiblock;

import java.util.ArrayList;
import java.util.List;

public class ModifierRegistry implements IModifierRegistry {

    private List<IModifierBlock> registry = new ArrayList<>();
    private static ModifierRegistry instance;

    public static ModifierRegistry getInstance() {
        if (instance == null) {
            instance = new ModifierRegistry();
        }

        return instance;
    }

    private ModifierRegistry() {}

    public boolean registerModifier(IModifierBlock modifier) {
        if (!this.modifierExists(modifier)) {
            this.registry.add(modifier);
            return true;
        } else {
            return false;
        }
    }

    public boolean modifierExists(IModifierBlock modifier) {
        return this.getModifier(modifier.getModifierName()) != null;
    }

    public IModifierBlock getModifier(String modName) {
        for (IModifierBlock iModifierBlock : this.registry) {
            if (iModifierBlock.getModifierName()
                .equalsIgnoreCase(modName)) {
                return iModifierBlock;
            }
        }

        return null;
    }
}
