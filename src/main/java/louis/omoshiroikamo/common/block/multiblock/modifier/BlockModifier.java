package louis.omoshiroikamo.common.block.multiblock.modifier;

import java.util.ArrayList;
import java.util.List;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.api.multiblock.IModifierBlock;
import louis.omoshiroikamo.api.multiblock.ModifierRegistry;
import louis.omoshiroikamo.common.block.BlockOK;

public abstract class BlockModifier extends BlockOK implements IModifierBlock {

    private final String modifierName;
    private final List<IModifierAttribute> attributes;

    protected BlockModifier(ModObject modObject, String modifierName) {
        super(modObject, null);
        this.modifierName = modifierName;
        ModifierRegistry.getInstance()
            .registerModifier(this);
        this.attributes = new ArrayList<>();
        this.addAttributes(this.attributes);
    }

    public abstract void addAttributes(List<IModifierAttribute> var1);

    @Override
    public String getModifierName() {
        return this.modifierName;
    }

    @Override
    public List<IModifierAttribute> getAttributes() {
        return this.attributes;
    }
}
