package louis.omoshiroikamo.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import louis.omoshiroikamo.client.handler.PoweredItemRenderer;
import louis.omoshiroikamo.common.item.backpack.ItemBackpack;
import louis.omoshiroikamo.common.item.backpack.ItemCraftingUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemMagnetUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemStackUpgrade;
import louis.omoshiroikamo.common.item.backpack.ItemUpgrade;
import louis.omoshiroikamo.common.ore.ItemOre;

public final class ModItems {

    public static Item itemOperationOrb;
    public static Item itemBackPack;
    public static Item itemUpgrade;
    public static Item itemStackUpgrade;
    public static Item itemCraftingUpgrade;
    public static Item itemMagnetUpgrade;
    public static Item itemWireCoil;
    public static Item itemMaterial;
    public static Item itemOre;
    public static Item itemHammer;

    public static void init() {
        itemOperationOrb = ItemOperationOrb.create();
        itemBackPack = ItemBackpack.create();
        itemUpgrade = ItemUpgrade.create();
        itemStackUpgrade = ItemStackUpgrade.create();
        itemCraftingUpgrade = ItemCraftingUpgrade.create();
        itemMagnetUpgrade = ItemMagnetUpgrade.create();
        itemWireCoil = ItemWireCoil.create();
        itemMaterial = ItemMaterial.create();
        itemOre = ItemOre.create();
        itemHammer = ItemHammer.create();
    }

    public static void registerItemRenderer() {
        PoweredItemRenderer dsr = new PoweredItemRenderer();
        MinecraftForgeClient.registerItemRenderer(itemOperationOrb, dsr);
    }
}
