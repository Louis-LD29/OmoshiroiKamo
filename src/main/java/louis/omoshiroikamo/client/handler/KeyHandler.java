package louis.omoshiroikamo.client.handler;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.utils.Platform;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibMods;

public class KeyHandler {

    public static final KeyHandler instance = new KeyHandler();

    private final KeyBinding keyOpenBackpack;

    private KeyHandler() {
        keyOpenBackpack = new KeyBinding(
            LibMisc.lang.localize("keybind.backpackOpenToggle"),
            Keyboard.KEY_B,
            LibMisc.lang.localize("category.omoshiroikamo"));
        ClientRegistry.registerKeyBinding(keyOpenBackpack);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        handleInput();
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) {
        if (Mouse.getEventButton() >= 0) {
            handleInput();
        }
    }

    private void handleInput() {
        handleOpenBackpack();
    }

    private void handleOpenBackpack() {
        if (keyOpenBackpack.isPressed() && LibMods.Baubles.isLoaded()) {
            InventoryTypes.BAUBLES.visitAll(Platform.getClientPlayer(), (type, index, stack) -> {
                if (stack != null && stack.getItem() instanceof ItemBackpack) {
                    GuiFactories.playerInventory()
                        .openFromBaublesClient(index);
                    return true;
                }
                return false;
            });
        }
    }
}
