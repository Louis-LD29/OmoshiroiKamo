package ruiseki.omoshiroikamo.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.fluid.FluidMaterialRegister;
import ruiseki.omoshiroikamo.common.fluid.FluidRegister;
import ruiseki.omoshiroikamo.common.item.ModItems;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;

public class OKCreativeTab extends CreativeTabs {

    public static final CreativeTabs INSTANCE = new OKCreativeTab();
    List<ItemStack> list;
    private static final List<ItemStack> externalStacks = new ArrayList<>();

    public OKCreativeTab() {
        super(LibMisc.MOD_ID);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModItems.itemMaterial);
    }

    @Override
    public Item getTabIconItem() {
        return getIconItemStack().getItem();
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> list) {
        this.list = list;

        addItem(ModItems.itemMaterial);
        addItem(ModItems.itemWireCoil);
        addItem(ModItems.itemOre);
        addItem(ModItems.itemHammer);
        addItem(ModItems.itemOperationOrb);
        addItem(ModItems.itemBackPack);
        addItem(ModItems.itemUpgrade);
        addItem(ModItems.itemStackUpgrade);
        addItem(ModItems.itemCraftingUpgrade);
        addItem(ModItems.itemMagnetUpgrade);
        addItem(ModItems.itemFeedingUpgrade);
        addItem(ModItems.itemBatteryUpgrade);
        addItem(ModItems.itemEverlastingUpgrade);
        addItem(ModItems.itemStabilizedEnderPear);
        addItem(ModItems.itemPhotovoltaicCell);
        addItem(ModItems.itemAssembler);
        addItem(FluidMaterialRegister.itemBucketMaterial);
        addItem(FluidRegister.itemBucketFluid);

        addBlock(ModBlocks.blockMaterial);
        addBlock(ModBlocks.blockAnvil);
        addBlock(ModBlocks.blockFurnace);
        addBlock(ModBlocks.blockBasalt);
        addBlock(ModBlocks.blockAlabaster);
        addBlock(ModBlocks.blockHardenedStone);
        addBlock(ModBlocks.blockMica);
        addBlock(ModBlocks.blockStructureFrame);
        addBlock(ModBlocks.blockMachineBase);
        addBlock(ModBlocks.blockModifierNull);
        addBlock(ModBlocks.blockModifierAccuracy);
        addBlock(ModBlocks.blockModifierSpeed);
        addBlock(ModBlocks.blockModifierPiezo);
        addBlock(ModBlocks.blockSolarArray);
        addBlock(ModBlocks.blockSolarCell);
        addBlock(ModBlocks.blockVoidOreMiner);
        addBlock(ModBlocks.blockVoidResMiner);
        addBlock(ModBlocks.blockNanoBotBeacon);
        addBlock(ModBlocks.blockLaserCore);
        addBlock(ModBlocks.blockLaserLens);

        for (ItemStack stack : externalStacks) {
            addStack(stack);
        }
    }

    private void addItem(Item item) {
        item.getSubItems(item, this, list);
    }

    private void addBlock(Block block) {
        ItemStack stack = new ItemStack(block);
        block.getSubBlocks(stack.getItem(), this, list);
    }

    private void addStack(ItemStack stack) {
        list.add(stack);
    }

    public static void addToTab(Item item) {
        externalStacks.add(new ItemStack(item));
    }

    public static void addToTab(Block block) {
        externalStacks.add(new ItemStack(block));
    }

    public static void addToTab(ItemStack stack) {
        externalStacks.add(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTabLabel() {
        return LibMisc.MOD_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return LibMisc.MOD_NAME;
    }
}
