package louis.omoshiroikamo.common.fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.api.material.MaterialEntry;
import louis.omoshiroikamo.api.material.MaterialRegistry;
import louis.omoshiroikamo.common.OKCreativeTab;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.fluid.material.FluidMaterialRegister;

public class ItemBucketMaterial extends Item {

    @SideOnly(Side.CLIENT)
    protected IIcon baseIcon, overlayIcon;

    private static final Map<Integer, MaterialEntry> META_TO_ENTRY = new HashMap<>();
    private static final Map<MaterialEntry, Integer> ENTRY_TO_META = new HashMap<>();

    public ItemBucketMaterial() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(OKCreativeTab.INSTANCE);
        setUnlocalizedName(ModObject.itemBucketMaterial.unlocalisedName);
    }

    public static ItemBucketMaterial create() {
        ItemBucketMaterial bucket = new ItemBucketMaterial();
        FluidMaterialRegister.itemBucketMaterial = bucket;
        bucket.init();
        return bucket;
    }

    private void init() {
        GameRegistry.registerItem(this, ModObject.itemBucketMaterial.unlocalisedName);

        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            register(materialEntry, meta);
        }
    }

    public static void register(MaterialEntry entry, int meta) {
        META_TO_ENTRY.put(meta, entry);
        ENTRY_TO_META.put(entry, meta);

        Fluid fluid = FluidMaterialRegister.getFluid(entry);
        if (FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid, 1000), new ItemStack(Items.bucket))
            == null) {
            ItemStack filled = new ItemStack(FluidMaterialRegister.itemBucketMaterial, 1, meta);
            ItemStack empty = new ItemStack(Items.bucket);
            FluidContainerRegistry.registerFluidContainer(
                new FluidContainerRegistry.FluidContainerData(new FluidStack(fluid, 1000), filled, empty));
        }
    }

    public static MaterialEntry getMaterial(int meta) {
        return META_TO_ENTRY.get(meta);
    }

    public static int getMeta(MaterialEntry entry) {
        return ENTRY_TO_META.getOrDefault(entry, 0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String base = super.getUnlocalizedName(stack);
        String mat = MaterialRegistry.fromMeta(stack.getItemDamage() % LibResources.META1)
            .getUnlocalizedName();
        return base + "." + mat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (MaterialEntry materialEntry : MaterialRegistry.all()) {
            int meta = materialEntry.meta;
            list.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? baseIcon : overlayIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg) {
        baseIcon = reg.registerIcon("bucket_empty");
        overlayIcon = reg.registerIcon(LibResources.PREFIX_MOD + "material_bucket_overlay");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass == 1) {
            MaterialEntry entry = getMaterial(stack.getItemDamage());
            return entry != null ? entry.getMoltenColor() : 0xFFFFFF;
        }
        return 0xFFFFFF;
    }

    public Fluid getFluid(ItemStack stack) {
        MaterialEntry entry = getMaterial(stack.getItemDamage());
        return entry != null ? FluidMaterialRegister.getFluid(entry) : null;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);
        if (mop == null) return stack;

        FillBucketEvent event = new FillBucketEvent(player, stack, world, mop);
        if (MinecraftForge.EVENT_BUS.post(event)) return stack;

        if (event.getResult() == Event.Result.ALLOW) {
            if (!player.capabilities.isCreativeMode) {
                if (--stack.stackSize <= 0) return event.result;
                if (!player.inventory.addItemStackToInventory(event.result)) {
                    player.dropPlayerItemWithRandomChoice(event.result, false);
                }
            }
            return stack;
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;
            int side = mop.sideHit;

            if (!world.canMineBlock(player, x, y, z)) return stack;
            if (!player.canPlayerEdit(x, y, z, side, stack)) return stack;

            Fluid fluid = getFluid(stack);
            Block fluidBlock = fluid != null ? fluid.getBlock() : null;

            if (fluidBlock != null) {
                // Offset to adjacent block face
                switch (side) {
                    case 0:
                        y--;
                        break;
                    case 1:
                        y++;
                        break;
                    case 2:
                        z--;
                        break;
                    case 3:
                        z++;
                        break;
                    case 4:
                        x--;
                        break;
                    case 5:
                        x++;
                        break;
                }

                if (!player.canPlayerEdit(x, y, z, side, stack)) return stack;

                if (tryPlaceContainedLiquid(world, x, y, z, fluidBlock) && !player.capabilities.isCreativeMode) {
                    return new ItemStack(Items.bucket);
                }
            }
        }

        return stack;
    }

    private boolean tryPlaceContainedLiquid(World world, int x, int y, int z, Block fluidBlock) {
        if (fluidBlock == Blocks.air) return false;

        Material targetMat = world.getBlock(x, y, z)
            .getMaterial();
        boolean replaceable = !targetMat.isSolid();

        if (!world.isAirBlock(x, y, z) && !replaceable) return false;

        if (world.provider.isHellWorld && fluidBlock == Blocks.flowing_water) {
            world.playSoundEffect(
                x + 0.5,
                y + 0.5,
                z + 0.5,
                "random.fizz",
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            for (int i = 0; i < 8; i++) {
                world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0);
            }
            return true;
        } else {
            if (!world.isRemote && replaceable && !targetMat.isLiquid()) {
                world.func_147480_a(x, y, z, true);
            }
            world.setBlock(x, y, z, fluidBlock, 0, 3);
            return true;
        }
    }
}
