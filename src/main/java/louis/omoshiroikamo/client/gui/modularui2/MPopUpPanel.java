package louis.omoshiroikamo.client.gui.modularui2;

import javax.annotation.Nonnull;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.widgets.ButtonWidget;

public class MPopUpPanel extends ModularPanel {

    private final boolean disablePanelsBelow;
    private final boolean closeOnOutOfBoundsClick;

    public MPopUpPanel(@Nonnull String name, boolean disablePanelsBelow, boolean closeOnOutOfBoundsClick) {
        super(name);
        this.disablePanelsBelow = disablePanelsBelow;
        this.closeOnOutOfBoundsClick = closeOnOutOfBoundsClick;
        child(ButtonWidget.panelCloseButton());
    }

    @Override
    public boolean disablePanelsBelow() {
        return disablePanelsBelow;
    }

    @Override
    public boolean closeOnOutOfBoundsClick() {
        return closeOnOutOfBoundsClick;
    }
}
