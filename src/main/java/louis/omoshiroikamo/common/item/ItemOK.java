package louis.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemOK extends Item implements IAdvancedTooltipProvider {

    protected final String name;

    public ItemOK(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    protected void init() {
        GameRegistry.registerItem(this, name);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibResources.PREFIX_MOD + name);
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }
}
