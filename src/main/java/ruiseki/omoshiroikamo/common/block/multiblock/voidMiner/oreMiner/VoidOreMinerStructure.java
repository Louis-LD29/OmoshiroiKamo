package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static ruiseki.omoshiroikamo.plugin.structureLib.StructureLibUtils.ofBlockAdderWithPos;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.TEVoidMiner;
import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.util.Logger;

public class VoidOreMinerStructure {

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
    public static IStructureDefinition<TEVoidOreMinerT1> STRUCTURE_DEFINITION_TIER_1;
    public static IStructureDefinition<TEVoidOreMinerT2> STRUCTURE_DEFINITION_TIER_2;
    public static IStructureDefinition<TEVoidOreMinerT3> STRUCTURE_DEFINITION_TIER_3;
    public static IStructureDefinition<TEVoidOreMinerT4> STRUCTURE_DEFINITION_TIER_4;

    @SuppressWarnings("unchecked")
    public static void registerStructureInfo() {
        StructureDefinition.Builder<TEVoidOreMinerT1> builder1 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidOreMinerT2> builder2 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidOreMinerT3> builder3 = StructureDefinition.builder();
        StructureDefinition.Builder<TEVoidOreMinerT4> builder4 = StructureDefinition.builder();

        builder1.addShape(STRUCTURE_TIER_1, transpose(SHAPE_TIER_1))
            .addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('Q', ofBlock(ModBlocks.VOID_ORE_MINER.get(), 0))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement('C', ofBlock(ModBlocks.LASER_CORE.get(), 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.LASER_LENS.get(), 0),
                    ofBlockAnyMeta(ModBlocks.LASER_LENS.get(), 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_ACCURACY.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 0),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 4),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 8)));

        builder2.addShape(STRUCTURE_TIER_2, transpose(SHAPE_TIER_2))
            .addElement('Q', ofBlock(ModBlocks.VOID_ORE_MINER.get(), 1))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement('C', ofBlock(ModBlocks.LASER_CORE.get(), 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.LASER_LENS.get(), 0),
                    ofBlockAnyMeta(ModBlocks.LASER_LENS.get(), 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_ACCURACY.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 1),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 5),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 9)));

        builder3.addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addElement('Q', ofBlock(ModBlocks.VOID_ORE_MINER.get(), 2))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement('C', ofBlock(ModBlocks.LASER_CORE.get(), 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.LASER_LENS.get(), 0),
                    ofBlockAnyMeta(ModBlocks.LASER_LENS.get(), 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_ACCURACY.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 2),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 6),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 10)));

        builder4.addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('Q', ofBlock(ModBlocks.VOID_ORE_MINER.get(), 3))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement('C', ofBlock(ModBlocks.LASER_CORE.get(), 0))
            .addElement(
                'L',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.LASER_LENS.get(), 0),
                    ofBlockAnyMeta(ModBlocks.LASER_LENS.get(), 0)))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TEVoidMiner::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_ACCURACY.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 3),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 7),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 11)));

        IStructureDefinition<TEVoidOreMinerT1> definition1 = builder1.build();
        STRUCTURE_DEFINITION_TIER_1 = definition1;
        IStructureDefinition<TEVoidOreMinerT2> definition2 = builder2.build();
        STRUCTURE_DEFINITION_TIER_2 = definition2;
        IStructureDefinition<TEVoidOreMinerT3> definition3 = builder3.build();
        STRUCTURE_DEFINITION_TIER_3 = definition3;
        IStructureDefinition<TEVoidOreMinerT4> definition4 = builder4.build();
        STRUCTURE_DEFINITION_TIER_4 = definition4;

        IMultiblockInfoContainer.registerTileClass(TEVoidOreMinerT1.class, new MultiblockInfoContainerT1(definition1));
        IMultiblockInfoContainer.registerTileClass(TEVoidOreMinerT2.class, new MultiblockInfoContainerT2(definition2));
        IMultiblockInfoContainer.registerTileClass(TEVoidOreMinerT3.class, new MultiblockInfoContainerT3(definition3));
        IMultiblockInfoContainer.registerTileClass(TEVoidOreMinerT4.class, new MultiblockInfoContainerT4(definition4));

        Logger.info("Register Solar Array Structure Info");
    }

    private static class MultiblockInfoContainerT1 implements IMultiblockInfoContainer<TEVoidOreMinerT1> {

        private final IStructureDefinition<TEVoidOreMinerT1> structure;

        public MultiblockInfoContainerT1(IStructureDefinition<TEVoidOreMinerT1> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidOreMinerT1 ctx, ExtendedFacing aSide) {
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
            TEVoidOreMinerT1 ctx, ExtendedFacing aSide) {

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

    private static class MultiblockInfoContainerT2 implements IMultiblockInfoContainer<TEVoidOreMinerT2> {

        private final IStructureDefinition<TEVoidOreMinerT2> structure;

        public MultiblockInfoContainerT2(IStructureDefinition<TEVoidOreMinerT2> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidOreMinerT2 ctx, ExtendedFacing aSide) {
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
            TEVoidOreMinerT2 ctx, ExtendedFacing aSide) {

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

    private static class MultiblockInfoContainerT3 implements IMultiblockInfoContainer<TEVoidOreMinerT3> {

        private final IStructureDefinition<TEVoidOreMinerT3> structure;

        public MultiblockInfoContainerT3(IStructureDefinition<TEVoidOreMinerT3> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidOreMinerT3 ctx, ExtendedFacing aSide) {
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
            TEVoidOreMinerT3 ctx, ExtendedFacing aSide) {

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

    private static class MultiblockInfoContainerT4 implements IMultiblockInfoContainer<TEVoidOreMinerT4> {

        private final IStructureDefinition<TEVoidOreMinerT4> structure;

        private MultiblockInfoContainerT4(IStructureDefinition<TEVoidOreMinerT4> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TEVoidOreMinerT4 ctx, ExtendedFacing aSide) {
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
            TEVoidOreMinerT4 ctx, ExtendedFacing aSide) {

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
