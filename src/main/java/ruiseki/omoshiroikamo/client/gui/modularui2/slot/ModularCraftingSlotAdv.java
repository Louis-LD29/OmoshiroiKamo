package ruiseki.omoshiroikamo.client.gui.modularui2.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.InventoryCraftingWrapper;
import com.cleanroommc.modularui.widgets.slot.ModularCraftingSlot;

import cpw.mods.fml.common.FMLCommonHandler;

public class ModularCraftingSlotAdv extends ModularCraftingSlot {

    private InventoryCraftingWrapper craftMatrix;

    public ModularCraftingSlotAdv(IItemHandler itemHandler, int index) {
        super(itemHandler, index);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        FMLCommonHandler.instance()
            .firePlayerCraftingEvent(player, stack, craftMatrix);
        onCrafting(stack);
        for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
            ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
            if (itemstack1 != null) {
                this.craftMatrix.decrStackSize(i, 1);
                if (itemstack1.getItem()
                    .hasContainerItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem()
                        .getContainerItem(itemstack1);
                    if (itemstack2 != null && itemstack2.isItemStackDamageable()
                        && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, itemstack2));
                        continue;
                    }
                    if (!itemstack1.getItem()
                        .doesContainerItemLeaveCraftingGrid(itemstack1)
                        || !player.inventory.addItemStackToInventory(itemstack2)) {
                        if (this.craftMatrix.getStackInSlot(i) == null) {
                            this.craftMatrix.setInventorySlotContents(i, itemstack2);
                        } else {
                            player.dropPlayerItemWithRandomChoice(itemstack2, false);
                        }
                    }
                }
            }
        }
        this.craftMatrix.notifyContainer();
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        boolean allEmpty = true;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = craftMatrix.getStackInSlot(i);
            if (stack != null) {
                allEmpty = false;
                break;
            }
        }

        if (allEmpty) {
            return false;
        }
        return super.canTakeStack(player);
    }

    @Override
    public void setCraftMatrix(InventoryCraftingWrapper craftMatrix) {
        this.craftMatrix = craftMatrix;
    }
}
