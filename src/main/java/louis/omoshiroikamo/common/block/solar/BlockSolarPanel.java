package louis.omoshiroikamo.common.block.solar;

import java.util.List;

import louis.omoshiroikamo.common.block.abstractClass.AbstractMachineBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.cleanroommc.modularui.factory.GuiFactories;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.client.IAdvancedTooltipProvider;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.core.lib.LibResources;

public class BlockSolarPanel extends AbstractMachineBlock<TileSolarPanel> {

    protected BlockSolarPanel() {
        super(ModObject.blockSolar, TileSolarPanel.class);
    }

    public static BlockSolarPanel create() {
        BlockSolarPanel res = new BlockSolarPanel();
        res.init();
        return res;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public TileEntity createTileEntity(World world, int i) {
        return new TileSolarPanel();
    }

    @Override
    protected String getMachineFrontIconKey(boolean active) {
        return LibResources.PREFIX_MOD + "solarPanelFront";
    }

}
