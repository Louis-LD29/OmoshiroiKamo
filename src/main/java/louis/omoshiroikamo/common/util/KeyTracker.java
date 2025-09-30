package louis.omoshiroikamo.common.util;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.inventory.InventoryTypes;
import com.cleanroommc.modularui.utils.Platform;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;
import louis.omoshiroikamo.common.util.lib.LibMisc;
import louis.omoshiroikamo.common.util.lib.LibMods;

@EventBusSubscriber()
public class KeyTracker {

    public static final KeyTracker instance = new KeyTracker();

    private static KeyBinding backpackOpenKey;

    public KeyTracker() {
        backpackOpenKey = new KeyBinding(
            LibMisc.lang.localize("keybind.backpackOpenToggle"),
            Keyboard.KEY_NONE,
            LibMisc.lang.localize("category.omoshiroikamo"));
        ClientRegistry.registerKeyBinding(backpackOpenKey);

    }

    @SubscribeEvent
    public static void onKeyInput(KeyInputEvent event) {
        handleInput();
    }

    @SubscribeEvent
    public static void onMouseInput(MouseInputEvent event) {
        if (Mouse.getEventButton() >= 0) {
            handleInput();
        }
    }

    private static void handleInput() {
        handleOpenBackpack();
    }

    private static void handleOpenBackpack() {
        if (backpackOpenKey.isPressed() && LibMods.Baubles.isLoaded()) {
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
