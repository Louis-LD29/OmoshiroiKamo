package louis.omoshiroikamo.common.plugin.tic;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.fluid.material.FluidMaterialRegistry;
import louis.omoshiroikamo.common.item.ModItems;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;

public class TICCompat {

    public static boolean hasClay = GameRegistry.findItem("TConstruct", "clayPattern") != null;

    public static void registerTinkersConstructIntegration(FMLPostInitializationEvent evt) {
        if (TinkerSmeltery.smeltery == null) return;

        Block block = ModBlocks.blockMaterial;
        Item item = ModItems.itemMaterial;
        Item moltenBucket = FluidMaterialRegistry.itemBucketMaterial;

        for (MaterialEntry entry : MaterialRegistry.all()) {
            String name = entry.getUnlocalizedName();
            int meta = entry.getMeta();

            addItemStackToDirectoryTic(name, meta);
            if ("Carbon Steel".equalsIgnoreCase(entry.getName())) {
                addItemStackToDirectoryTic("Steel", meta);
            }

            String fluidName = entry.getUnlocalizedName()
                .toLowerCase(Locale.ROOT) + ".molten";
            Fluid fluid = FluidRegistry.getFluid(fluidName);

            if (fluid == null) {
                System.err.println("[TICCompat] Skipping material without fluid: " + fluidName);
                continue;
            }

            int temp = (int) Math.round(entry.getMeltingPointK());

            if (FluidType.getFluidType(fluid) == null) {
                FluidType.registerFluidType(name, block, meta, temp, fluid, true);
            }

            FluidType fluidType = FluidType.getFluidType(fluid);
            if (fluidType == null) {
                System.err.println("[TICCompat] Failed to get FluidType for fluid: " + fluid.getUnlocalizedName());
                continue;
            }

            LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
            LiquidCasting basinCasting = TConstructRegistry.getBasinCasting();

            Smeltery.addMelting(block, meta, temp, new FluidStack(fluid, TConstruct.blockLiquidValue));
            Smeltery.addMelting(fluidType, new ItemStack(item, 1, meta), 0, TConstruct.ingotLiquidValue);
            Smeltery.addMelting(
                fluidType,
                new ItemStack(item, 1, LibResources.META1 + meta),
                0,
                TConstruct.nuggetLiquidValue);

            int cooldown = entry.getCooldownTicks();

            ItemStack nuggetcast = new ItemStack(TinkerSmeltery.metalPattern, 1, 27);
            ItemStack nuggetcast_clay = hasClay ? new ItemStack(TinkerSmeltery.clayPattern, 1, 27) : null;
            ItemStack ingotCast = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
            ItemStack ingotCastClay = hasClay ? new ItemStack(TinkerSmeltery.clayPattern, 1, 0) : null;
            ItemStack rodCast = new ItemStack(TinkerSmeltery.metalPattern, 1, 1);
            ItemStack rodCastClay = hasClay ? new ItemStack(TinkerSmeltery.clayPattern, 1, 1) : null;
            ItemStack gearCast = new ItemStack(GameRegistry.findItem("TConstruct", "gearCast"), 1);

            basinCasting.addCastingRecipe(
                new ItemStack(block, 1, meta),
                new FluidStack(fluid, TConstruct.blockLiquidValue),
                null,
                false,
                cooldown * 2);

            tableCasting.addCastingRecipe(
                new ItemStack(moltenBucket, 1, meta),
                new FluidStack(fluid, 1000),
                new ItemStack(Items.bucket),
                true,
                cooldown);

            tableCasting.addCastingRecipe(
                new ItemStack(item, 1, meta),
                new FluidStack(fluid, TConstruct.ingotLiquidValue),
                ingotCast,
                false,
                cooldown);
            if (ingotCastClay != null) {
                tableCasting.addCastingRecipe(
                    new ItemStack(item, 1, meta),
                    new FluidStack(fluid, TConstruct.ingotLiquidValue),
                    ingotCastClay,
                    true,
                    cooldown);
            }
            int nuggetCooldown = Math.max(10, cooldown / 2);
            tableCasting.addCastingRecipe(
                new ItemStack(item, 1, LibResources.META1 + meta),
                new FluidStack(fluid, TConstruct.nuggetLiquidValue),
                nuggetcast,
                false,
                nuggetCooldown);
            if (nuggetcast_clay != null) {
                tableCasting.addCastingRecipe(
                    new ItemStack(item, 1, LibResources.META1 + meta),
                    new FluidStack(fluid, TConstruct.nuggetLiquidValue),
                    nuggetcast_clay,
                    true,
                    nuggetCooldown);
            }

            int rodCooldown = Math.max(10, cooldown / 2);
            tableCasting.addCastingRecipe(
                new ItemStack(item, 1, LibResources.META3 + meta),
                new FluidStack(fluid, TConstruct.ingotLiquidValue / 2),
                rodCast,
                false,
                rodCooldown);
            if (rodCastClay != null) {
                tableCasting.addCastingRecipe(
                    new ItemStack(item, 1, LibResources.META3 + meta),
                    new FluidStack(fluid, TConstruct.ingotLiquidValue / 2),
                    rodCastClay,
                    true,
                    rodCooldown);
            }

            int gearCooldown = Math.max(20, cooldown + cooldown / 2);
            tableCasting.addCastingRecipe(
                new ItemStack(item, 1, LibResources.META5 + meta),
                new FluidStack(fluid, TConstruct.ingotLiquidValue * 4),
                gearCast,
                false,
                gearCooldown);
        }
    }

    public static void addItemStackToDirectoryTic(String name, int meta) {
        TConstructRegistry.addItemStackToDirectory("ingot" + name, new ItemStack(ModItems.itemMaterial, 1, meta));
        TConstructRegistry.addItemStackToDirectory(
            "nugget" + name,
            new ItemStack(ModItems.itemMaterial, 1, LibResources.META1 + meta));
        TConstructRegistry.addItemStackToDirectory(
            "plate" + name,
            new ItemStack(ModItems.itemMaterial, 1, LibResources.META2 + meta));
        TConstructRegistry
            .addItemStackToDirectory("rod" + name, new ItemStack(ModItems.itemMaterial, 1, LibResources.META3 + meta));
    }

}
