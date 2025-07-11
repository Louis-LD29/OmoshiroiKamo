package com.louis.test.common.block.material;

import static org.apache.commons.lang3.StringUtils.capitalize;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.louis.test.api.MaterialEntry;
import com.louis.test.api.MaterialRegistry;
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
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockMaterial.class, ModObject.blockMaterial.unlocalisedName);

        int meta = 0;
        for (MaterialEntry entry : MaterialRegistry.all()) {
            String matName = entry.name.toLowerCase(Locale.ROOT)
                .replace(' ', '_');

            registerMaterialOreDict(matName, meta);

            if ("Carbon Steel".equalsIgnoreCase(entry.name)) {
                registerMaterialOreDict("Steel", meta);
            }

            meta++;
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
    public float getBlockHardness(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return MaterialRegistry.fromMeta(meta)
            .getHardness();
    }

    @Override
    public float getExplosionResistance(Entity exploder, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        int meta = world.getBlockMetadata(x, y, z);
        return MaterialRegistry.fromMeta(meta)
            .getResistance();
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
        int count = MaterialRegistry.all()
            .size();
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        MaterialEntry mat = MaterialRegistry.fromMeta(meta);
        return mat.getColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(net.minecraft.world.IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        MaterialEntry mat = MaterialRegistry.fromMeta(meta);
        return mat.getColor();
    }
}
