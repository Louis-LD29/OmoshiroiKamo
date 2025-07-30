package louis.omoshiroikamo.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemOK extends Item {

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
}
