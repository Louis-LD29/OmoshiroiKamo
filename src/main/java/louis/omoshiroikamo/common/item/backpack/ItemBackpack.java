package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.client.gui.BackpackGui;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.item.ItemBauble;
import louis.omoshiroikamo.common.util.lib.LibMods;

public class ItemBackpack extends ItemBauble implements IGuiHolder<PlayerInventoryGuiData> {

    static BackpackMagnetController controller = new BackpackMagnetController();

    public static BackpackGui gui;

    public static ItemBackpack create() {
        ItemBackpack item = new ItemBackpack();
        item.init();
        return item;
    }

    public ItemBackpack() {
        super(ModObject.itemBackPack.unlocalisedName);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(OKCreativeTab.INSTANCE);
        disableRightClickEquip();
    }

    @Override
    protected void init() {
        GameRegistry.registerItem(this, name);
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

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advanced) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            list.add(tag.toString());
        } else {
            list.add("No data");
        }
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

}
