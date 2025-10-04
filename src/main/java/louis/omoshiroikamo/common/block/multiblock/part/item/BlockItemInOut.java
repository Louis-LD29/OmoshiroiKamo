package louis.omoshiroikamo.common.block.multiblock.part.item;

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
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.abstractClass.AbstractBlock;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.TEFluidInput;
import louis.omoshiroikamo.common.block.multiblock.part.fluid.TEFluidOutput;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockItemInOut extends AbstractBlock<TEItemInOut> {

    protected BlockItemInOut() {
        super(ModObject.blockItemInOut, TEItemInOut.class);
    }

    public static BlockItemInOut create() {
        BlockItemInOut res = new BlockItemInOut();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockItemInOut.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(TEFluidInput.class, modObject.unlocalisedName + "TEItemInput");
        GameRegistry.registerTileEntity(TEFluidOutput.class, modObject.unlocalisedName + "TEItemOutput");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            list.add(new ItemStack(this, 1, meta));;
            list.add(new ItemStack(this, 1, LibResources.META1 + meta));
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        if (meta >= 100) {
            return new TEItemOutput(meta);
        } else {
            return new TEItemInput(meta);
        }
    }
}
