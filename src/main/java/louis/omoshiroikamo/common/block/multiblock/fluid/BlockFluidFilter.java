package louis.omoshiroikamo.common.block.multiblock.fluid;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.basicblock.machine.AbstractMachineBlock;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class BlockFluidFilter extends AbstractMachineBlock<TileFluidFilter> implements IAdvancedTooltipProvider {

    protected BlockFluidFilter() {
        super(ModObject.blockFluidFilter, TileFluidFilter.class);
    }

    public static BlockFluidFilter create() {
        BlockFluidFilter res = new BlockFluidFilter();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, BlockItemFluidFilter.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new TileFluidFilter();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        int meta = world.getBlockMetadata(x, y, z);
        meta = MathHelper.clamp_int(meta, 0, 1);
        if (meta == 1) {
            return 2000;
        } else {
            return super.getExplosionResistance(par1Entity);
        }
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack stack) {
        return stack.getUnlocalizedName();
    }

    @Override
    protected String getMachineFrontIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "fluidFilterFront";
    }
}
