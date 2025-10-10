package louis.omoshiroikamo.common.block.solarArray;

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

    private static final String STRUCTURE_TIER_1 = "tier1";
    private static final String[][] SHAPE_TIER_1 = new String[][] { { "IIIII", "IGGGI", "IGGGI", "IGGGI", "IIIII" },
        { "     ", " U U ", "  1  ", " U U ", "     ", } };
    private static final String STRUCTURE_TIER_2 = "tier2";
    private static final String[][] SHAPE_TIER_2 = new String[][] {
        { "IIIIIII", "IGGGGGI", "IGGGGGI", "IGGGGGI", "IGGGGGI", "IGGGGGI", "IIIIIII" },
        { "       ", "       ", "  U U  ", "   2   ", "  U U  ", "       ", "       " } };
    private static final String STRUCTURE_TIER_3 = "tier3";
    private static final String STRUCTURE_TIER_4 = "tier4";
    private static final String STRUCTURE_TIER_5 = "tier5";
    private static final String STRUCTURE_TIER_6 = "tier6";

    public static final int[][] TIER_OFFSET = { { 2, 1, 2 }, { 3, 1, 3 }, { 4, 1, 4 }, { 5, 1, 5 }, { 6, 1, 6 },
        { 7, 1, 7 } };

    @SuppressWarnings("unchecked")
    public static void registerSolarArrayStructureInfo() {
        StructureDefinition.Builder<TESolarArray> builder = StructureDefinition.builder();

        builder.addShape(STRUCTURE_TIER_1, transpose(SHAPE_TIER_1))
            .addShape(STRUCTURE_TIER_2, transpose(SHAPE_TIER_2))
            .addElement('1', ofBlock(ModBlocks.blockSolarArray, 0))
            .addElement('2', ofBlock(ModBlocks.blockSolarArray, 1))
            .addElement('U', ofBlock(Blocks.gold_block, 0))
            .addElement('I', ofBlock(Blocks.iron_block, 0))
            .addElement('G', ofBlock(Blocks.glass, 0));

        IMultiblockInfoContainer.registerTileClass(
            TESolarArray.class,
            new SolarPanelStructure.SolarArrayMultiblockInfoContainer(builder.build()));
        Logger.info("Register Solar Array Structure Info");
    }

    private static class SolarArrayMultiblockInfoContainer implements IMultiblockInfoContainer<TESolarArray> {

        private final IStructureDefinition<TESolarArray> structureAltar;

        public SolarArrayMultiblockInfoContainer(IStructureDefinition<TESolarArray> structureAltar) {
            this.structureAltar = structureAltar;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TESolarArray ctx, ExtendedFacing aSide) {
            int tier = triggerStack.stackSize;
            if (tier > 6) {
                tier = 6;
            }
            for (int i = 1; i <= tier; i++) {
                this.structureAltar.buildOrHints(
                    ctx,
                    triggerStack,
                    "tier" + i,
                    ctx.getWorldObj(),
                    ExtendedFacing.DEFAULT,
                    ctx.xCoord,
                    ctx.yCoord,
                    ctx.zCoord,
                    TIER_OFFSET[i - 1][0],
                    TIER_OFFSET[i - 1][1],
                    TIER_OFFSET[i - 1][2],
                    hintsOnly);
            }
        }

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudge, ISurvivalBuildEnvironment env,
            TESolarArray altar, ExtendedFacing aSide) {
            int built = 0;
            int tier = triggerStack.stackSize;
            if (tier > 6) {
                tier = 6;
            }
            if (altar.getTier() >= tier) {
                return -1;
            }

            for (int i = 1; i <= tier; i++) {
                built += this.structureAltar.survivalBuild(
                    altar,
                    triggerStack,
                    "tier" + i,
                    altar.getWorldObj(),
                    ExtendedFacing.DEFAULT,
                    altar.xCoord,
                    altar.yCoord,
                    altar.zCoord,
                    TIER_OFFSET[i - 1][0],
                    TIER_OFFSET[i - 1][1],
                    TIER_OFFSET[i - 1][2],
                    elementBudge,
                    env,
                    false);
            }
            return built;
        }

        @Override
        public String[] getDescription(ItemStack stackSize) {
            return new String[0];
        }

    }
}
