package louis.omoshiroikamo.common.block;

import net.minecraft.block.material.Material;

import com.enderio.core.common.BlockEnder;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.OKCreativeTab;

public class BlockOK extends BlockEnder {

    protected final ModObject modObject;

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEio> teClass) {
        super(modObject.unlocalisedName, teClass);
        this.modObject = modObject;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEio> teClass, Material mat) {
        super(modObject.unlocalisedName, teClass, mat);
        this.modObject = modObject;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

}
