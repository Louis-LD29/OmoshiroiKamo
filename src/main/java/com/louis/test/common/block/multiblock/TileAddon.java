package com.louis.test.common.block.multiblock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.common.block.machine.AbstractMachineEntity;
import com.louis.test.common.block.machine.SlotDefinition;

public abstract class TileAddon extends AbstractMachineEntity {

    private TileMain controller;

    public TileAddon(SlotDefinition slotDefinition) {
        super(slotDefinition);
    }

    @Override
    public void onNeighborBlockChange(Block blockId) {
        super.onNeighborBlockChange(blockId);
        if (!worldObj.isRemote) {
            verifyConnectionToController();
        }
    }

    public void findAndSetController() {
        if (hasValidController()) {
            getController().unregisterAddon(this);
            setController(null);
        }

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (te instanceof TileMain) {
                setController((TileMain) te);
                getController().registerAddon(this);
                return;
            }

            if (te instanceof TileAddon otherAddon && otherAddon != this && otherAddon.hasValidController()) {
                TileMain potentialController = otherAddon.getController();
                if (potentialController != null && !potentialController.isInvalid()) {
                    setController(potentialController);
                    getController().registerAddon(this);
                    return;
                }
            }
        }
    }

    public void verifyConnectionToController() {
        if (!hasValidController()) {
            setController(null);
            return;
        }

        Set<TileEntity> visited = new HashSet<>();
        Queue<TileEntity> queue = new LinkedList<>();
        visited.add(this);
        queue.add(this);

        while (!queue.isEmpty()) {
            TileEntity current = queue.poll();
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity te = worldObj.getTileEntity(
                    current.xCoord + dir.offsetX,
                    current.yCoord + dir.offsetY,
                    current.zCoord + dir.offsetZ);
                if (te == null || visited.contains(te)) continue;
                if (te == getController()) {
                    return;
                }

                if (te instanceof TileAddon addon && addon.hasValidController()) {
                    visited.add(te);
                    queue.add(te);
                }
            }
        }

        invalidateController();

        findAndSetController();
    }

    public void invalidateController() {
        if (getController() != null) {
            getController().unregisterAddon(this);
        }
        setController(null);
    }

    public boolean hasValidController() {
        if (getController() == null || getController().isInvalid()) return false;

        return hasPathToController();
    }

    private boolean hasPathToController() {
        Set<TileEntity> visited = new HashSet<>();
        Queue<TileEntity> queue = new LinkedList<>();
        visited.add(this);
        queue.add(this);

        while (!queue.isEmpty()) {
            TileEntity current = queue.poll();
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity te = worldObj.getTileEntity(
                    current.xCoord + dir.offsetX,
                    current.yCoord + dir.offsetY,
                    current.zCoord + dir.offsetZ);

                if (te == null || visited.contains(te)) continue;

                if (te == getController()) {
                    return true; // Tìm thấy đường đến controller
                }

                if (te instanceof TileAddon addon && addon.getController() == getController()) {
                    visited.add(te);
                    queue.add(te);
                }
            }
        }
        return false;
    }

    public TileMain getController() {
        return controller;
    }

    public void setController(TileMain controller) {
        this.controller = controller;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (te instanceof TileAddon addon) {
                addon.invalidateController();
                addon.findAndSetController();
            }
        }
    }
}
