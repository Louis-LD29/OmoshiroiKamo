package louis.omoshiroikamo.common.block.multiblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.ItemUtil;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;

public abstract class AbstractMultiBlockModifierTE extends AbstractTE {

    public boolean isFormed = false;
    private boolean isProcessing = false;
    private int currentDuration = 0;
    private int currentProgress = 0;
    private int ticker = 0;

    protected abstract IStructureDefinition getStructureDefinition();

    public abstract int[][] getOffSet();

    protected abstract String getStructurePieceName();

    protected ExtendedFacing getExtendedFacing() {
        return ExtendedFacing.of(ForgeDirection.getOrientation(getFacing()));
    }

    @SuppressWarnings("unchecked")
    protected boolean structureCheck(String piece, int offsetX, int offsetY, int offsetZ) {
        boolean valid = getStructureDefinition().check(
            this,
            piece,
            worldObj,
            getExtendedFacing(),
            xCoord,
            yCoord,
            zCoord,
            offsetX,
            offsetY,
            offsetZ,
            false);

        if (valid && !isFormed) {
            isFormed = true;
            // Logger.info("Multiblock formed!");
        } else if (!valid && isFormed) {
            isFormed = false;
            // Logger.info("Multiblock broken!");
            clearStructureParts();
        }

        return !valid;
    }

    protected void clearStructureParts() {}

    protected boolean addToMachine(TileEntity tile) {
        return false;
    }

    protected boolean addToMachine(Block block, int meta, int x, int y, int z) {
        return false;
    }

    @Override
    public void doUpdate() {

        if (structureCheck(
            getStructurePieceName(),
            getOffSet()[getTier() - 1][0],
            getOffSet()[getTier() - 1][1],
            getOffSet()[getTier() - 1][2])) {
            return;
        }

        if (this.isFormed) {
            this.machineTick();
        } else {
            this.isProcessing = false;
        }
        super.doUpdate();
    }

    public void machineTick() {
        if (this.canProcess()) {
            if (this.currentProgress < this.currentDuration) {
                this.isProcessing = true;
                this.onProcessTick();
                ++this.currentProgress;
            } else {
                this.onProcessComplete();
                this.currentDuration = this.getCurrentProcessDuration();
                this.currentProgress = 0;
                this.isProcessing = false;
            }
        } else {
            this.isProcessing = false;
        }

    }

    public abstract int getBaseDuration();

    public abstract int getMinDuration();

    public abstract int getMaxDuration();

    public abstract float getSpeedMultiplier();

    public abstract boolean canProcess();

    public abstract void onProcessTick();

    public abstract void onProcessComplete();

    public int getCurrentProcessDuration() {
        int duration = (int) ((float) this.getBaseDuration() * this.getSpeedMultiplier());
        if (duration < this.getMinDuration()) {
            return this.getMinDuration();
        } else {
            return duration > this.getMaxDuration() ? this.getMaxDuration() : duration;
        }
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        root.setBoolean("processing", this.isProcessing);
        root.setInteger("curr_dur", this.currentDuration);
        root.setInteger("curr_prog", this.currentProgress);
        root.setBoolean("isFormed", this.isFormed);
    }

    @Override
    public void readCommon(NBTTagCompound root) {

        this.isProcessing = root.getBoolean("processing");
        this.currentDuration = root.getInteger("curr_dur");
        this.currentProgress = root.getInteger("curr_prog");
        this.isFormed = root.getBoolean("isFormed");
    }

    public boolean isFormed() {
        return this.isFormed;
    }

    public boolean isProcessing() {
        return this.isProcessing;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    public abstract int getTier();

    public void ejectAll(ItemStackHandler output) {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            BlockCoord loc = getLocation().getLocation(dir);
            TileEntity te = worldObj.getTileEntity(loc.x, loc.y, loc.z);
            if (te == null) {
                continue;
            }
            for (int i = 0; i < output.getSlots(); i++) {
                ItemStack stack = output.getStackInSlot(i);
                if (stack != null) {
                    int num = ItemUtil.doInsertItem(te, stack, dir.getOpposite());
                    if (num > 0) {
                        stack.stackSize -= num;
                        if (stack.stackSize <= 0) {
                            stack = null;
                        }
                        output.setStackInSlot(i, stack);
                        markDirty();
                    }
                }
            }
        }
    }

}
