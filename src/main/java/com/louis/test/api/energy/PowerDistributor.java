package com.louis.test.api.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;
import com.louis.test.api.enums.VoltageTier;
import com.louis.test.api.material.MaterialEntry;
import com.louis.test.common.block.AbstractTE;

public class PowerDistributor {

    private final List<Receptor> receptors = new ArrayList<Receptor>();
    private ListIterator<Receptor> receptorIterator = receptors.listIterator();
    private boolean receptorsDirty = true;

    private final BlockCoord bc;

    public PowerDistributor(BlockCoord bc) {
        this.bc = bc;
    }

    public void neighboursChanged() {
        receptorsDirty = true;
    }

    public int transmitEnergy(World worldObj, int available) {
        int transmitted = 0;
        checkReceptors(worldObj);

        if (receptors.isEmpty()) {
            return 0;
        }

        if (!receptorIterator.hasNext()) {
            receptorIterator = receptors.listIterator();
        }

        TileEntity senderTile = worldObj.getTileEntity(bc.x, bc.y, bc.z);
        MaterialEntry senderMaterial = (senderTile instanceof AbstractTE) ? ((AbstractTE) senderTile).getMaterial()
            : null;
        VoltageTier senderTier = (senderMaterial != null) ? senderMaterial.getVoltageTier() : null;

        int appliedCount = 0;
        int numReceptors = receptors.size();

        while (receptorIterator.hasNext() && available > 0 && appliedCount < numReceptors) {
            Receptor receptor = receptorIterator.next();
            IPowerInterface pp = receptor.receptor;

            MaterialEntry receiverMaterial = receptor.material;
            VoltageTier receiverTier = (receiverMaterial != null) ? receiverMaterial.getVoltageTier() : null;

            if (pp != null && pp.getMinEnergyReceived(receptor.fromDir.getOpposite()) <= available) {
                int toSend = Math.min(available, pp.getPowerRequest(receptor.fromDir.getOpposite()));
                double efficiency = 1.0;

                if (senderTier == null || receiverTier == null) {
                    efficiency = 0.7;
                } else if (senderTier.ordinal() < receiverTier.ordinal()) {
                    efficiency = 0.25;
                } else if (senderTier.isTooFarFrom(receiverTier)) {
                    efficiency = 0.7;
                }

                int energyDelivered = Math.max(1, (int) (toSend * efficiency));
                int used = pp.recieveEnergy(receptor.fromDir.getOpposite(), energyDelivered);

                // Ghi nhận mất đúng lượng nguồn cung cấp (dù block chỉ nhận ít hơn)
                transmitted += toSend;
                available -= toSend;
            }

            appliedCount++;

            if (!receptors.isEmpty() && !receptorIterator.hasNext()) {
                receptorIterator = receptors.listIterator();
            }
        }

        return transmitted;
    }

    private void checkReceptors(World worldObj) {
        if (!receptorsDirty) return;

        receptors.clear();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            BlockCoord checkLoc = bc.getLocation(dir);
            TileEntity tile = worldObj.getTileEntity(checkLoc.x, checkLoc.y, checkLoc.z);

            if (tile == null) {
                continue;
            }
            MaterialEntry mat = null;
            if (tile instanceof AbstractTE) {
                mat = ((AbstractTE) tile).getMaterial();
            }

            IPowerInterface pi = PowerHandlerUtil.create(tile);
            if (pi != null && pi.canConduitConnect(dir)) {
                receptors.add(new Receptor(pi, mat, dir));
            }
        }

        receptorIterator = receptors.listIterator();
        receptorsDirty = false;
    }

    static class Receptor {

        IPowerInterface receptor;
        MaterialEntry material;
        ForgeDirection fromDir;

        private Receptor(IPowerInterface rec, MaterialEntry material, ForgeDirection fromDir) {
            this.receptor = rec;
            this.material = material;
            this.fromDir = fromDir;
        }
    }
}
