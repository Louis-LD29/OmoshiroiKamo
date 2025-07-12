package com.louis.test.common.block.meta;

import com.louis.test.api.ModObject;
import com.louis.test.api.mte.MetaTileEntity;
import com.louis.test.common.block.AbstractBlock;
import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.core.lib.LibResources;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockMeta extends AbstractBlock<AbstractTE> {

    protected BlockMeta() {
        super(ModObject.blockMeta, AbstractTE.class);
    }

    public static BlockMeta create() {
        BlockMeta res = new BlockMeta();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockMeta.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(TEMeta.class, modObject.unlocalisedName + "_meta_te");
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TEMeta(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (int meta : MetaTileEntity.getAllBaseMetas()) {
            list.add(new ItemStack(this, 1, meta));
        }
    }

    private IIcon[] icons;

    @Override
    public void registerBlockIcons(IIconRegister iIconRegister) {
        int variantCount = MetaTileEntity.values().length;
        icons = new IIcon[variantCount];

        for (MetaTileEntity meta : MetaTileEntity.values()) {
            String iconName = LibResources.PREFIX_MOD + meta.name()
                .toLowerCase();
            icons[meta.ordinal()] = iIconRegister.registerIcon(iconName);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (icons == null || meta < 0 || meta >= icons.length) return blockIcon;
        return icons[meta];
    }

}
