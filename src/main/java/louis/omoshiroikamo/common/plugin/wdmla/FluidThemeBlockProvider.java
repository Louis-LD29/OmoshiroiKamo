package louis.omoshiroikamo.common.plugin.wdmla;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;

import louis.omoshiroikamo.api.fluid.IFluidHandlerAdv;
import louis.omoshiroikamo.api.fluid.SmartTank;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;
import louis.omoshiroikamo.common.core.lib.LibMisc;

public enum FluidThemeBlockProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof IFluidHandlerAdv handler)) return;

        AbstractTE te = tileEntity instanceof AbstractTE at ? at : null;
        SmartTank[] tanks = handler.getTanks();
        if (tanks == null || tanks.length == 0) return;

        if (te != null && te.getMaterial() != null) {
            if (accessor.showDetails()) {
                int meltPointK = (int) te.getMaterial()
                    .getMeltingPointK();
                tooltip.child(ThemeHelper.INSTANCE.info(String.format("Melting Point: %d K", meltPointK)));
            } else {
                tooltip.child(new TextComponent("§7(Hold §eShift§7 for details)"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(LibMisc.MOD_ID.toLowerCase(), "te_fluid_theme_block");
    }
}
