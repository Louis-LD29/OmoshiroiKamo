package ruiseki.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.multiblock.IModifierAttribute;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierNull extends BlockModifier {

    protected BlockModifierNull() {
        super(ModObject.blockModifierNull, "null");
    }

    public static BlockModifierNull create() {
        return new BlockModifierNull();
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.NULL);
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_core");
    }
}
