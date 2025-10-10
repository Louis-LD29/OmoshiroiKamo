package louis.omoshiroikamo.common.block.abstractClass;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;
import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import com.enderio.core.common.TileEntityEnder;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.BlockOK;

public abstract class AbstractBlock<T extends AbstractTE> extends BlockOK
    implements IResourceTooltipProvider, IAdvancedTooltipProvider {

    public static int renderId;

    @SideOnly(Side.CLIENT)
    protected IIcon[][] iconBuffer;

    protected final Random random;
    protected final ModObject modObject;

    protected final Class<T> teClass;

    protected AbstractBlock(ModObject mo, Class<T> teClass, Material mat) {
        super(mo, teClass, mat);
        modObject = mo;
        this.teClass = teClass;
        setHardness(2.0F);
        setStepSound(soundTypeMetal);
        setHarvestLevel("pickaxe", 0);
        random = new Random();
    }

    protected AbstractBlock(ModObject mo, Class<T> teClass) {
        this(mo, teClass, new Material(MapColor.ironColor));
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return false;
    }

    @Override
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

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int blockSide, int blockMeta) {
        if (iconBuffer == null || iconBuffer[0] == null || iconBuffer[0][blockSide] == null) {
            return blockIcon;
        }
        return iconBuffer[0][blockSide];
    }

    public IIcon getModelIcon(IBlockAccess world, int x, int y, int z) {
        return getModelIcon(((AbstractTE) world.getTileEntity(x, y, z)).isActive());
    }

    public IIcon getModelIcon() {
        return getModelIcon(false);
    }

    private IIcon getModelIcon(boolean active) {
        return active ? iconBuffer[1][1] : iconBuffer[1][0];
    }

    @Override
    public boolean doNormalDrops(World world, int x, int y, int z) {
        return false;
    }

    @Override
    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
        if (te != null) {
            ((AbstractTE) te).processDrop(world, x, y, z, te, stack);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof AbstractTE) {
            return ((AbstractTE) tile)
                .onBlockActivated(world, player, ForgeDirection.getOrientation(side), hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, player, stack);
        int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        AbstractTE te = (AbstractTE) world.getTileEntity(x, y, z);
        te.setFacing(getFacingForHeading(heading));
        te.readFromItemStack(stack);
        if (world.isRemote) {
            return;
        }
        world.markBlockForUpdate(x, y, z);
    }

    protected short getFacingForHeading(int heading) {
        switch (heading) {
            case 0:
                return 2;
            case 1:
                return 5;
            case 2:
                return 3;
            case 3:
            default:
                return 4;
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockId) {
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent instanceof AbstractTE te) {
            te.onNeighborBlockChange(world, x, y, z, blockId);
        }
    }

    protected String getMachineFrontIconKey(boolean active) {
        return "";
    }

    protected String getSideIconKey(boolean active) {
        return "omoshiroikamo:machineSide";
    }

    protected String getBackIconKey(boolean active) {
        return "omoshiroikamo:machineBack";
    }

    protected String getTopIconKey(boolean active) {
        return "omoshiroikamo:machineTop";
    }

    protected String getBottomIconKey(boolean active) {
        return "omoshiroikamo:machineBottom";
    }

    protected String getModelIconKey(boolean active) {
        return getSideIconKey(active);
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer player, List<String> list, boolean advanced) {

    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    public boolean isActive(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te instanceof AbstractTE) {
            return ((AbstractTE) te).isActive();
        }
        return false;
    }
}
