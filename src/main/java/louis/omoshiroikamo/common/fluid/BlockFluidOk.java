package louis.omoshiroikamo.common.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockFluidOk extends BlockFluidClassic {

    String texture;
    boolean alpha;
    public IIcon stillIcon;
    public IIcon flowIcon;
    boolean overwriteFluidIcons = true;
    private Fluid fluid = null;

    public BlockFluidOk(Fluid fluid, Material material, String texture) {
        super(fluid, material);
        this.texture = texture;
        this.setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public BlockFluidOk(Fluid fluid, Material material, String texture, boolean alpha) {
        this(fluid, material, texture);
        this.alpha = alpha;
    }

    @Override
    public int getRenderBlockPass() {
        return alpha ? 1 : 0;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        stillIcon = iconRegister.registerIcon(LibResources.PREFIX_MOD + texture.toLowerCase());
        flowIcon = iconRegister.registerIcon(LibResources.PREFIX_MOD + texture.toLowerCase() + "_flow");

        if (overwriteFluidIcons) this.getFluid()
            .setIcons(stillIcon, flowIcon);

        if (this.getFluid()
            .getBlock() != this && fluid != null) fluid.setIcons(stillIcon, flowIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) return stillIcon;
        return flowIcon;
    }

    public void suppressOverwritingFluidIcons() {
        overwriteFluidIcons = false;
    }

    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
    }
}
