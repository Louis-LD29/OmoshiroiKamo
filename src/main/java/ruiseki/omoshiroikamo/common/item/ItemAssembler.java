package ruiseki.omoshiroikamo.common.item;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.Sets;
import com.gtnewhorizon.structurelib.alignment.constructable.ConstructableUtility;

import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.util.lib.LibMisc;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class ItemAssembler extends ItemOK {

    private static final Set<Block> VALID_SOLAR_BLOCKS = Sets.newHashSet(
        ModBlocks.SOLAR_ARRAY.get(),
        ModBlocks.SOLAR_CELL.get(),
        ModBlocks.VOID_RES_MINER.get(),
        ModBlocks.VOID_ORE_MINER.get(),
        ModBlocks.NANO_BOT_BEACON.get(),
        ModBlocks.LASER_LENS.get(),
        ModBlocks.LASER_CORE.get(),
        ModBlocks.STRUCTURE_FRAME.get(),
        ModBlocks.MACHINE_BASE.get(),
        ModBlocks.MODIFIER_NULL.get(),
        ModBlocks.MODIFIER_ACCURACY.get(),
        ModBlocks.MODIFIER_SPEED.get(),
        ModBlocks.MODIFIER_RESISTANCE.get(),
        ModBlocks.MODIFIER_FLIGHT.get(),
        ModBlocks.MODIFIER_HASTE.get(),
        ModBlocks.MODIFIER_SATURATION.get(),
        ModBlocks.MODIFIER_STRENGTH.get(),
        ModBlocks.MODIFIER_FIRE_RESISTANCE.get(),
        ModBlocks.MODIFIER_REGENERATION.get(),
        ModBlocks.MODIFIER_NIGHT_VISION.get(),
        ModBlocks.MODIFIER_JUMP_BOOST.get(),
        ModBlocks.MODIFIER_WATER_BREATHING.get(),
        ModBlocks.MODIFIER_PIEZO.get());

    public ItemAssembler() {
        super(ModObject.itemAssembler.unlocalisedName);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setNoRepair();
    }

    public static ItemAssembler create() {
        ItemAssembler item = new ItemAssembler();
        item.init();
        return item;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibResources.PREFIX_MOD + "assembler");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        return ConstructableUtility.handle(stack, player, world, x, y, z, side);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, int x, int y, int z,
        EntityLivingBase entity) {

        if (isValidSolarBlock(blockIn)) {
            return true;
        }

        return super.onBlockDestroyed(stack, worldIn, blockIn, x, y, z, entity);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack itemStack) {
        if (isValidSolarBlock(block)) {
            return true;
        }
        return false;
    }

    @Override
    public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
        if (isValidSolarBlock(block)) {
            return 50.0F;
        }
        return 0f;
    }

    private boolean isValidSolarBlock(Block block) {
        return VALID_SOLAR_BLOCKS.contains(block);
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        super.addDetailedEntries(itemstack, entityplayer, list, flag);
        list.add(LibMisc.lang.localize(LibResources.TOOLTIP + "assembler.l1"));
        list.add(LibMisc.lang.localize(LibResources.TOOLTIP + "assembler.l2"));
    }
}
