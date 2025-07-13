package com.louis.test.common.block.meta.energyConnector;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.config.Config;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MTEInsulator extends AbstractMTEConnector {

    public int currentTickAccepted = 0;
    boolean inICNet = false;

    public MTEInsulator(int meta) {
        super(meta);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    protected boolean canTakeLV() {
        return true;
    }

    @Override
    protected boolean canTakeHV() {
        return true;
    }

    @Override
    protected boolean canTakeMV() {
        return true;
    }

    @Override
    public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
        ForgeDirection fd = ForgeDirection.getOrientation(host.getFacing())
            .getOpposite();
        return Vec3.createVectorHelper(.5 + fd.offsetX * .0625, .5 + fd.offsetY * .0625, .5 + fd.offsetZ * .0625);
    }

    @Override
    public Vec3 getConnectionOffset(Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(host.getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2;
        return Vec3
            .createVectorHelper(.5 - conRadius * fd.offsetX, .5 - conRadius * fd.offsetY, .5 - conRadius * fd.offsetZ);
    }

    @SideOnly(Side.CLIENT)
    private AxisAlignedBB renderAABB;

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderAABB == null) {
            if (Config.increasedRenderboxes) {
                int inc = getRenderRadiusIncrease();
                renderAABB = AxisAlignedBB.getBoundingBox(
                    xCoord - inc,
                    yCoord - inc,
                    zCoord - inc,
                    xCoord + inc + 1,
                    yCoord + inc + 1,
                    zCoord + inc + 1);
            } else renderAABB = super.getRenderBoundingBox();
        }
        return renderAABB;
    }

    int getRenderRadiusIncrease() {
        return WireType.COPPER.getMaxLength();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (world.isRemote) return;

        ForgeDirection direction = null;
        float pitch = player.rotationPitch;

        int dx = x, dy = y, dz = z;
        if (pitch > 60) {
            direction = ForgeDirection.UP;
        } else if (pitch < -60) {
            direction = ForgeDirection.DOWN;
        }
        if (direction == null) {
            int yaw = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
            switch (yaw) {
                case 0:
                    direction = ForgeDirection.NORTH;
                    break;
                case 1:
                    direction = ForgeDirection.EAST;
                    break;
                case 2:
                    direction = ForgeDirection.SOUTH;
                    break;
                case 3:
                default:
                    direction = ForgeDirection.WEST;
                    break;
            }

            ForgeDirection opposite = direction.getOpposite();
            int tx = x + opposite.offsetX;
            int ty = y + opposite.offsetY;
            int tz = z + opposite.offsetZ;
            Block targetBlock = world.getBlock(tx, ty, tz);

            if (targetBlock == null || targetBlock.isAir(world, tx, ty, tz)) {
                direction = null;
            }
        }

        if (direction == null) {
            dy = y + 1;
            if (world.isSideSolid(dx, dy, dz, ForgeDirection.UP, false)) {
                direction = ForgeDirection.DOWN;
            } else {
                dy = y - 1;
                if (world.isSideSolid(dx, dy, dz, ForgeDirection.DOWN, false)) {
                    direction = ForgeDirection.UP;
                } else {
                    world.setBlockToAir(x, y, z);
                    if (player instanceof EntityPlayerMP playerMP) {
                        if (!playerMP.inventory.addItemStackToInventory(stack.copy())) {
                            playerMP.dropPlayerItemWithRandomChoice(stack.copy(), false);
                        }
                    }
                    return;
                }
            }
        }

        AbstractTE te = (AbstractTE) world.getTileEntity(x, y, z);
        if (te != null) {
            te.setFacing((short) direction.ordinal());
            te.readFromItemStack(stack);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z, Block block) {
        ForgeDirection dir = ForgeDirection.getOrientation(getFacing());
        switch (dir) {
            case UP:
                block.setBlockBounds(0.375F, 0F, 0.375F, 0.625F, 0.5F, 0.625F);
                break;
            case DOWN:
                block.setBlockBounds(0.375F, 0.5F, 0.375F, 0.625F, 1F, 0.625F);
                break;
            case NORTH:
                block.setBlockBounds(0.375F, 0.375F, 0.5F, 0.625F, 0.625F, 1F);
                break;
            case SOUTH:
                block.setBlockBounds(0.375F, 0.375F, 0F, 0.625F, 0.625F, 0.5F);
                break;
            case EAST:
                block.setBlockBounds(0F, 0.375F, 0.375F, 0.5F, 0.625F, 0.625F);
                break;
            case WEST:
                block.setBlockBounds(0.5F, 0.375F, 0.375F, 1F, 0.625F, 0.625F);
                break;
        }
    }

}
