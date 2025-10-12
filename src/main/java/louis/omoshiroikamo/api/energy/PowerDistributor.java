package louis.omoshiroikamo.api.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;

import louis.omoshiroikamo.api.energy.powerInterface.IPowerInterface;

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
        if (!receptors.isEmpty() && !receptorIterator.hasNext()) {
            receptorIterator = receptors.listIterator();
        }

        int appliedCount = 0;
        int numReceptors = receptors.size();
        while (receptorIterator.hasNext() && available > 0 && appliedCount < numReceptors) {
            Receptor receptor = receptorIterator.next();
            IPowerInterface pp = receptor.receptor;
            if (pp != null && pp.getMinEnergyReceived(receptor.fromDir.getOpposite()) <= available) {
                int used = pp.recieveEnergy(receptor.fromDir.getOpposite(), available);
                transmitted += used;
                available -= used;
            }
            if (available <= 0) {
                break;
            }

            if (!receptors.isEmpty() && !receptorIterator.hasNext()) {
                receptorIterator = receptors.listIterator();
            }
            appliedCount++;
        }
        return transmitted;
    }

    private void checkReceptors(World worldObj) {
        if (!receptorsDirty) {
            return;
        }

        receptors.clear();

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            BlockCoord checkLoc = bc.getLocation(dir);
            TileEntity tile = worldObj.getTileEntity(checkLoc.x, checkLoc.y, checkLoc.z);

            if (tile == null) {
                continue;
            }
            IPowerInterface pi = PowerHandlerUtil.create(tile);
            if (pi != null && pi.canConduitConnect(dir)) {
                receptors.add(new Receptor(pi, dir));
            }
        }

        receptorIterator = receptors.listIterator();
        receptorsDirty = false;
    }

    static class Receptor {

        IPowerInterface receptor;
        ForgeDirection fromDir;

        private Receptor(IPowerInterface rec, ForgeDirection fromDir) {
            this.receptor = rec;
            this.fromDir = fromDir;
        }
    }
}
