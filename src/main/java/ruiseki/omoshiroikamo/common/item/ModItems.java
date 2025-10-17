package ruiseki.omoshiroikamo.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.client.handler.PoweredItemRenderer;
import ruiseki.omoshiroikamo.common.item.backpack.ItemBackpack;
import ruiseki.omoshiroikamo.common.item.backpack.ItemBatteryUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemCraftingUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemEverlastingUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemFeedingUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemMagnetUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemStackUpgrade;
import ruiseki.omoshiroikamo.common.item.backpack.ItemUpgrade;
import ruiseki.omoshiroikamo.common.ore.ItemOre;

public final class ModItems {

    public static Item itemOperationOrb;
    public static Item itemBackPack;
    public static Item itemUpgrade;
    public static Item itemStackUpgrade;
    public static Item itemCraftingUpgrade;
    public static Item itemMagnetUpgrade;
    public static Item itemFeedingUpgrade;
    public static Item itemBatteryUpgrade;
    public static Item itemEverlastingUpgrade;
    public static Item itemStabilizedEnderPear;
    public static Item itemPhotovoltaicCell;
    public static Item itemAssembler;
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
        itemFeedingUpgrade = ItemFeedingUpgrade.create();
        itemBatteryUpgrade = ItemBatteryUpgrade.create();
        itemEverlastingUpgrade = ItemEverlastingUpgrade.create();

        itemStabilizedEnderPear = ItemOK.create(ModObject.itemStabilizedEnderPear, "ender_stabilized");
        itemPhotovoltaicCell = ItemOK.create(ModObject.itemPhotovoltaicCell, "photovoltaic_cell");
        itemAssembler = ItemAssembler.create();

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
