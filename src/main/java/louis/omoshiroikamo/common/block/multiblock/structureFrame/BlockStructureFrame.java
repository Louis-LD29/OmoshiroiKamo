package louis.omoshiroikamo.common.block.multiblock.structureFrame;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.BlockOK;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockStructureFrame extends BlockOK {

    public static String[] blocks = new String[] { "basalt_structure_1", "basalt_structure_2", "basalt_structure_3",
        "basalt_structure_4", "hardened_stone_structure_1", "hardened_stone_structure_2", "hardened_stone_structure_3",
        "hardened_stone_structure_4" };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public static BlockStructureFrame create() {
        BlockStructureFrame result = new BlockStructureFrame();
        result.init();
        return result;
    }

    private BlockStructureFrame() {
        super(ModObject.blockStructureFrame, null, net.minecraft.block.material.Material.iron);
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockStructureFrame.class, ModObject.blockStructureFrame.unlocalisedName);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < blocks.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        icons = new IIcon[blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            String iconName = LibResources.PREFIX_MOD + blocks[i];
            icons[i] = reg.registerIcon(iconName);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (icons == null || meta < 0 || meta >= icons.length) {
            return blockIcon;
        }
        return icons[meta];
    }
}
