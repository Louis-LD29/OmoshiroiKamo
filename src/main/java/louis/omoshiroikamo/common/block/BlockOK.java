package louis.omoshiroikamo.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.enderio.core.common.BlockEnder;
import com.enderio.core.common.TileEntityEnder;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockOK extends BlockEnder {

    protected final ModObject modObject;
    protected String textureName;

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass) {
        super(modObject.unlocalisedName, teClass);
        this.modObject = modObject;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, Material mat) {
        super(modObject.unlocalisedName, teClass, mat);
        this.modObject = modObject;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, Material mat, String textureName) {
        super(modObject.unlocalisedName, teClass, mat);
        this.modObject = modObject;
        this.textureName = textureName;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, String textureName) {
        super(modObject.unlocalisedName, teClass);
        this.modObject = modObject;
        this.textureName = textureName;
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public static BlockOK create(ModObject modObject, String textureName, Material mat) {
        BlockOK blockOK = new BlockOK(modObject, null, mat, textureName);
        blockOK.init();
        return blockOK;
    }

    public static BlockOK create(ModObject modObject, String textureName) {
        BlockOK blockOK = new BlockOK(modObject, null, textureName);
        blockOK.init();
        return blockOK;
    }

    public static BlockOK create(ModObject modObject) {
        BlockOK blockOK = new BlockOK(modObject, null);
        blockOK.init();
        return blockOK;
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        if (textureName != null) {
            blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + textureName);
        } else {
            blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + name);
        }
    }
}
