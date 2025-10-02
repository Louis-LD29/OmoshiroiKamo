package louis.omoshiroikamo.common.util;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import louis.omoshiroikamo.api.DirectionalChunkCoords;
import louis.omoshiroikamo.api.energy.wire.IWireConnectable;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is Utils
 */
public class Utils {

    public static ChunkCoordinates toCC(Object object) {
        if (object instanceof ChunkCoordinates) {
            return (ChunkCoordinates) object;
        }
        if (object instanceof TileEntity) {
            return new ChunkCoordinates(
                ((TileEntity) object).xCoord,
                ((TileEntity) object).yCoord,
                ((TileEntity) object).zCoord);
        }
        return null;
    }

    public static DirectionalChunkCoords toDirCC(Object object, ForgeDirection direction) {
        if (object instanceof ChunkCoordinates) {
            return new DirectionalChunkCoords((ChunkCoordinates) object, direction);
        }
        if (object instanceof TileEntity) {
            return new DirectionalChunkCoords(
                ((TileEntity) object).xCoord,
                ((TileEntity) object).yCoord,
                ((TileEntity) object).zCoord,
                direction);
        }
        return null;
    }

    public static IWireConnectable toIIC(Object object, World world) {
        if (object instanceof IWireConnectable) {
            return (IWireConnectable) object;
        } else if (object instanceof ChunkCoordinates && world != null
            && world.blockExists(
                ((ChunkCoordinates) object).posX,
                ((ChunkCoordinates) object).posY,
                ((ChunkCoordinates) object).posZ)) {
                    TileEntity te = world.getTileEntity(
                        ((ChunkCoordinates) object).posX,
                        ((ChunkCoordinates) object).posY,
                        ((ChunkCoordinates) object).posZ);
                    if (te instanceof IWireConnectable) {
                        return (IWireConnectable) te;
                    }
                }
        return null;
    }

