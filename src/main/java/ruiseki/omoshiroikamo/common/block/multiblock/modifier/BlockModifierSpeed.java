package ruiseki.omoshiroikamo.common.block.multiblock.modifier;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.multiblock.AttributeEnergyCost;
import ruiseki.omoshiroikamo.api.multiblock.AttributeEnergyCostFixed;
import ruiseki.omoshiroikamo.api.multiblock.IModifierAttribute;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockModifierSpeed extends BlockModifier {

    protected BlockModifierSpeed() {
        super(ModObject.blockModifierSpeed, "speed");
    }

    public static BlockModifierSpeed create() {
        BlockModifierSpeed block = new BlockModifierSpeed();
        block.init();
        return block;
    }

    @Override
    public void addAttributes(List<IModifierAttribute> list) {
        list.add(new AttributeEnergyCost(1.0F));
        list.add(ModifierAttributes.SPEED);
        list.add(ModifierAttributes.P_SPEED);
        list.add(new AttributeEnergyCostFixed(16));
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "modifier_speed");
    }
}
