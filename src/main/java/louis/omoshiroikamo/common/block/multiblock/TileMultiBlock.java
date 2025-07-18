package louis.omoshiroikamo.common.block.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import louis.omoshiroikamo.api.enums.ModObject;

public class TileMultiBlock extends AbstractMultiBlockEntity<TileMultiBlock> {

    private static final int[] SHAPE_OFFSET = MultiBlockStructure.SHAPE_OFFSET;

    static {
        MultiBlockStructure.registerAltarStructureInfo();
    }

    @Override
    public String getMachineName() {
        return ModObject.blockMultiblock.unlocalisedName;
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
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        if (mMachine) {
            openGui(player);
            return true;
        }
        return super.onBlockActivated(world, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return ModularPanel.defaultPanel(getMachineName())
            .bindPlayerInventory();
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        if (!isServerSide()) return;
        if (structureCheck(getStructurePieceName(), SHAPE_OFFSET[0], SHAPE_OFFSET[1], SHAPE_OFFSET[2])) return;
    }

    @Override
    protected String getStructurePieceName() {
        return MultiBlockStructure.SHAPE_NAME;
    }

    @Override
    public IStructureDefinition<TileMultiBlock> getStructureDefinition() {
        return MultiBlockStructure.STRUCTURE_DEFINITION;
    }
}
