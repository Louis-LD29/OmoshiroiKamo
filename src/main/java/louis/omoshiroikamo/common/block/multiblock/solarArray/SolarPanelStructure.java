package louis.omoshiroikamo.common.block.multiblock.solarArray;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static louis.omoshiroikamo.plugin.structureLib.StructureLibUtils.ofBlockAdderWithPos;

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
            "11111",
            "1GGG1",
            "1GGG1",
            "1GGG1",
            "11111"},
        {
            "     ",
            " A A ",
            "  Q  ",
            " A A ",
            "     ",
        }};
    public static final String STRUCTURE_TIER_2 = "tier2";
    public static final String[][] SHAPE_TIER_2 = new String[][]{
        {
            "2222222",
            "2GGGGG2",
            "2GGGGG2",
            "2GGGGG2",
            "2GGGGG2",
            "2GGGGG2",
            "2222222"
        },
        {
            "       ",
            "       ",
            "  A A  ",
            "   W   ",
            "  A A  ",
            "       ",
            "       "
        }};
    public static final String STRUCTURE_TIER_3 = "tier3";
    public static final String[][] SHAPE_TIER_3 = new String[][]{
        {
            "333333333",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "3GGGGGGG3",
            "333333333"
        },
        {
            "         ",
            "         ",
            "   A A   ",
            "  A   A  ",
            "    E    ",
            "  A   A  ",
            "   A A   ",
            "         ",
            "         "
        }};
    public static final String STRUCTURE_TIER_4 = "tier4";
    public static final String[][] SHAPE_TIER_4 = new String[][]{
        {
            "44444444444",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "4GGGGGGGGG4",
            "44444444444"
        },
        {
            "           ",
            "           ",
            "           ",
            "    A A    ",
            "   A   A   ",
            "     R     ",
            "   A   A   ",
            "    A A    ",
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
            .addElement('Q', ofBlock(ModBlocks.blockSolarArray, 0))
            .addElement('W', ofBlock(ModBlocks.blockSolarArray, 1))
            .addElement('E', ofBlock(ModBlocks.blockSolarArray, 2))
            .addElement('R', ofBlock(ModBlocks.blockSolarArray, 3))
            .addElement('G', ofBlock(ModBlocks.blockSolarCell, 0))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(
                        (teSolarArray, block, meta, x, y, z) -> teSolarArray.addToMachine(block, meta, x, y, z),
                        ModBlocks.blockMultiblockUpgrade,
                        0),
                    ofBlock(ModBlocks.blockMultiblockUpgrade, 1),
                    ofBlock(ModBlocks.blockMultiblockUpgrade, 0)))
            .addElement(
                '1',
                ofChain(ofBlock(ModBlocks.blockStructureFrame, 0), ofBlock(ModBlocks.blockStructureFrame, 4)))
            .addElement(
                '2',
                ofChain(ofBlock(ModBlocks.blockStructureFrame, 1), ofBlock(ModBlocks.blockStructureFrame, 5)))
            .addElement(
                '3',
                ofChain(ofBlock(ModBlocks.blockStructureFrame, 2), ofBlock(ModBlocks.blockStructureFrame, 6)))
            .addElement(
                '4',
                ofChain(ofBlock(ModBlocks.blockStructureFrame, 3), ofBlock(ModBlocks.blockStructureFrame, 7)));

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

            if (ctx.getMeta() + 1 >= tier) {
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
