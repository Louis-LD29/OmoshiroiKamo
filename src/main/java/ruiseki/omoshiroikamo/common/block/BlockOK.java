package ruiseki.omoshiroikamo.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.enderio.core.common.BlockEnder;
import com.enderio.core.common.TileEntityEnder;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockOK extends BlockEnder {

    protected final ModObject modObject;
    protected String textureName;

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass) {
        super(modObject.unlocalisedName, teClass);
        this.modObject = modObject;
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, Material mat) {
        super(modObject.unlocalisedName, teClass, mat);
        this.modObject = modObject;
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, Material mat, String textureName) {
        super(modObject.unlocalisedName, teClass, mat);
        this.modObject = modObject;
        this.textureName = textureName;
    }

    protected BlockOK(ModObject modObject, Class<? extends TileEntityEnder> teClass, String textureName) {
        super(modObject.unlocalisedName, teClass);
        this.modObject = modObject;
        this.textureName = textureName;
    }

    public static BlockOK create(ModObject modObject, String textureName, Material mat) {
        return new BlockOK(modObject, null, mat, textureName);
    }

    public static BlockOK create(ModObject modObject, String textureName) {
        return new BlockOK(modObject, null, textureName);
    }

    public static BlockOK create(ModObject modObject) {
        return new BlockOK(modObject, null);
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        if (textureName != null) {
            blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + textureName);
        } else {
            blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + name);
        }
    }

    @Override
    public void init() {
        super.init();
    }
}
