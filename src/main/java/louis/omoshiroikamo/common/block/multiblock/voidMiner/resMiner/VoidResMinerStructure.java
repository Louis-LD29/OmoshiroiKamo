package louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
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
import louis.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import louis.omoshiroikamo.common.util.Logger;

public class VoidResMinerStructure {

    // spotless:off
    public static final String STRUCTURE_TIER_1 = "tier1";
    public static final String[][] SHAPE_TIER_1 = new String[][]{
        {
            "       ",
            "       ",
            "       ",
            "   Q   ",
            "       ",
            "       ",
            "       "
        },
        {
            "       ",
            "       ",
            "   F   ",
            "  FCF  ",
            "   F   ",
            "       ",
            "       "
        },
        {
            "       ",
            "   F   ",
            "       ",
            " F L F ",
            "       ",
            "   F   ",
            "       "
        },
        {
            "  FFF  ",
            " FPPPF ",
            "FPPPPPF",
            "FPPCPPF",
            "FPPPPPF",
            " FPPPF ",
            "  FFF  "
        }};
    public static final String STRUCTURE_TIER_2 = "tier2";
    public static final String[][] SHAPE_TIER_2 = new String[][]{
        {
            "       ",
            "       ",
            "       ",
            "   Q   ",
            "       ",
            "       ",
            "       "
        },
        {
            "       ",
            "   F   ",
            "   F   ",
            " FFCFF ",
            "   F   ",
            "   F   ",
            "       "
        },
        {
            "   F   ",
            "       ",
            "       ",
            "F  C  F",
            "       ",
            "       ",
            "   F   "
        },
        {
            "   F   ",
            "       ",
            "       ",
            "F  L  F",
            "       ",
            "       ",
            "   F   "
        },
        {
            "  FFF  ",
            " FPAPF ",
            "FPPPPPF",
            "FAPCPAF",
            "FPPPPPF",
            " FPAPF ",
            "  FFF  "
        }};
    public static final String STRUCTURE_TIER_3 = "tier3";
    public static final String[][] SHAPE_TIER_3 = new String[][]{
        {
            "           ",
            "           ",
            "           ",
            "           ",
            "           ",
            "     Q     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "           ",
        },
        {
            "           ",
            "     F     ",
            "     F     ",
            "     F     ",
            "     F     ",
            " FFFFCFFFF ",
            "     F     ",
            "     F     ",
            "     F     ",
            "     F     ",
            "           ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    C    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    C    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    L    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "    FFF    ",
            "  FFPAPFF  ",
            " FAPPPPPAF ",
            " FPPPPPPPF ",
            "FPPPPPPPPPF",
            "FAPPPCPPPAF",
            "FPPPPPPPPPF",
            " FPPPPPPPF ",
            " FAPPPPPAF ",
            "  FFPAPFF  ",
            "    FFF    ",
        }};
    public static final String STRUCTURE_TIER_4 = "tier4";
    public static final String[][] SHAPE_TIER_4 = new String[][]{
        {
            "           ",
            "           ",
            "           ",
            "           ",
            "           ",
            "     Q     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "           ",
        },
        {
            "           ",
            "     F     ",
            "     F     ",
            "     F     ",
            "     F     ",
            " FFFFCFFFF ",
            "     F     ",
            "     F     ",
            "     F     ",
            "     F     ",
            "           ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    C    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    C    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    C    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "     F     ",
            "           ",
            "           ",
            "           ",
            "           ",
            "F    L    F",
            "           ",
            "           ",
            "           ",
            "           ",
            "     F     ",
        },
        {
            "   FFFFF   ",
            "  FPAAAPF  ",
            " FPPPPPPPF ",
            "FPPPPPPPPPF",
            "FAPPPPPPPAF",
            "FAPPPCPPPAF",
            "FAPPPPPPPAF",
            "FPPPPPPPPPF",
            " FPPPPPPPF ",
            "  FPAAAPF  ",
            "   FFFFF   ",
        }};
    // spotless:on

