package louis.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import louis.omoshiroikamo.api.multiblock.IModifierAttribute;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierWaterBreathing extends BlockModifier {

    protected BlockModifierWaterBreathing() {
        super(ModObject.blockModifierWaterBreathing, "water_breathing");
    }

    public static BlockModifierWaterBreathing create() {
        BlockModifierWaterBreathing block = new BlockModifierWaterBreathing();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.P_WATER_BREATHING);
        list.add(new AttributeEnergyCostFixed(128));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_water_breathing");
    }
}
