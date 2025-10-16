package louis.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierResistance extends BlockModifier {

    protected BlockModifierResistance() {
        super(ModObject.blockModifierResistance, "resistance");
    }

    public static BlockModifierResistance create() {
        BlockModifierResistance block = new BlockModifierResistance();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.P_RESISTANCE);
        list.add(new AttributeEnergyCostFixed(128));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_resistance");
    }
}
