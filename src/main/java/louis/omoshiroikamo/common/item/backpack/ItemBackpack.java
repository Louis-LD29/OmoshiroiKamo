package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cofh.api.energy.IEnergyContainerItem;
import louis.omoshiroikamo.api.energy.PowerDisplayUtil;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.entity.EntityImmortalItem;
import louis.omoshiroikamo.common.item.ItemBauble;
import louis.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import louis.omoshiroikamo.common.util.lib.LibMods;

public class ItemBackpack extends ItemBauble implements IEnergyContainerItem, IGuiHolder<PlayerInventoryGuiData> {

    public ItemBackpack() {
        super(ModObject.itemBackPack.unlocalisedName);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setNoRepair();
        disableRightClickEquip();
    }

    public static ItemBackpack create() {
        ItemBackpack item = new ItemBackpack();
        item.init();
        return item;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);
        String type;
        if (meta == 1) {
            type = "Copper";
        } else if (meta == 2) {
            type = "Iron";
        } else if (meta == 3) {
            type = "Gold";
        } else if (meta == 4) {
            type = "Diamond";
        } else if (meta == 5) {
            type = LibMods.EtFuturum.isLoaded() ? "Netherite" : "Obsidian";
        } else {
            type = "Starter";
        }
        return base + "." + type;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote && !player.isSneaking()) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    public int getBackpackRow(int meta) {
        switch (meta) {
            case 1:
                return 4;
            case 2:
                return 6;
            case 3:
                return 9;
            case 4:
                return 9;
            case 5:
                return 10;
            default:
                return 3;
        }
    }

    public int getBackpackCol(int meta) {
        switch (meta) {
            case 4:
                return 12;
            case 5:
                return 12;
            default:
                return 9;
        }
    }

    public int getUpgradeRow(int meta) {
        switch (meta) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 7;
            default:
                return 1;
        }
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new BackpackGui(data.getPlayer(), data, syncManager, settings, this);
    }

    // Energy
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return EnergyUpgrade.receiveEnergy(container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return EnergyUpgrade.extractEnergy(container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return EnergyUpgrade.getEnergyStored(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return EnergyUpgrade.getMaxEnergyStored(container);
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        EnergyUpgrade up = EnergyUpgrade.loadFromItem(itemstack);
        if (up != null) {
            list.add(PowerDisplayUtil.formatStoredPower(up.getEnergy(), up.getCapacity()));
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack stack) {
        return new EntityImmortalItem(world, location, stack);
    }
}
