package ruiseki.omoshiroikamo.plugin.wdmla;

import java.util.Arrays;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.enderio.core.api.common.util.IProgressTile;
import com.gtnewhorizons.wdmla.api.accessor.Accessor;
import com.gtnewhorizons.wdmla.api.provider.IClientExtensionProvider;
import com.gtnewhorizons.wdmla.api.provider.IServerExtensionProvider;
import com.gtnewhorizons.wdmla.api.view.ClientViewGroup;
import com.gtnewhorizons.wdmla.api.view.ProgressView;
import com.gtnewhorizons.wdmla.api.view.ViewGroup;
import com.gtnewhorizons.wdmla.impl.ui.ThemeHelper;
import com.gtnewhorizons.wdmla.util.FormatUtil;

import ruiseki.omoshiroikamo.api.IWailaInfoProvider;

public enum BlockProcessProvider
    implements IServerExtensionProvider<ProgressView.Data>, IClientExtensionProvider<ProgressView.Data, ProgressView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return ModWDMlaPlugin.Uid("te_process");
    }

    @Override
    public List<ClientViewGroup<ProgressView>> getClientGroups(Accessor accessor,
        List<ViewGroup<ProgressView.Data>> viewGroups) {
        Object target = accessor.getTarget();
        if (!(target instanceof TileEntity tileEntity)) {
            return null;
        }
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return null;
        }
        if (!provider.hasProcessStatus()) {
            return null;
        }
        if (!accessor.showDetails()) {
            return null;
        }
        return ClientViewGroup.map(viewGroups, ProgressView::read, (group, clientGroup) -> {
            ProgressView view = clientGroup.views.get(0);
            view.description = ThemeHelper.INSTANCE.value(
                StatCollector.translateToLocal("hud.msg.wdmla.progress"),
                FormatUtil.PERCENTAGE_STANDARD.format((float) view.progress / 100));
            view.hasScale = true;
        });
    }

    @Override
    public List<ViewGroup<ProgressView.Data>> getGroups(Accessor accessor) {
        Object target = accessor.getTarget();
        if (!(target instanceof TileEntity tileEntity)) {
            return null;
        }
        if (!(tileEntity instanceof IWailaInfoProvider provider)) {
            return null;
        }
        if (!provider.hasProcessStatus()) {
            return null;
        }

        if (tileEntity instanceof IProgressTile handler) {
            float progress = handler.getProgress();
            if (progress <= 0) {
                return null;
            }

            ProgressView.Data progressData = new ProgressView.Data((long) (progress * 100), 100);
            ViewGroup<ProgressView.Data> group = new ViewGroup<>(Arrays.asList(progressData));
            return Arrays.asList(group);
        }
        return null;
    }

}
