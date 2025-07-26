package louis.omoshiroikamo.common.block.multiblock.part.fluid;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.core.lib.LibMisc;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class ItemBlockFluidInOut extends ItemBlockWithMetadata implements IAdvancedTooltipProvider {

    public ItemBlockFluidInOut() {
        super(ModBlocks.blockFluidInOut, ModBlocks.blockFluidInOut);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.INSTANCE);
    }

    public ItemBlockFluidInOut(Block block) {
        super(block, block);
        setHasSubtypes(true);
        setCreativeTab(OKCreativeTab.tabBlock);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        boolean isOutput = meta >= LibResources.META1;
        MaterialEntry material = MaterialRegistry.fromMeta(meta % LibResources.META1);

        String base = super.getUnlocalizedName(stack);
        String type = isOutput ? "output" : "input";
        String mat = material.getUnlocalizedName();

        return base + "." + type + "." + mat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            list.add(new ItemStack(this, 1, meta));;
            list.add(new ItemStack(this, 1, LibResources.META1 + meta));
        }
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer player, List<String> list, boolean advanced) {
        int meta = itemstack.getItemDamage();
        MaterialEntry material = MaterialRegistry.fromMeta(meta % LibResources.META1);

        list.add(String.format("§7Material:§f %s", material.getName()));
        list.add(String.format("§7Volume:§f %,dL", material.getVolumeMB()));
        list.add(String.format("§7Melting Point:§f %d K", (int) material.getMeltingPointK()));

        NBTTagCompound tag = itemstack.getTagCompound();
        if (tag != null && tag.hasKey("tank")) {
            FluidStack fl = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("tank"));
            if (fl != null && fl.getFluid() != null) {
                list.add(
                    String.format(
                        "§7Stored:§f %,dL %s",
                        fl.amount,
                        fl.getFluid()
                            .getName()));
            }
        } else {
            list.add("§7Stored:§f " + LibMisc.lang.localize("fluid.empty"));
        }
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {}
}
