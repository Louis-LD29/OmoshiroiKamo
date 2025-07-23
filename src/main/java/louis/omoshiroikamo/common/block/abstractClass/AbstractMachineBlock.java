package louis.omoshiroikamo.common.block.abstractClass;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.client.IResourceTooltipProvider;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.core.lib.LibResources;

public abstract class AbstractMachineBlock<T extends AbstractTE> extends AbstractBlock<T>
    implements IResourceTooltipProvider, IAdvancedTooltipProvider {

    @SideOnly(Side.CLIENT)
    protected IIcon[][] iconBuffer;

    protected AbstractMachineBlock(ModObject mo, Class<T> teClass, Material mat) {
        super(mo, teClass, mat);
    }

    protected AbstractMachineBlock(ModObject mo, Class<T> teClass) {
        this(mo, teClass, new Material(MapColor.ironColor));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        // If active, randomly throw some smoke around
        if (isActive(world, x, y, z)) {
            float startX = x + 1.0F;
            float startY = y + 1.0F;
            float startZ = z + 1.0F;
            for (int i = 0; i < 4; i++) {
                float xOffset = -0.2F - rand.nextFloat() * 0.6F;
                float yOffset = -0.1F + rand.nextFloat() * 0.2F;
                float zOffset = -0.2F - rand.nextFloat() * 0.6F;
                world.spawnParticle("smoke", startX + xOffset, startY + yOffset, startZ + zOffset, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected boolean isActive(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te instanceof AbstractTE) {
            return ((AbstractTE) te).isActive();
        }
        return false;
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack stack) {
        return stack.getUnlocalizedName();
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iIconRegister) {

        iconBuffer = new IIcon[2][12];
        String side = getSideIconKey(false);
        // first the 6 sides in OFF state
        iconBuffer[0][0] = iIconRegister.registerIcon(getBottomIconKey(false));
        iconBuffer[0][1] = iIconRegister.registerIcon(getTopIconKey(false));
        iconBuffer[0][2] = iIconRegister.registerIcon(getBackIconKey(false));
        iconBuffer[0][3] = iIconRegister.registerIcon(getMachineFrontIconKey(false));
        iconBuffer[0][4] = iIconRegister.registerIcon(side);
        iconBuffer[0][5] = iIconRegister.registerIcon(side);

        side = getSideIconKey(true);
        iconBuffer[0][6] = iIconRegister.registerIcon(getBottomIconKey(true));
        iconBuffer[0][7] = iIconRegister.registerIcon(getTopIconKey(true));
        iconBuffer[0][8] = iIconRegister.registerIcon(getBackIconKey(true));
        iconBuffer[0][9] = iIconRegister.registerIcon(getMachineFrontIconKey(true));
        iconBuffer[0][10] = iIconRegister.registerIcon(side);
        iconBuffer[0][11] = iIconRegister.registerIcon(side);

        iconBuffer[1][0] = iIconRegister.registerIcon(getModelIconKey(false));
        iconBuffer[1][1] = iIconRegister.registerIcon(getModelIconKey(true));
    }

    protected abstract String getMachineFrontIconKey(boolean active);

    protected String getSideIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "machineSide";
    }

    protected String getBackIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "machineBack";
    }

    protected String getTopIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "machineTop";
    }

    protected String getBottomIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "machineBottom";
    }

    protected String getModelIconKey(boolean active) {
        return getSideIconKey(active);
    }

}
