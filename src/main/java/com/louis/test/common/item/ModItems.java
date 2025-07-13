package com.louis.test.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.louis.test.client.handler.PoweredItemRenderer;

public final class ModItems {

    public static Item itemOperationOrb;
    public static Item itemWireCoil;
    public static Item itemMaterial;

    public static void init() {
        itemOperationOrb = new ItemOperationOrb();
        itemWireCoil = ItemWireCoil.create();
        itemMaterial = ItemMaterial.create();
        // MaterialWire.mWireCoil = ModItems.itemWireCoil;
    }

    public static void registerItemRenderer() {
        PoweredItemRenderer dsr = new PoweredItemRenderer();
        MinecraftForgeClient.registerItemRenderer(itemOperationOrb, dsr);
    }
}
