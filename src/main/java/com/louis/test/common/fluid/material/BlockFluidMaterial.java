package com.louis.test.common.fluid.material;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.StringUtils;

import com.louis.test.common.core.lib.LibResources;
import com.louis.test.common.fluid.BlockFluidEio;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidMaterial extends BlockFluidEio {

    protected BlockFluidMaterial(Fluid fluid, Material material) {
        super(fluid, material);
    }

    public static BlockFluidMaterial create(Fluid fluid, Material material) {
        BlockFluidMaterial res = new BlockFluidMaterial(fluid, material);
        res.init();
        fluid.setBlock(res);
        return res;
    }

    @Override
    protected void init() {
        String baseName = StringUtils.uncapitalize(fluidName.replace(".molten", ""));
        GameRegistry.registerBlock(this, "liquid_" + baseName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String baseName = fluidName.replace(".molten", "");
        icons = new IIcon[] { iconRegister.registerIcon(LibResources.PREFIX_MOD + "liquid_" + baseName),
            iconRegister.registerIcon(LibResources.PREFIX_MOD + "liquid_" + baseName + "_flow") };
        fluid.setStillIcon(icons[0]);
        fluid.setFlowingIcon(icons[1]);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1) ? fluid.getStillIcon() : fluid.getFlowingIcon();
    }

}
