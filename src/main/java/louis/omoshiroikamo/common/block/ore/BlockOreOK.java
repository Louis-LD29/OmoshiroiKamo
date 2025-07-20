package louis.omoshiroikamo.common.block.ore;

import static org.apache.commons.lang3.StringUtils.capitalize;

import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.ore.OreEntry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class BlockOreOK extends BlockOre {

    private final OreEntry ore;
    private IIcon icon;

    public BlockOreOK(OreEntry ore) {
        super();
        this.ore = ore;
        setHardness(3.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 1);
        String name = "ore_" + ore.getUnlocalizedName();
        setBlockName(name);
        setCreativeTab(OKCreativeTab.INSTANCE);
        OreDictionary.registerOre("ore" + capitalize(name), new ItemStack(Item.getItemFromBlock(this), 1, 0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        icon = reg.registerIcon(LibResources.PREFIX_MOD + "ore_" + ore.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    public OreEntry getOreEntry() {
        return ore;
    }
}
