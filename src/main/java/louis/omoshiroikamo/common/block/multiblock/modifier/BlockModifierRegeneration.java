package louis.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierRegeneration extends BlockModifier {

    protected BlockModifierRegeneration() {
        super(ModObject.blockModifierRegeneration, "regen");
    }

    public static BlockModifierRegeneration create() {
        BlockModifierRegeneration block = new BlockModifierRegeneration();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.P_REGEN);
        list.add(new AttributeEnergyCostFixed(128));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_regen");
    }
}
