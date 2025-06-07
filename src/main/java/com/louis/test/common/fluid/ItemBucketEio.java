package com.louis.test.common.fluid;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import org.apache.commons.lang3.StringUtils;

import com.louis.test.common.TestCreativeTab;

import cpw.mods.fml.common.registry.GameRegistry;
import crazypants.enderio.fluid.BucketHandler;

public class ItemBucketEio extends ItemBucket {

    public static ItemBucketEio create(Fluid fluid) {
        ItemBucketEio b = new ItemBucketEio(fluid.getBlock() != null ? fluid.getBlock() : Blocks.air, fluid.getName());
        b.init();

        FluidContainerRegistry.registerFluidContainer(fluid, new ItemStack(b), new ItemStack(Items.bucket));
        BucketHandler.instance.registerFluid(fluid.getBlock(), b);

        return b;
    }

    private String fluidName;

    protected ItemBucketEio(Block block, String fluidName) {
        super(block);
        this.fluidName = fluidName;
        setCreativeTab(TestCreativeTab.INSTANCE);
        setContainerItem(Items.bucket);
        String str = "bucket" + StringUtils.capitalize(fluidName);
        setUnlocalizedName(str);
        setTextureName("enderio:" + str);
    }

    protected void init() {
        GameRegistry.registerItem(this, "bucket" + StringUtils.capitalize(fluidName));
    }
}
