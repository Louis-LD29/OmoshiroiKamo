package com.louis.test.common.block;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.enderio.core.common.TileEntityEnder;
import com.louis.test.api.SideReference;
import com.louis.test.api.material.MaterialEntry;
import com.louis.test.client.gui.modularui2.MGuis;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public abstract class AbstractTE extends TileEntityEio implements IGuiHolder<PosGuiData> {

    public short facing = -1;
    public boolean redstoneCheckPassed;
    public boolean redstoneStateDirty = true;
    protected boolean forceClientUpdate = true;
    protected boolean lastActive;
    protected int ticksSinceActiveChanged = 0;
    public boolean isDirty = false;
    protected boolean notifyNeighbours = false;
    protected MaterialEntry material;
    protected int meta;

    public short getFacing() {
        return facing;
    }

    public void setFacing(short facing) {
        this.facing = facing;
    }

    public ForgeDirection getFacingDir() {
        return ForgeDirection.getOrientation(facing);
    }

    public int getMeta() {
        return meta;
    }

    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
                                    float hitZ) {
        return false;
    }

    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {

    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        readFromItemStack(stack);
        if (!world.isRemote) {
            world.markBlockForUpdate(x, y, z);
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z, Block block) {

    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid) {
        redstoneStateDirty = true;

    }

    public void dropBlockAsItem(World world, int x, int y, int z, ItemStack itemIn) {

    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {

    }

    protected void processDrop(World world, int x, int y, int z, TileEntityEnder te, ItemStack stack) {
        writeToItemStack(stack);
    }

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return false;
    }

    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
        return false;
    }

    // Special

    public abstract String getMachineName();

    public abstract boolean isActive();

    protected abstract boolean processTasks(boolean redstoneCheckPassed);

    @Override
    protected void doUpdate() {
        if (!isServerSide()) {
            updateEntityClient();
            return;
        } // else is server, do all logic only on the server

        boolean requiresClientSync = forceClientUpdate;
        boolean prevRedCheck = redstoneCheckPassed;
        if (redstoneStateDirty) {
            redstoneCheckPassed = true;
            redstoneStateDirty = false;
        }

        requiresClientSync |= prevRedCheck != redstoneCheckPassed;

        requiresClientSync |= processTasks(redstoneCheckPassed);
        if (requiresClientSync) {

            // this will cause 'getPacketDescription()' to be called and its result
            // will be sent to the PacketHandler on the other end of
            // client/server connection
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            // And this will make sure our current tile entity state is saved
            markDirty();
        }

        if (notifyNeighbours) {
            worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
            notifyNeighbours = false;
        }
    }

    protected void updateEntityClient() {
        if (isActive() != lastActive) {
            ticksSinceActiveChanged++;
            if (ticksSinceActiveChanged > 20 || isActive()) {
                ticksSinceActiveChanged = 0;
                lastActive = isActive();
                forceClientUpdate = true;
            }
        }

        if (forceClientUpdate) {
            if (worldObj.rand.nextInt(1024) <= (isDirty ? 256 : 0)) {
                isDirty = !isDirty;
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            forceClientUpdate = false;
        }
    }

    public void setForceClientUpdate(boolean forceClientUpdate) {
        this.forceClientUpdate = forceClientUpdate;
    }

    @Override
    protected void writeCustomNBT(NBTTagCompound root) {
        root.setShort("facing", facing);
        root.setBoolean("redstoneCheckPassed", redstoneCheckPassed);
        root.setBoolean("forceClientUpdate", forceClientUpdate);
        forceClientUpdate = false;
        writeCommon(root);
    }

    @Override
    protected void readCustomNBT(NBTTagCompound root) {
        setFacing(root.getShort("facing"));
        redstoneCheckPassed = root.getBoolean("redstoneCheckPassed");
        forceClientUpdate = root.getBoolean("forceClientUpdate");
        readCommon(root);
    }

    public abstract void writeCommon(NBTTagCompound root);

    public abstract void readCommon(NBTTagCompound root);

    public boolean isServerSide() {
        return !this.worldObj.isRemote || SideReference.Side.Server;
    }

    public void readFromItemStack(ItemStack stack) {
        if (stack == null || stack.stackTagCompound == null) {
            return;
        }
        readCommon(stack.stackTagCompound);
    }

    public void writeToItemStack(ItemStack stack) {
        if (stack == null) {
            return;
        }
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }

        NBTTagCompound root = stack.stackTagCompound;
        root.setBoolean("te.abstractMachine", true);
        writeCommon(root);
    }

    public MaterialEntry getMaterial() {
        return material;
    }

    public void openGui(EntityPlayer player) {
        if (isServerSide()) {
            MGuis.open(player, this);
        }
    }

    public short getFacingForHeading(int heading) {
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

    public ChunkCoordinates getChunkCoord() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }
}
