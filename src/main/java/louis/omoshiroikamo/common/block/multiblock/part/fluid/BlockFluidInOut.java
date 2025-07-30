package louis.omoshiroikamo.common.block.multiblock.part.fluid;

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
import louis.omoshiroikamo.common.network.PacketFluidTanks;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class BlockFluidInOut extends AbstractBlock<TEFluidInOut> {

    static {
        PacketHandler.INSTANCE
            .registerMessage(PacketFluidTanks.class, PacketFluidTanks.class, PacketHandler.nextID(), Side.SERVER);
    }

    protected BlockFluidInOut() {
        super(ModObject.blockFluidInOut, TEFluidInOut.class);
    }

    public static BlockFluidInOut create() {
        BlockFluidInOut res = new BlockFluidInOut();
        res.init();
        return res;
    }

    @Override
    protected void init() {
        GameRegistry.registerBlock(this, ItemBlockFluidInOut.class, modObject.unlocalisedName);
        GameRegistry.registerTileEntity(TEFluidInput.class, modObject.unlocalisedName + "TEFluidInput");
        GameRegistry.registerTileEntity(TEFluidOutput.class, modObject.unlocalisedName + "TEFluidOutput");
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
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
            return new TEFluidOutput(meta);
        } else {
            return new TEFluidInput(meta);
        }
    }
}
