package louis.omoshiroikamo.client.gui.modularui2.widgets;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.TabTexture;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeSelectable;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.PagedWidget;

public class BackPackPageButton extends Widget<BackPackPageButton> implements Interactable {

    private final int index;
    private final PagedWidget.Controller controller;
    private IDrawable inactiveTexture = null;
    private boolean invert = false;

    private boolean playClickSound = true;
    private Runnable clickSound;
    private IGuiAction.MousePressed mousePressed;
    private InteractionSyncHandler syncHandler;

    public BackPackPageButton(int index, PagedWidget.Controller controller) {
        this.index = index;
        this.controller = controller;
        disableHoverBackground();
    }

    @Override
    public boolean isValidSyncHandler(SyncHandler syncHandler) {
        this.syncHandler = castIfTypeElseNull(syncHandler, InteractionSyncHandler.class);
        return this.syncHandler != null;
    }

    @Override
    public WidgetTheme getWidgetThemeInternal(ITheme theme) {
        WidgetThemeSelectable widgetTheme = theme.getToggleButtonTheme();
        return isActive() ^ invertSelected() ? widgetTheme : widgetTheme.getSelected();
    }

    public void playClickSound() {
        if (this.playClickSound) {
            if (this.clickSound != null) {
                this.clickSound.run();
            } else {
                Interactable.playButtonClickSound();
            }
        }
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (!isActive()) {
            if (this.mousePressed != null && this.mousePressed.press(mouseButton)) {
                this.controller.setPage(this.index);
                playClickSound();
                return Result.SUCCESS;
            }
            if (this.syncHandler != null && this.syncHandler.onMousePressed(mouseButton)) {
                this.controller.setPage(this.index);
                Interactable.playButtonClickSound();
                return Result.SUCCESS;
            }
        }
        return Result.ACCEPT;
    }

    @Override
    public IDrawable getBackground() {
        return isActive() || this.inactiveTexture == null ? super.getBackground() : this.inactiveTexture;
    }

    public boolean isActive() {
        return this.controller.getActivePageIndex() == this.index;
    }

    public BackPackPageButton background(boolean active, IDrawable... background) {
        if (active) {
            return background(background);
        }
        if (background.length == 0) {
            this.inactiveTexture = null;
        } else if (background.length == 1) {
            this.inactiveTexture = background[0];
        } else {
            this.inactiveTexture = new DrawableStack(background);
        }
        return this;
    }

    public BackPackPageButton tab(TabTexture texture, int location) {
        return background(invertSelected(), texture.get(location, invertSelected()))
            .background(!invertSelected(), texture.get(location, !invertSelected()))
            .disableHoverBackground()
            .size(texture.getWidth(), texture.getHeight());
    }

    public BackPackPageButton invertSelected(boolean invert) {
        this.invert = invert;
        return getThis();
    }

    public boolean invertSelected() {
        return this.invert;
    }

    public BackPackPageButton onMousePressed(IGuiAction.MousePressed mousePressed) {
        this.mousePressed = mousePressed;
        return getThis();
    }

    public BackPackPageButton syncHandler(InteractionSyncHandler interactionSyncHandler) {
        this.syncHandler = interactionSyncHandler;
        setSyncHandler(interactionSyncHandler);
        return getThis();
    }

    public BackPackPageButton playClickSound(boolean play) {
        this.playClickSound = play;
        return getThis();
    }

    public BackPackPageButton clickSound(Runnable clickSound) {
        this.clickSound = clickSound;
        return getThis();
    }
}
