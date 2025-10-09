package louis.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemStackUpgrade extends ItemUpgrade {

    @SideOnly(Side.CLIENT)
    protected IIcon tier1, tier2, tier3, tier4;

    public static ItemStackUpgrade create() {
        ItemStackUpgrade item = new ItemStackUpgrade();
        item.init();
        return item;
    }

    public ItemStackUpgrade() {
        super(ModObject.itemStackUpgrade.unlocalisedName);
        setNoRepair();
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        switch (meta) {
            case 1:
                return super.getUnlocalizedName(stack) + ".Gold";
            case 2:
                return super.getUnlocalizedName(stack) + ".Diamond";
            case 3:
                return super.getUnlocalizedName(stack) + ".Netherite";
            default:
                return super.getUnlocalizedName(stack) + ".Iron";
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        switch (meta) {
            case 1:
                return tier2;
            case 2:
                return tier3;
            case 3:
                return tier4;
            default:
                return tier1;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg) {
        tier1 = reg.registerIcon(LibResources.PREFIX_MOD + "stack_upgrade_tier_1");
        tier2 = reg.registerIcon(LibResources.PREFIX_MOD + "stack_upgrade_tier_2");
        tier3 = reg.registerIcon(LibResources.PREFIX_MOD + "stack_upgrade_tier_3");
        tier4 = reg.registerIcon(LibResources.PREFIX_MOD + "stack_upgrade_tier_4");
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LibMisc.lang.localize(LibResources.TOOLTIP + "stack_multiplier", multiplier(itemstack)));
    }

    public int multiplier(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 1:
                return 4;
            case 2:
                return 8;
            case 3:
                return 16;
            default:
                return 2;
        }
    }
}
