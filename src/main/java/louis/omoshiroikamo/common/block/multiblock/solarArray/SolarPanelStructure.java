package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import louis.omoshiroikamo.common.block.ModBlocks;
import louis.omoshiroikamo.common.util.Logger;

public class SolarPanelStructure {

    // spotless:off
    public static final String STRUCTURE_TIER_1 = "tier1";
    public static final String[][] SHAPE_TIER_1 = new String[][]{
        {
            "IIIII",
            "IGGGI",
            "IGGGI",
            "IGGGI",
            "IIIII"},
        {
            "     ",
            " U U ",
            "  1  ",
            " U U ",
            "     ",
        }};
    public static final String STRUCTURE_TIER_2 = "tier2";
    public static final String[][] SHAPE_TIER_2 = new String[][]{
        {
            "IIIIIII",
            "IGGGGGI",
            "IGGGGGI",
            "IGGGGGI",
            "IGGGGGI",
            "IGGGGGI",
            "IIIIIII"
        },
        {
            "       ",
            "       ",
            "  U U  ",
            "   2   ",
            "  U U  ",
            "       ",
            "       "
        }};
    public static final String STRUCTURE_TIER_3 = "tier3";
    public static final String[][] SHAPE_TIER_3 = new String[][]{
        {
            "IIIIIIIII",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IGGGGGGGI",
            "IIIIIIIII"
        },
        {
            "         ",
            "         ",
            "   U U   ",
            "  U   U  ",
            "    3    ",
            "  U   U  ",
            "   U U   ",
            "         ",
            "         "
        }};
    public static final String STRUCTURE_TIER_4 = "tier4";
    public static final String[][] SHAPE_TIER_4 = new String[][]{
        {
            "IIIIIIIIIII",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IGGGGGGGGGI",
            "IIIIIIIIIII"
        },
        {
            "           ",
            "           ",
            "           ",
            "    U U    ",
            "   U   U   ",
            "     4     ",
            "   U   U   ",
            "    U U    ",
            "           ",
            "           ",
            "           "
        }};
    // spotless:on

    public static final int[][] TIER_OFFSET = { { 2, 1, 2 }, { 3, 1, 3 }, { 4, 1, 4 }, { 5, 1, 5 } };
    public static IStructureDefinition<TESolarArray> STRUCTURE_DEFINITION;

    @SuppressWarnings("unchecked")
    public static void registerSolarArrayStructureInfo() {
        StructureDefinition.Builder<TESolarArray> builder = StructureDefinition.builder();

        builder.addShape(STRUCTURE_TIER_1, transpose(SHAPE_TIER_1))
            .addShape(STRUCTURE_TIER_2, transpose(SHAPE_TIER_2))
            .addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('1', ofBlock(ModBlocks.blockSolarArray, 0))
            .addElement('2', ofBlock(ModBlocks.blockSolarArray, 1))
            .addElement('3', ofBlock(ModBlocks.blockSolarArray, 2))
            .addElement('4', ofBlock(ModBlocks.blockSolarArray, 3))
            .addElement('U', ofBlock(Blocks.gold_block, 0))
            .addElement('I', ofBlock(Blocks.iron_block, 0))
            .addElement('G', ofBlock(Blocks.glass, 0));

        IStructureDefinition<TESolarArray> definition = builder.build();
        STRUCTURE_DEFINITION = definition;

        IMultiblockInfoContainer
            .registerTileClass(TESolarArray.class, new SolarArrayMultiblockInfoContainer(definition));

        Logger.info("Register Solar Array Structure Info");
    }

    private static class SolarArrayMultiblockInfoContainer implements IMultiblockInfoContainer<TESolarArray> {

        private final IStructureDefinition<TESolarArray> structure;

        public SolarArrayMultiblockInfoContainer(IStructureDefinition<TESolarArray> structure) {
            this.structure = structure;
        }

        /**
         * Build or hint only the current tier of the Solar Array.
         */
        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TESolarArray ctx, ExtendedFacing aSide) {
            int tier = Math.min(ctx.getMeta() + 1, 4);

            this.structure.buildOrHints(
                ctx,
                triggerStack,
                "tier" + tier,
                ctx.getWorldObj(),
                ExtendedFacing.DEFAULT,
                ctx.xCoord,
                ctx.yCoord,
                ctx.zCoord,
                TIER_OFFSET[tier - 1][0],
                TIER_OFFSET[tier - 1][1],
                TIER_OFFSET[tier - 1][2],
                hintsOnly);
        }

        /**
         * Build in survival mode only the current tier.
         */
        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudget, ISurvivalBuildEnvironment env,
            TESolarArray ctx, ExtendedFacing aSide) {

            int tier = Math.min(ctx.getMeta() + 1, 4);
            int built = 0;

            if (ctx.getTier() >= tier) {
                return -1;
            }

            built = this.structure.survivalBuild(
                ctx,
                triggerStack,
                "tier" + tier,
                ctx.getWorldObj(),
                ExtendedFacing.DEFAULT,
                ctx.xCoord,
                ctx.yCoord,
                ctx.zCoord,
                TIER_OFFSET[tier - 1][0],
                TIER_OFFSET[tier - 1][1],
                TIER_OFFSET[tier - 1][2],
                elementBudget,
                env,
                false);

            return built;
        }

        @Override
        public String[] getDescription(ItemStack stackSize) {
            return new String[0];
        }
    }
}
