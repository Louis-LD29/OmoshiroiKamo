package ruiseki.omoshiroikamo.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.enderio.core.api.client.gui.IAdvancedTooltipProvider;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.OKCreativeTab;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class ItemOK extends Item implements IAdvancedTooltipProvider {

    protected final String name;
    protected String textureName;

    public ItemOK(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemOK(String name, String textureName) {
        this.name = name;
        this.textureName = textureName;
        setUnlocalizedName(name);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public void init() {
        GameRegistry.registerItem(this, name);
    }

    public static ItemOK create(String name) {
        ItemOK mat = new ItemOK(name);
        mat.init();
        return mat;
    }

    public static ItemOK create(ModObject modObject, String textureName) {
        return new ItemOK(modObject.unlocalisedName, textureName);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        if (textureName != null) {
            itemIcon = register.registerIcon(LibResources.PREFIX_MOD + textureName);
        } else {
            itemIcon = register.registerIcon(LibResources.PREFIX_MOD + name);
        }
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