    public static boolean tilePositionMatch(TileEntity tile0, TileEntity tile1) {
        return tile0.xCoord == tile1.xCoord && tile0.yCoord == tile1.yCoord && tile0.zCoord == tile1.zCoord;
    }

    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityLivingBase living,
        boolean bool) {
        float f = 1.0F;
        float f1 = living.prevRotationPitch + (living.rotationPitch - living.prevRotationPitch) * f;
        float f2 = living.prevRotationYaw + (living.rotationYaw - living.prevRotationYaw) * f;
        double d0 = living.prevPosX + (living.posX - living.prevPosX) * (double) f;
        double d1 = living.prevPosY + (living.posY - living.prevPosY) * (double) f
            + (double) (world.isRemote
                ? living.getEyeHeight()
                    - (living instanceof EntityPlayer ? ((EntityPlayer) living).getDefaultEyeHeight() : 0)
                : living.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the
        // eye height clientside and player yOffset differences
        double d2 = living.prevPosZ + (living.posZ - living.prevPosZ) * (double) f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (living instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) living).theItemInWorldManager.getBlockReachDistance();
        }

        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return world.func_147447_a(vec3, vec31, bool, !bool, false);
    }

    public static boolean canBlocksSeeOther(World world, ChunkCoordinates cc0, ChunkCoordinates cc1, Vec3 pos0,
        Vec3 pos1) {
        HashSet<ChunkCoordinates> inter = rayTrace(pos0, pos1, world);
        Iterator<ChunkCoordinates> it = inter.iterator();
        while (it.hasNext()) {
            ChunkCoordinates cc = it.next();
            if (!cc.equals(cc0) && !cc.equals(cc1)) {
                return false;
            }
        }
        return true;
    }

    public static Vec3 getFlowVector(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) instanceof BlockFluidBase) {
            return ((BlockFluidBase) world.getBlock(x, y, z)).getFlowVector(world, x, y, z);
        } else if (!(world.getBlock(x, y, z) instanceof BlockLiquid)) {
            return Vec3.createVectorHelper(0, 0, 0);
        }

        BlockLiquid block = (BlockLiquid) world.getBlock(x, y, z);
        Vec3 vec3 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        Material mat = block.getMaterial();
        int l = getEffectiveFlowDecay(world, x, y, z, mat);

        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = x;
            int k1 = z;

            if (i1 == 0) {
                j1 = x - 1;
            }
            if (i1 == 1) {
                k1 = z - 1;
            }
            if (i1 == 2) {
                ++j1;
            }
            if (i1 == 3) {
                ++k1;
            }
            int l1 = getEffectiveFlowDecay(world, j1, y, k1, mat);
            int i2;

            if (l1 < 0) {
                if (!world.getBlock(j1, y, k1)
                    .getMaterial()
                    .blocksMovement()) {
                    l1 = getEffectiveFlowDecay(world, j1, y - 1, k1, mat);

                    if (l1 >= 0) {
                        i2 = l1 - (l - 8);
                        vec3 = vec3
                            .addVector((double) ((j1 - x) * i2), (double) ((y - y) * i2), (double) ((k1 - z) * i2));
                    }
                }
            } else if (l1 >= 0) {
                i2 = l1 - l;
                vec3 = vec3.addVector((double) ((j1 - x) * i2), (double) ((y - y) * i2), (double) ((k1 - z) * i2));
            }
        }

        if (world.getBlockMetadata(x, y, z) >= 8) {
            boolean flag = false;

            if (flag || block.isBlockSolid(world, x, y, z - 1, 2)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x, y, z + 1, 3)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x - 1, y, z, 4)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x + 1, y, z, 5)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x, y + 1, z - 1, 2)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x, y + 1, z + 1, 3)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x - 1, y + 1, z, 4)) {
                flag = true;
            }
            if (flag || block.isBlockSolid(world, x + 1, y + 1, z, 5)) {
                flag = true;
            }
            if (flag) {
                vec3 = vec3.normalize()
                    .addVector(0.0D, -6.0D, 0.0D);
            }
        }
        vec3 = vec3.normalize();
        return vec3;
    }

    static int getEffectiveFlowDecay(IBlockAccess world, int x, int y, int z, Material mat) {
        if (world.getBlock(x, y, z)
            .getMaterial() != mat) {
            return -1;
        }
        int l = world.getBlockMetadata(x, y, z);
        if (l >= 8) {
            l = 0;
        }
        return l;
    }

    public static Vec3 addVectors(Vec3 vec0, Vec3 vec1) {
        return vec0.addVector(vec1.xCoord, vec1.yCoord, vec1.zCoord);
    }

    public static Vec3 rotateVector(Vec3 vec0, double angleX, double angleY, double angleZ) {
        Vec3 vec1 = Vec3.createVectorHelper(vec0.xCoord, vec0.yCoord, vec0.zCoord);
        if (angleX != 0) {
            vec1.rotateAroundX((float) angleX);
        }
        if (angleY != 0) {
            vec1.rotateAroundY((float) angleY);
        }
        if (angleZ != 0) {
            vec1.rotateAroundZ((float) angleZ);
        }
        return vec1;
    }

    public static boolean isVecInEntityHead(EntityLivingBase entity, Vec3 vec) {
        if (entity.height / entity.width < 2)// Crude check to see if the entity is bipedal or at least upright (this
        // should work for blazes)
        {
            return false;
        }
        double d = vec.yCoord - (entity.posY + entity.getEyeHeight());
        if (Math.abs(d) < .25) {
            return true;
        }
        return false;
    }

    public static HashSet<ChunkCoordinates> rayTrace(Vec3 start, Vec3 end, World world) {
        HashSet<ChunkCoordinates> ret = new HashSet<ChunkCoordinates>();
        HashSet<ChunkCoordinates> checked = new HashSet<ChunkCoordinates>();
        // x
        if (start.xCoord > end.xCoord) {
            Vec3 tmp = start;
            start = end;
            end = tmp;
        }
        double min = start.xCoord;
        double dif = end.xCoord - min;
        double lengthAdd = Math.ceil(min) - start.xCoord;
        Vec3 mov = start.subtract(end);
        if (mov.xCoord != 0) {
            mov = scalarProd(mov, 1 / mov.xCoord);
            ray(dif, mov, start, lengthAdd, ret, world, checked, Blocks.diamond_ore);
        }
        // y
        if (mov.yCoord != 0) {
            if (start.yCoord > end.yCoord) {
                Vec3 tmp = start;
                start = end;
                end = tmp;
            }
            min = start.yCoord;
            dif = end.yCoord - min;
            lengthAdd = Math.ceil(min) - start.yCoord;
            mov = start.subtract(end);
            mov = scalarProd(mov, 1 / mov.yCoord);

            ray(dif, mov, start, lengthAdd, ret, world, checked, Blocks.iron_ore);
        }

        // z
        if (mov.zCoord != 0) {
            if (start.zCoord > end.zCoord) {
                Vec3 tmp = start;
                start = end;
                end = tmp;
            }
            min = start.zCoord;
            dif = end.zCoord - min;
            lengthAdd = Math.ceil(min) - start.zCoord;
            mov = start.subtract(end);
            mov = scalarProd(mov, 1 / mov.zCoord);

            ray(dif, mov, start, lengthAdd, ret, world, checked, Blocks.gold_ore);
        }
        return ret;
    }

    private static void ray(double dif, Vec3 mov, Vec3 start, double lengthAdd, HashSet<ChunkCoordinates> ret,
        World world, HashSet<ChunkCoordinates> checked, Block tmp) {
        // Do NOT set this to true unless for debugging. Causes blocks to be placed along the traced ray
        boolean place = false;
        double standartOff = .0625;
        for (int i = 0; i < dif; i++) {
            Vec3 pos = addVectors(start, scalarProd(mov, i + lengthAdd + standartOff));
            Vec3 posNext = addVectors(start, scalarProd(mov, i + 1 + lengthAdd + standartOff));
            Vec3 posPrev = addVectors(start, scalarProd(mov, i + lengthAdd - standartOff));
            Vec3 posVeryPrev = addVectors(start, scalarProd(mov, i - 1 + lengthAdd - standartOff));

            ChunkCoordinates cc = new ChunkCoordinates(
                (int) Math.floor(pos.xCoord),
                (int) Math.floor(pos.yCoord),
                (int) Math.floor(pos.zCoord));
            Block b;
            int meta;
            if (!checked.contains(cc) && i + lengthAdd + standartOff < dif) {
                b = world.getBlock(cc.posX, cc.posY, cc.posZ);
                meta = world.getBlockMetadata(cc.posX, cc.posY, cc.posZ);
                if (b.canCollideCheck(meta, false)
                    && b.collisionRayTrace(world, cc.posX, cc.posY, cc.posZ, pos, posNext) != null) {
                    ret.add(cc);
                }
                if (place) {
                    world.setBlock(cc.posX, cc.posY, cc.posZ, tmp);
                }
                checked.add(cc);
            }
            cc = new ChunkCoordinates(
                (int) Math.floor(posPrev.xCoord),
                (int) Math.floor(posPrev.yCoord),
                (int) Math.floor(posPrev.zCoord));
            if (!checked.contains(cc) && i + lengthAdd - standartOff < dif) {
                b = world.getBlock(cc.posX, cc.posY, cc.posZ);
                meta = world.getBlockMetadata(cc.posX, cc.posY, cc.posZ);
                if (b.canCollideCheck(meta, false)
                    && b.collisionRayTrace(world, cc.posX, cc.posY, cc.posZ, posVeryPrev, posPrev) != null) {
                    ret.add(cc);
                }
                if (place) {
                    world.setBlock(cc.posX, cc.posY, cc.posZ, tmp);
                }
                checked.add(cc);
            }
        }
    }

    public static Vec3 scalarProd(Vec3 v, double s) {
        return Vec3.createVectorHelper(v.xCoord * s, v.yCoord * s, v.zCoord * s);
    }

    public static ChunkCoordinates rayTraceForFirst(Vec3 start, Vec3 end, World w, HashSet<ChunkCoordinates> ignore) {
        HashSet<ChunkCoordinates> trace = rayTrace(start, end, w);
        for (ChunkCoordinates cc : ignore) trace.remove(cc);
        if (start.xCoord != end.xCoord) {
            trace = findMinOrMax(trace, start.xCoord > end.xCoord, 0);
        }
        if (start.yCoord != end.yCoord) {
            trace = findMinOrMax(trace, start.yCoord > end.yCoord, 0);
        }
        if (start.zCoord != end.zCoord) {
            trace = findMinOrMax(trace, start.zCoord > end.zCoord, 0);
        }
        if (trace.size() > 0) {
            ChunkCoordinates ret = trace.iterator()
                .next();
            return ret;
        }
        return null;
    }

    public static HashSet<ChunkCoordinates> findMinOrMax(HashSet<ChunkCoordinates> in, boolean max, int coord) {
        HashSet<ChunkCoordinates> ret = new HashSet<ChunkCoordinates>();
        int currMinMax = max ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        // find minimum
        for (ChunkCoordinates cc : in) {
            int curr = (coord == 0 ? cc.posX : (coord == 1 ? cc.posY : cc.posZ));
            if (max ^ (curr < currMinMax)) {
                currMinMax = curr;
            }
        }
        // fill ret set
        for (ChunkCoordinates cc : in) {
            int curr = (coord == 0 ? cc.posX : (coord == 1 ? cc.posY : cc.posZ));
            if (curr == currMinMax) {
                ret.add(cc);
            }
        }
        return ret;
    }
}
