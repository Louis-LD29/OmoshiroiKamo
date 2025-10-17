package ruiseki.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import ruiseki.omoshiroikamo.api.multiblock.IModifierAttribute;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierFlight extends BlockModifier {

    protected BlockModifierFlight() {
        super(ModObject.blockModifierFlight, "flight");
    }

    public static BlockModifierFlight create() {
        BlockModifierFlight block = new BlockModifierFlight();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(ModifierAttributes.E_FLIGHT_CREATIVE);
        list.add(new AttributeEnergyCostFixed(128));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_flight");
    }
}
