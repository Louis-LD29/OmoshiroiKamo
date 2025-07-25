package louis.omoshiroikamo.common.block.furnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.client.gui.modularui2.MGuiTextures;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTaskTE;
import louis.omoshiroikamo.common.block.abstractClass.machine.SlotDefinition;

public class TEFurnace extends AbstractTaskTE {

    public int burnTime = 0;

    public int totalBurnTime;

    public TEFurnace() {
        super(new SlotDefinition(0, 1, 2, 10, -1, -1));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFurnace.unlocalisedName;
    }

    @Override
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        openGui(player);
        return true;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
        if (slot == 1) return TileEntityFurnace.isItemFuel(itemstack);
        return slotDefinition.isInputSlot(slot);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        if (slot == 1) return false;
        return super.canExtractItem(slot, itemstack, side);
    }

    public int getBurnTime(ItemStack item) {
        return TileEntityFurnace.getItemBurnTime(item);
    }

    @Override
    protected boolean processTasks(boolean redstoneChecksPassed) {
        if (burnTime <= 0 && inv.getStackInSlot(0) != null) {
            ItemStack fuelStack = inv.getStackInSlot(1);
            if (fuelStack == null || fuelStack.stackSize <= 0) return false;

            int fuelBurn = getBurnTime(fuelStack);
            if (fuelBurn <= 0) return false;

            burnTime = totalBurnTime = fuelBurn;

            ItemStack container = fuelStack.getItem()
                .getContainerItem(fuelStack);
            if (container != null && !ItemStack.areItemStacksEqual(container, fuelStack)) {
                inv.setStackInSlot(1, container);
            } else {
                fuelStack.stackSize--;
                if (fuelStack.stackSize <= 0) {
                    inv.setStackInSlot(1, null);
                }
            }
            forceClientUpdate = true;
        }

        if (burnTime <= 0) {
            return false;
        }

        return super.processTasks(redstoneChecksPassed);
    }

    @Override
    protected void taskComplete() {
        super.taskComplete();
        forceClientUpdate = true;
    }

    @Override
    public float useStage() {
        return useStage(1f);
    }

    @Override
    public float useStage(float amount) {
        if (burnTime <= 0) return 0;

        float used = Math.min(burnTime, amount);
        burnTime -= used;

        if (currentTask != null) {
            currentTask.update(used);
        }
        return used;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
        root.setInteger("burnTime", burnTime);
        root.setInteger("totalBurnTime", totalBurnTime);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
        burnTime = root.getInteger("burnTime");
        totalBurnTime = root.getInteger("totalBurnTime");
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.registerSlotGroup("furnace", 3);
        ModularPanel panel = new ModularPanel(getMachineName());
        syncManager.syncValue("progress", new DoubleSyncValue(this::getProgress, value -> setProgress((float) value)));
        syncManager.syncValue("burnTime", new IntSyncValue(() -> burnTime, value -> burnTime = value));
        syncManager.syncValue("totalBurnTime", new IntSyncValue(() -> totalBurnTime, value -> totalBurnTime = value));
        panel.child(
            new Column().child(
                new ProgressWidget().progress(this::getProgress)
                    .topRel(0.2f)
                    .leftRel(0.375f)
                    .texture(GuiTextures.PROGRESS_ARROW, 20))
                .child(
                    SlotGroupWidget.builder()
                        .matrix("ISSOOO", "BSSOOO", "ISSOOO")
                        .key(
                            'I',
                            index -> new ItemSlot().slot(
                                new ModularSlot(inv, index).slotGroup("furnace")
                                    .filter(stack -> isMachineItemValidForSlot(index, stack))))
                        .key(
                            'O',
                            index -> new ItemSlot().slot(
                                new ModularSlot(inv, index + 2)
                                    .filter(stack -> isMachineItemValidForSlot(index + 2, stack))
                                    .slotGroup("furnace")))
                        .key(
                            'B',
                            new ProgressWidget().progress(() -> (double) burnTime / totalBurnTime)
                                .texture(MGuiTextures.PROGRESS_BURN, 16)
                                .direction(ProgressWidget.Direction.UP))
                        .build()
                        .topRel(0.1f)
                        .alignX(Alignment.CENTER)));
        panel.bindPlayerInventory();
        return panel;
    }
}
