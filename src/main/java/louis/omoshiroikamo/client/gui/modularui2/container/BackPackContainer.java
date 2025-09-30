package louis.omoshiroikamo.client.gui.modularui2.container;

import com.cleanroommc.modularui.screen.ModularContainer;

import louis.omoshiroikamo.client.gui.BackpackGui;

public class BackPackContainer extends ModularContainer {

    public final BackpackGui gui;
    public int meta;

    public BackPackContainer(BackpackGui gui) {
        this.gui = gui;
        this.meta = gui.getData()
            .getUsedItemStack()
            .getItemDamage();
    }

}
