package louis.omoshiroikamo.common.fluid;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import crazypants.enderio.fluid.BucketHandler;
import louis.omoshiroikamo.common.OKCreativeTab;

public class ItemBucketEio extends ItemBucket {

    private final boolean canPlace;
    protected String fluidName;

    public static ItemBucketEio create(Fluid fluid) {
        return create(fluid, true);
    }

    public static ItemBucketEio create(Fluid fluid, boolean canPlace) {
        Block fluidBlock = fluid.getBlock() != null ? fluid.getBlock() : Blocks.air;
        ItemBucketEio b = new ItemBucketEio(fluidBlock, fluid.getName(), canPlace);
        b.init();

        FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(b), new ItemStack(Items.bucket));
        BucketHandler.instance.registerFluid(fluid.getBlock(), b);

        return b;
    }

    protected ItemBucketEio(Block block, String fluidName, boolean canPlace) {
        super(block);
        this.fluidName = fluidName;
        this.canPlace = canPlace;
        setCreativeTab(OKCreativeTab.INSTANCE);
        setContainerItem(Items.bucket);
        String str = "bucket" + StringUtils.capitalize(fluidName);
        setUnlocalizedName(str);
        setTextureName("enderio:" + str);
    }

    @Override
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
        return canPlace && super.tryPlaceContainedLiquid(world, x, y, z);
    }

    protected void init() {
        GameRegistry.registerItem(this, "bucket" + StringUtils.capitalize(fluidName));
    }
}
