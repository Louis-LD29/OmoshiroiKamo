package com.louis.test.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.louis.test.core.handlers.PoweredItemRenderer;

public final class ModItems {

    public static Item itemOperationOrb;

    public static void init() {
        itemOperationOrb = new ItemOperationOrb();
    }

    public static void registerItemRenderer() {
        PoweredItemRenderer dsr = new PoweredItemRenderer();
        MinecraftForgeClient.registerItemRenderer(itemOperationOrb, dsr);
    }
}