    public static final int[][] TIER_OFFSET = { { 3, 0, 3 }, { 3, 0, 3 }, { 5, 0, 5 }, { 5, 0, 5 } };
    public static IStructureDefinition<TEVoidResMinerT1> STRUCTURE_DEFINITION_TIER_1;
    public static IStructureDefinition<TEVoidResMinerT2> STRUCTURE_DEFINITION_TIER_2;
    public static IStructureDefinition<TEVoidResMinerT3> STRUCTURE_DEFINITION_TIER_3;
    public static IStructureDefinition<TEVoidResMinerT4> STRUCTURE_DEFINITION_TIER_4;

    @SuppressWarnings("unchecked")
    public static void registerStructureInfo() {
        StructureDefinition.Builder<TEVoidResMinerT1> builder1 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidResMinerT2> builder2 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidResMinerT3> builder3 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidResMinerT4> builder4 = StructureDefinition.builder();

        builder1.addShape(STRUCTURE_TIER_1, transpose(SHAPE_TIER_1))
            .addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('Q', ofBlock(ModBlocks.blockVoidResMiner, 0))
            .addElement('P', ofBlockAnyMeta(ModBlocks.blockMachineBase, 0))
            .addElement('C', ofBlock(ModBlocks.blockLaserCore, 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockLaserLens, 0),
                    ofBlockAnyMeta(ModBlocks.blockLaserLens, 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierAccuracy, 0),
                    ofBlock(ModBlocks.blockModifierSpeed, 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.blockStructureFrame, 0),
                    ofBlock(ModBlocks.blockStructureFrame, 4),
                    ofBlock(ModBlocks.blockStructureFrame, 8)));

