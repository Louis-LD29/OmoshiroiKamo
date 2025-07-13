package com.louis.test.common.block.multiblock.fluid;

import net.minecraft.item.ItemStack;

import com.louis.test.api.enums.ModObject;
import com.louis.test.api.material.MaterialRegistry;
import com.louis.test.common.block.basicblock.machine.SlotDefinition;
import com.louis.test.common.block.multiblock.TileAddon;

public class TileFluidFilter extends TileAddon {

    public TileFluidFilter() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1), MaterialRegistry.get("Iron"));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidFilter.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockFluidFilter.unlocalisedName;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int i, ItemStack itemstack) {
        return false;
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
    public void doUpdate() {
        super.doUpdate();
        if (worldObj.isRemote) return;
        if (hasValidController()) {
            getController().setFluidFilter(true);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (hasValidController()) {
            getController().setFluidFilter(false);
        }
    }
}
