package louis.omoshiroikamo.common.block.multiblock;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.enderio.core.common.TileEntityEnder;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;

public class AbstractMultiBlockBlock<T extends AbstractMultiBlockModifierTE> extends AbstractBlock<T> {

    protected AbstractMultiBlockBlock(ModObject mo, Class<T> teClass, Material material) {
        super(mo, teClass, material);
    }

    protected AbstractMultiBlockBlock(ModObject mo, Class<T> teClass) {
        super(mo, teClass);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof AbstractMultiBlockModifierTE blockModifierTE) {
            if (player instanceof EntityPlayer) {
                blockModifierTE.setPlayer((EntityPlayer) player);
            }
        }
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {

    }
}
