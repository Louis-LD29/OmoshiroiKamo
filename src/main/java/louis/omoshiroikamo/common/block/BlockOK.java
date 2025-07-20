package louis.omoshiroikamo.common.block;

import net.minecraft.block.material.Material;

import com.enderio.core.common.BlockEnder;

import louis.omoshiroikamo.common.OKCreativeTab;

public class BlockOK extends BlockEnder {

    protected BlockOK(String name, Class<? extends TileEntityEio> teClass) {
        super(name, teClass);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockOK(String name, Class<? extends TileEntityEio> teClass, Material mat) {
        super(name, teClass, mat);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

}
