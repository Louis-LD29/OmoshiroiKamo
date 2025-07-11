package com.louis.test.common.block.multiblock.part.energy;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import com.louis.test.api.MaterialEntry;
import com.louis.test.api.MaterialRegistry;
import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.ModBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockEnergyInOut extends ItemBlockWithMetadata implements IAdvancedTooltipProvider {

    public ItemBlockEnergyInOut() {
        super(ModBlocks.blockEnergyInOut, ModBlocks.blockEnergyInOut);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    public ItemBlockEnergyInOut(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(TestCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        boolean isOutput = meta >= 100;
        MaterialEntry material = MaterialRegistry.fromMeta(meta % 100);

        String base = super.getUnlocalizedName(stack);
        String type = isOutput ? "output" : "input";
        String mat = material.name.toLowerCase(Locale.ROOT)
            .replace(" ", "_");

        return base + "." + type + "." + mat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        int count = MaterialRegistry.all()
            .size();
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(this, 1, i));
            list.add(new ItemStack(this, 1, 100 + i));
        }
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer player, List<String> list, boolean flag) {
        int meta = itemstack.getItemDamage();
        MaterialEntry material = MaterialRegistry.fromMeta(meta);

        list.add(String.format("§7Material:§f %s", material.name));
        list.add(
            String.format(
                "§7Voltage Tier:§f %s",
                material.getVoltageTier()
                    .getDisplayName()));
        list.add(String.format("§7Voltage:§f %d V", material.getMaxVoltage()));
        list.add(String.format("§7Capacity:§f %,d RF", material.getEnergyStorageCapacity()));
        list.add(String.format("§7Max Transfer:§f %,d RF/t", material.getMaxPowerTransfer()));
        list.add(String.format("§7Max Usage:§f %,d RF/t", material.getMaxPowerUsage()));
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}
}
