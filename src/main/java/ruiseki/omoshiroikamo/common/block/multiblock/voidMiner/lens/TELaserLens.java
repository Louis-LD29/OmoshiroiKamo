package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.lens;

import net.minecraft.nbt.NBTTagCompound;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.block.abstractClass.AbstractTE;

public class TELaserLens extends AbstractTE {

    public TELaserLens(int meta) {
        this.meta = meta;
    }

    @Override
    public String getMachineName() {
        return ModObject.blockLaserLens.unlocalisedName;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {

    }

    @Override
    public void readCommon(NBTTagCompound root) {

    }
}
