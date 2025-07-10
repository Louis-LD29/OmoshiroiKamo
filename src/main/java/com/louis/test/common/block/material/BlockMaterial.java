package com.louis.test.common.block.material;

import static org.apache.commons.lang3.StringUtils.capitalize;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.BlockEio;
import com.louis.test.core.lib.LibResources;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMaterial extends BlockEio {

    protected IIcon icon;

    public static BlockMaterial create() {
        BlockMaterial result = new BlockMaterial();
        result.init();
        return result;
    }

    private BlockMaterial() {
        super(ModObject.blockMaterial.unlocalisedName, null, net.minecraft.block.material.Material.iron);
        setHardness(3.0F);
        setResistance(5.0F);
        setStepSound(soundTypeMetal);
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockMaterial.class, ModObject.blockMaterial.unlocalisedName);

        for (Material mat : Material.values()) {
            String matName = mat.name()
                .toLowerCase(Locale.ROOT);
            int meta = mat.ordinal();

            registerMaterialOreDict(matName, meta);
            switch (mat) {
                case CARBON_STEEL:
                    registerMaterialOreDict("Steel", meta);
                    break;
                default:
                    break;
            }
        }
    }

    private void registerMaterialOreDict(String name, int meta) {
        OreDictionary.registerOre("block" + capitalize(name), new ItemStack(this, 1, meta));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        icon = reg.registerIcon(LibResources.PREFIX_MOD + "material_block");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        int count = Material.values().length;
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        Material mat = Material.fromMeta(meta);
        return mat.getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(net.minecraft.world.IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        Material mat = Material.fromMeta(meta);
        return mat.getColor();
    }
}
