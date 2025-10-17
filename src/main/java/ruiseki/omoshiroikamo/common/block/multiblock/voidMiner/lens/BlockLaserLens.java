package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.lens;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.enderio.core.common.util.DyeColor;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.block.BlockOK;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class BlockLaserLens extends BlockOK {

    public static String[] blocks = new String[] { "clear", "black", "red", "green", "brown", "blue", "purple", "cyan",
        "light_gray", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white" };

    protected BlockLaserLens() {
        super(ModObject.blockLaserLens, TELaserLens.class, Material.glass);
    }

    public static BlockLaserLens create() {
        BlockLaserLens res = new BlockLaserLens();
        res.init();
        return res;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockLaserLens.class, name);
        GameRegistry.registerTileEntity(teClass, name + "TileEntity");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TELaserLens(meta);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        blockIcon = iIconRegister.registerIcon(LibResources.PREFIX_MOD + "lens_colored");
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public DyeColor getFocusColor(int meta) {
        if (meta <= 0) {
            return DyeColor.WHITE;
        }
        meta = Math.min(meta - 1, DyeColor.values().length - 1);
        return DyeColor.values()[meta];
    }
}
