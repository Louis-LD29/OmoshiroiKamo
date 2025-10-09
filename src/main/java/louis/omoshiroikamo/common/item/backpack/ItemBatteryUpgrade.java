package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemBatteryUpgrade extends ItemUpgrade {

    public static ItemBatteryUpgrade create() {
        ItemBatteryUpgrade item = new ItemBatteryUpgrade();
        item.init();
        return item;
    }

    public ItemBatteryUpgrade() {
        super(ModObject.itemBatteryUpgrade.unlocalisedName);
        setNoRepair();
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibResources.PREFIX_MOD + "battery_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LibMisc.lang.localize(LibResources.TOOLTIP + "crafting_upgrade"));
    }
}
