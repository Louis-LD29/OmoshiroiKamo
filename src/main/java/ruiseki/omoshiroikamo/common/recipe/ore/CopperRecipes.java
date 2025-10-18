package ruiseki.omoshiroikamo.common.recipe.ore;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.omoshiroikamo.api.ore.OreRegistry;
import ruiseki.omoshiroikamo.common.init.ModItems;
import ruiseki.omoshiroikamo.common.ore.OreRegister;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class CopperRecipes {

    public static void init() {
        ItemStack copperIngot = new ItemStack(ModItems.itemMaterial, 1, 1);

        Block copperOre = OreRegister.getBlock(OreRegistry.get("Copper"));
        Block cupriteOre = OreRegister.getBlock(OreRegistry.get("Cuprite"));
        Block borniteOre = OreRegister.getBlock(OreRegistry.get("Bornite"));
        Block malachiteOre = OreRegister.getBlock(OreRegistry.get("Malachite"));
        Block chalcopyriteOre = OreRegister.getBlock(OreRegistry.get("Chalcopyrite"));
        Block tetrahedriteOre = OreRegister.getBlock(OreRegistry.get("Tetrahedrite"));

        ItemStack washedCopper = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 7);
        ItemStack washedCuprite = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 6);
        ItemStack washedBornite = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 4);
        ItemStack washedMalachite = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 3);
        ItemStack washedChalcopyrite = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 2);
        ItemStack washedTetrahedrite = new ItemStack(ModItems.itemOre, 1, LibResources.META1 + 5);

        GameRegistry.addSmelting(new ItemStack(copperOre, 1, 0), copperIngot.copy(), 0.7f);
        GameRegistry.addSmelting(washedCopper, getCopperNugget(12), 0.7f);

        GameRegistry.addSmelting(washedCuprite, getCopperNugget(11), 0.7f);

        GameRegistry.addSmelting(washedBornite, getCopperNugget(8), 0.7f);

        GameRegistry.addSmelting(washedMalachite, getCopperNugget(7), 0.7f);

        GameRegistry.addSmelting(washedChalcopyrite, getCopperNugget(5), 0.7f);

        GameRegistry.addSmelting(washedTetrahedrite, getCopperNugget(4), 0.7f);

    }

    public static ItemStack getCopperNugget(int amount) {
        return new ItemStack(ModItems.itemMaterial, amount, LibResources.META1 + 1);
    }

}
