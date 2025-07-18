package louis.omoshiroikamo.common.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidEio extends BlockFluidClassic {

    public static class Mana extends BlockFluidEio {

        protected Mana(Fluid fluid, Material material) {
            super(fluid, material);
        }
    }

    public static class Steam extends BlockFluidEio {

        protected Steam(Fluid fluid, Material material) {
            super(fluid, material);
        }

        @Override
        public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
            if (!world.isRemote) {
                entity.setFire(3);
                super.onEntityCollidedWithBlock(world, x, y, z, entity);
            }
        }
    }

    public static class Hydrogen extends BlockFluidEio {

        protected Hydrogen(Fluid fluid, Material material) {
            super(fluid, material);
        }
    }

    public static class Oxygen extends BlockFluidEio {

        protected Oxygen(Fluid fluid, Material material) {
            super(fluid, material);
        }
    }

    public static BlockFluidEio create(Fluid fluid, Material material) {
        BlockFluidEio res = new BlockFluidEio(fluid, material);
        res.init();
        fluid.setBlock(res);
        return res;
    }

    protected Fluid fluid;

    protected BlockFluidEio(Fluid fluid, Material material) {
        super(fluid, material);
        this.fluid = fluid;
        setBlockName(fluid.getUnlocalizedName());
    }

    protected void init() {
        GameRegistry.registerBlock(this, "fluid" + StringUtils.capitalize(fluidName));

    }

    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[] { iconRegister.registerIcon("test:" + fluidName + "_still"),
            iconRegister.registerIcon("test:" + fluidName + "_flow") };

        fluid.setIcons(icons[0], icons[1]);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid()) {
            return false;
        }
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z)
            .getMaterial()
            .isLiquid()) {
            return false;
        }
        return super.displaceIfPossible(world, x, y, z);
    }
}
