package louis.omoshiroikamo.common.block.solarArray;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.abstractClass.AbstractMachineBlock;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockSolarArray extends AbstractMachineBlock<TESolarArray> {

    protected BlockSolarArray() {
        super(ModObject.blockSolarArray, TESolarArray.class);
    }

    public static BlockSolarArray create() {
        BlockSolarArray res = new BlockSolarArray();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockSolarArray.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(teClass, modObject.unlocalisedName + "TileEntity");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new TESolarArray();
    }

    @Override
    protected String getMachineFrontIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "solarPanelFront";
    }

}
