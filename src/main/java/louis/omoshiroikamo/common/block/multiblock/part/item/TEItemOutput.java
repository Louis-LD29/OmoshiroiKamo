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

public class TEItemOutput extends TEItemInOut {

    protected ItemStackHandler inv;

    protected TEItemOutput(int meta) {
        super(MaterialRegistry.fromMeta(meta));
        inv = new ItemStackHandler(material.getItemSlotCount());
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
        return true;
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
        parent.align(Alignment.TopCenter);

        int slotSize = 18;
        int totalWidth = slotCount * slotSize;
        int startX = -totalWidth / 2;

        for (int i = 0; i < slotCount; i++) {
            int x = startX + i * slotSize;
            parent.child(
                new ItemSlot().slot(new ModularSlot(inv, i).slotGroup("item_inv"))
                    .pos(x, 0));
        }

        return ModularPanel.defaultPanel(getMachineName())
            .child(parent)
            .bindPlayerInventory();
    }

    public ItemStackHandler getInv() {
        return inv;
    }

}
