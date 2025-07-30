package louis.omoshiroikamo.common.block.multiblock.part.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class TEItemOutput extends TEItemInOut {

    protected TEItemOutput(int meta) {
        super(MaterialRegistry.fromMeta(meta % LibResources.META1));
    }

    public TEItemOutput() {
        this(0);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockItemInOut.unlocalisedName;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int slot, ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        openGui(player);
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        int slotCount = inv.getSlots();
        syncManager.registerSlotGroup("item_inv", slotCount);

        ParentWidget<?> parent = new ParentWidget<>();
        parent.align(Alignment.TopLeft); // Giữ canh lề trái trên

        int slotSize = 18;
        int slotsPerRow = 9;
        int paddingX = 7;
        int paddingY = 7;

        for (int i = 0; i < slotCount; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;

            int x = paddingX + col * slotSize;
            int y = paddingY + row * slotSize;

            int finalI = i;
            parent.child(
                new ItemSlot().slot(
                    new ModularSlot(inv, finalI).slotGroup("item_inv")
                        .filter(itemStack -> isMachineItemValidForSlot(finalI, itemStack)))
                    .pos(x, y));
        }

        return ModularPanel.defaultPanel(getMachineName())
            .child(parent)
            .bindPlayerInventory();
    }

    public ItemStackHandler getInv() {
        return inv;
    }

}
