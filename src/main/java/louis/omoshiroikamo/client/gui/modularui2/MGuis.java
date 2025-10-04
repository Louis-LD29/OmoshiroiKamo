package louis.omoshiroikamo.client.gui.modularui2;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import louis.omoshiroikamo.common.block.abstractClass.AbstractIOTE;
import louis.omoshiroikamo.common.block.abstractClass.AbstractTE;

public class MGuis {

    public static MGuiBuilder mteTemplatePanelBuilder(AbstractIOTE te, PosGuiData data, PanelSyncManager syncManager,
        UISettings uiSettings) {
        return new MGuiBuilder(te, data, syncManager, uiSettings);
    }

    public static ModularPanel createPopUpPanel(@Nonnull String name) {
        return createPopUpPanel(name, false, false);
    }

    public static ModularPanel createPopUpPanel(@Nonnull String name, boolean disablePanelsBelow,
        boolean closeOnOutOfBoundsClick) {
        return new MPopUpPanel(name, disablePanelsBelow, closeOnOutOfBoundsClick);
    }

    public static void open(EntityPlayer player, AbstractTE entity) {
        GuiFactories.tileEntity()
            .open(player, entity.xCoord, entity.yCoord, entity.zCoord);
    }
}
