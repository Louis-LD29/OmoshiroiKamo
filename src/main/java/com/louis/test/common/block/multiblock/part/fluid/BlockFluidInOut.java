package com.louis.test.common.block.multiblock.part.fluid;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.louis.test.api.MaterialRegistry;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.IResourceTooltipProvider;
import com.louis.test.common.block.AbstractBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidInOut extends AbstractBlock<TEFluidInOut> implements IResourceTooltipProvider {

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
        GameRegistry.registerTileEntity(TEFluidInput.class, modObject.unlocalisedName + "TileEntityInput");
        GameRegistry.registerTileEntity(TEFluidOutput.class, modObject.unlocalisedName + "TileEntityOutput");
    }

    @Override
    public String getUnlocalizedNameForTooltip(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        int count = MaterialRegistry.all()
            .size();
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, i));
        }
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, 100 + i));
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
