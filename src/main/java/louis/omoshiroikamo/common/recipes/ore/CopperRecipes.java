package louis.omoshiroikamo.common.recipes.ore;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.ore.OreRegistry;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.common.ore.OreRegister;

public class CopperRecipes {

    public static void init() {
        ItemStack copperIngot = new ItemStack(ModItems.itemMaterial, 1, 1);

        Block copperOre = OreRegister.getBlock(OreRegistry.getByName("Copper"));
        Block cupriteOre = OreRegister.getBlock(OreRegistry.getByName("Cuprite"));
        Block borniteOre = OreRegister.getBlock(OreRegistry.getByName("Bornite"));
        Block malachiteeOre = OreRegister.getBlock(OreRegistry.getByName("Malachite"));
        Block chalcopyriteOre = OreRegister.getBlock(OreRegistry.getByName("Chalcopyrite"));
        Block tetrahedriteOre = OreRegister.getBlock(OreRegistry.getByName("Tetrahedrite"));

        GameRegistry.addSmelting(new ItemStack(copperOre, 1, 0), copperIngot.copy(), 0.7f);

        GameRegistry.addSmelting(new ItemStack(cupriteOre, 1, 0), getCopperNugget(8), 0.7f);

        GameRegistry.addSmelting(new ItemStack(borniteOre, 1, 0), getCopperNugget(5), 0.7f);

        GameRegistry.addSmelting(new ItemStack(malachiteeOre, 1, 0), getCopperNugget(5), 0.7f);

        GameRegistry.addSmelting(new ItemStack(chalcopyriteOre, 1, 0), getCopperNugget(3), 0.7f);

        GameRegistry.addSmelting(new ItemStack(tetrahedriteOre, 1, 0), getCopperNugget(3), 0.7f);
    }

    public static ItemStack getCopperNugget(int amount) {
        ItemStack stack = new ItemStack(ModItems.itemMaterial, amount, LibResources.META1 + 1);
        return stack;
    }

}
