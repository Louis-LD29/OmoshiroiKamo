package louis.omoshiroikamo.common.block;

import net.minecraft.block.material.Material;

import com.enderio.core.common.BlockEnder;

import louis.omoshiroikamo.common.OKCreativeTab;

public class BlockEio extends BlockEnder {

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass) {
        super(name, teClass);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass, Material mat) {
        super(name, teClass, mat);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

}
