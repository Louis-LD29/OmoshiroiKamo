package ruiseki.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import ruiseki.omoshiroikamo.api.multiblock.IModifierAttribute;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierNightVision extends BlockModifier {

    protected BlockModifierNightVision() {
        super(ModObject.blockModifierNightVision, "night_vision");
    }

    public static BlockModifierNightVision create() {
        BlockModifierNightVision block = new BlockModifierNightVision();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.P_NIGHT_VISION);
        list.add(new AttributeEnergyCostFixed(128));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_night_vision");
    }
}
