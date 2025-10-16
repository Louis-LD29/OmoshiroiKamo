package louis.omoshiroikamo.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.gtnewhorizon.structurelib.alignment.constructable.ConstructableUtility;

import louis.omoshiroikamo.api.enums.ModObject;
import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.util.lib.LibResources;

public class ItemAssembler extends ItemOK {

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
    public boolean func_150897_b(Block block) {
        return isValidSolarBlock(block);
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {
        return isValidSolarBlock(block) ? 50.0F : 1.0F;
    }

    private boolean isValidSolarBlock(Block block) {
        return block == ModBlocks.blockSolarArray || block == ModBlocks.blockSolarCell
            || block == ModBlocks.blockVoidOreMiner
            || block == ModBlocks.blockVoidResMiner
            || block == ModBlocks.blockNanoBotBeacon
            || block == ModBlocks.blockLaserCore
            || block == ModBlocks.blockLaserLens
            || block == ModBlocks.blockStructureFrame
            || block == ModBlocks.blockMachineBase
            || block == ModBlocks.blockModifierNull
            || block == ModBlocks.blockModifierAccuracy
            || block == ModBlocks.blockModifierSpeed
            || block == ModBlocks.blockModifierResistance
            || block == ModBlocks.blockModifierFlight
            || block == ModBlocks.blockModifierHaste
            || block == ModBlocks.blockModifierSaturation
            || block == ModBlocks.blockModifierStrength
            || block == ModBlocks.blockModifierFireResistance
            || block == ModBlocks.blockModifierRegeneration
            || block == ModBlocks.blockModifierNightVision
            || block == ModBlocks.blockModifierJumpBoost
            || block == ModBlocks.blockModifierWaterBreathing
            || block == ModBlocks.blockModifierPiezo;
    }
}
