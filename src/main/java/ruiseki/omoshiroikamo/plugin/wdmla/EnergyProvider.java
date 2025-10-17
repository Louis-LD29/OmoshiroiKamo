package ruiseki.omoshiroikamo.plugin.wdmla;

import static com.gtnewhorizons.wdmla.impl.ui.component.TooltipComponent.DEFAULT_PROGRESS_DESCRIPTION_PADDING;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.wdmla.api.accessor.BlockAccessor;
import com.gtnewhorizons.wdmla.api.provider.IBlockComponentProvider;
import com.gtnewhorizons.wdmla.api.ui.ColorPalette;
import com.gtnewhorizons.wdmla.api.ui.ITooltip;
import com.gtnewhorizons.wdmla.impl.ui.component.ProgressComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.TextComponent;
import com.gtnewhorizons.wdmla.impl.ui.component.VPanelComponent;
import com.gtnewhorizons.wdmla.impl.ui.style.ProgressStyle;
import com.gtnewhorizons.wdmla.impl.ui.style.TextStyle;

import cofh.api.energy.IEnergyReceiver;
import ruiseki.omoshiroikamo.api.IWailaInfoProvider;
import ruiseki.omoshiroikamo.common.block.abstractClass.AbstractTE;

public enum EnergyProvider implements IBlockComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor) {
        TileEntity tileEntity = accessor.getTileEntity();
        if (!(tileEntity instanceof AbstractTE te)) {
            return;
        }
        if (!(tileEntity instanceof IEnergyReceiver handler)) {
            return;
        }
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return;
        }
        if (!provider.hasEnergyStorage()) {
            return;
        }

        int stored = handler.getEnergyStored(ForgeDirection.UNKNOWN);
        int maxStored = handler.getMaxEnergyStored(ForgeDirection.UNKNOWN);

        if (maxStored > 0) {

            tooltip.child(
                new ProgressComponent(stored, maxStored)
                    .style(new ProgressStyle().color(ColorPalette.ENERGY_FILLED, ColorPalette.ENERGY_FILLED_ALTERNATE))
                    .child(
                        new VPanelComponent().padding(DEFAULT_PROGRESS_DESCRIPTION_PADDING)
                            .child(
                                new TextComponent(String.format("%,d / %,d RF", stored, maxStored))
                                    .style(new TextStyle().color(0xFFFFFF)))));
        }
        if (accessor.showDetails() && te.getMaterial() != null) {
            tooltip.child(
                new TextComponent(
                    String.format(
                        "Voltage Tier: %s",
                        te.getMaterial()
                            .getVoltageTier().displayName)).style(new TextStyle().color(0xFFFFFF)));

            tooltip.child(
                new TextComponent(
                    String.format(
                        "Voltage: %,d V",
                        te.getMaterial()
                            .getMaxVoltage())).style(new TextStyle().color(0xFFFFFF)));

            tooltip.child(
                new TextComponent(
                    String.format(
                        "Max Transfer: %,d RF/t",
                        te.getMaterial()
                            .getMaxPowerTransfer())).style(new TextStyle().color(0xFFFFFF)));

            tooltip.child(
                new TextComponent(
                    String.format(
                        "Max Usage: %,d RF/t",
                        te.getMaterial()
                            .getMaxPowerUsage())).style(new TextStyle().color(0xFFFFFF)));
        } else if (te.getMaterial() != null) {
            tooltip.child(new TextComponent("§7(Hold §eShift§7 for details)"));
        }

    }

    @Override
    public ResourceLocation getUid() {
        return ModWDMlaPlugin.Uid("te_energy");
    }

}
