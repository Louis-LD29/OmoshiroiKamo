package com.louis.test.common.item;

import com.louis.test.core.handlers.PoweredItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public final class ModItems {
    public static ItemOperationOrb itemOperationOrb;

    public static void init() {
        itemOperationOrb = new ItemOperationOrb();
    }

    public static void registerItemRenderer() {
        PoweredItemRenderer dsr = new PoweredItemRenderer();
        MinecraftForgeClient.registerItemRenderer(itemOperationOrb, dsr);
    }
}
