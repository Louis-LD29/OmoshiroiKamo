package louis.omoshiroikamo.common.block.multiblock.part.energy;

import java.util.List;

import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.common.core.lib.LibResources;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.client.IResourceTooltipProvider;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.AbstractBlock;

public class BlockEnergyInOut extends AbstractBlock<TEEnergyInOut> implements IResourceTooltipProvider {

    protected BlockEnergyInOut() {
        super(ModObject.blockEnergyInOut, TEEnergyInOut.class);
    }

    public static BlockEnergyInOut create() {
        BlockEnergyInOut res = new BlockEnergyInOut();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockEnergyInOut.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(TEEnergyInput.class, modObject.unlocalisedName + "TEEnergyInput");
        GameRegistry.registerTileEntity(TEEnergyOutput.class, modObject.unlocalisedName + "TEEnergyOutput");
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
        return getUnlocalizedName();
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
            return new TEEnergyOutput(meta);
        } else {
            return new TEEnergyInput(meta);
        }
    }
}
