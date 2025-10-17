package ruiseki.omoshiroikamo.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.common.OKCreativeTab;

public class ItemBlockOK extends ItemBlock {

    private Block block2;

    public ItemBlockOK(Block block) {
        super(block);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockOK(Block block1, Block block2) {
        super(block1);
        this.block2 = block2;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.block2.getIcon(2, meta);
    }

    public int getMetadata(int meta) {
        return meta;
    }

}