        builder2.addShape(STRUCTURE_TIER_2, transpose(SHAPE_TIER_2))
            .addElement('Q', ofBlock(ModBlocks.blockVoidResMiner, 1))
            .addElement('P', ofBlockAnyMeta(ModBlocks.blockMachineBase, 0))
            .addElement('C', ofBlock(ModBlocks.blockLaserCore, 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockLaserLens, 0),
                    ofBlockAnyMeta(ModBlocks.blockLaserLens, 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierAccuracy, 0),
                    ofBlock(ModBlocks.blockModifierSpeed, 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.blockStructureFrame, 1),
                    ofBlock(ModBlocks.blockStructureFrame, 5),
                    ofBlock(ModBlocks.blockStructureFrame, 9)));

        builder3.addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addElement('Q', ofBlock(ModBlocks.blockVoidResMiner, 2))
            .addElement('P', ofBlockAnyMeta(ModBlocks.blockMachineBase, 0))
            .addElement('C', ofBlock(ModBlocks.blockLaserCore, 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockLaserLens, 0),
                    ofBlockAnyMeta(ModBlocks.blockLaserLens, 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierAccuracy, 0),
                    ofBlock(ModBlocks.blockModifierSpeed, 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.blockStructureFrame, 2),
                    ofBlock(ModBlocks.blockStructureFrame, 6),
                    ofBlock(ModBlocks.blockStructureFrame, 10)));

        builder4.addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('Q', ofBlock(ModBlocks.blockVoidResMiner, 3))
            .addElement('P', ofBlockAnyMeta(ModBlocks.blockMachineBase, 0))
            .addElement('C', ofBlock(ModBlocks.blockLaserCore, 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockLaserLens, 0),
                    ofBlockAnyMeta(ModBlocks.blockLaserLens, 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierNull, 0),
                    ofBlock(ModBlocks.blockModifierAccuracy, 0),
                    ofBlock(ModBlocks.blockModifierSpeed, 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.blockStructureFrame, 3),
                    ofBlock(ModBlocks.blockStructureFrame, 7),
                    ofBlock(ModBlocks.blockStructureFrame, 11)));

        IStructureDefinition<TEVoidResMinerT1> definition1 = builder1.build();
        STRUCTURE_DEFINITION_TIER_1 = definition1;
        IStructureDefinition<TEVoidResMinerT2> definition2 = builder2.build();
        STRUCTURE_DEFINITION_TIER_2 = definition2;
        IStructureDefinition<TEVoidResMinerT3> definition3 = builder3.build();
        STRUCTURE_DEFINITION_TIER_3 = definition3;
        IStructureDefinition<TEVoidResMinerT4> definition4 = builder4.build();
        STRUCTURE_DEFINITION_TIER_4 = definition4;

        IMultiblockInfoContainer.registerTileClass(TEVoidResMinerT1.class, new MultiblockInfoContainerT1(definition1));
        IMultiblockInfoContainer.registerTileClass(TEVoidResMinerT2.class, new MultiblockInfoContainerT2(definition2));
        IMultiblockInfoContainer.registerTileClass(TEVoidResMinerT3.class, new MultiblockInfoContainerT3(definition3));
        IMultiblockInfoContainer.registerTileClass(TEVoidResMinerT4.class, new MultiblockInfoContainerT4(definition4));

        Logger.info("Register Solar Array Structure Info");
    }

    private static class MultiblockInfoContainerT1 implements IMultiblockInfoContainer<TEVoidResMinerT1> {

        private final IStructureDefinition<TEVoidResMinerT1> structure;

        public MultiblockInfoContainerT1(IStructureDefinition<TEVoidResMinerT1> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidResMinerT1 ctx, ExtendedFacing aSide) {
            int tier = ctx.getTier();

            this.structure.buildOrHints(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudget, ISurvivalBuildEnvironment env,
            TEVoidResMinerT1 ctx, ExtendedFacing aSide) {

            int built = 0;
            int tier = ctx.getTier();
            built = this.structure.survivalBuild(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

    private static class MultiblockInfoContainerT2 implements IMultiblockInfoContainer<TEVoidResMinerT2> {

        private final IStructureDefinition<TEVoidResMinerT2> structure;

        public MultiblockInfoContainerT2(IStructureDefinition<TEVoidResMinerT2> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidResMinerT2 ctx, ExtendedFacing aSide) {
            int tier = ctx.getTier();

            this.structure.buildOrHints(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudget, ISurvivalBuildEnvironment env,
            TEVoidResMinerT2 ctx, ExtendedFacing aSide) {

            int built = 0;
            int tier = ctx.getTier();
            built = this.structure.survivalBuild(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

    private static class MultiblockInfoContainerT3 implements IMultiblockInfoContainer<TEVoidResMinerT3> {

        private final IStructureDefinition<TEVoidResMinerT3> structure;

        public MultiblockInfoContainerT3(IStructureDefinition<TEVoidResMinerT3> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidResMinerT3 ctx, ExtendedFacing aSide) {
            int tier = ctx.getTier();

            this.structure.buildOrHints(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudget, ISurvivalBuildEnvironment env,
            TEVoidResMinerT3 ctx, ExtendedFacing aSide) {

            int built = 0;
            int tier = ctx.getTier();
            built = this.structure.survivalBuild(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

    private static class MultiblockInfoContainerT4 implements IMultiblockInfoContainer<TEVoidResMinerT4> {

        private final IStructureDefinition<TEVoidResMinerT4> structure;

        private MultiblockInfoContainerT4(IStructureDefinition<TEVoidResMinerT4> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidResMinerT4 ctx, ExtendedFacing aSide) {
            int tier = ctx.getTier();

            this.structure.buildOrHints(
                ctx,
                triggerStack,
                ctx.getStructurePieceName(),
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

        @Override
        public int survivalConstruct(ItemStack triggerStack, int elementBudget, ISurvivalBuildEnvironment env,
            TEVoidResMinerT4 ctx, ExtendedFacing aSide) {

            int built = 0;
            int tier = ctx.getTier();
            built = this.structure.survivalBuild(
                ctx,
                triggerStack,

                ctx.getStructurePieceName(),
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
