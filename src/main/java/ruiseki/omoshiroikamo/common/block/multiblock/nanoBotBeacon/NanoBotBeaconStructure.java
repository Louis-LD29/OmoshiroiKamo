package ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon;

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

import ruiseki.omoshiroikamo.common.init.ModBlocks;
import ruiseki.omoshiroikamo.common.util.Logger;

public class NanoBotBeaconStructure {

    // spotless:off
    public static final String STRUCTURE_TIER_1 = "tier1";
    public static final String[][] SHAPE_TIER_1 = new String[][]{
        {
            "     ",
            " F F ",
            "  Q  ",
            " F F ",
            "     "
        },
        {
            "F   F",
            " AFA ",
            " F F ",
            " AFA ",
            "F   F"
        },
        {
            "PF FP",
            "FPPPF",
            " P P ",
            "FPPPF",
            "PF FP"
        }};
    public static final String STRUCTURE_TIER_2 = "tier2";
    public static final String[][] SHAPE_TIER_2 = new String[][]{
        {
            "       ",
            "       ",
            "  F F  ",
            "   Q   ",
            "  F F  ",
            "       ",
            "       "
        },
        {
            "       ",
            " F   F ",
            "  AFA  ",
            "  F F  ",
            "  AFA  ",
            " F   F ",
            "       "
        },
        {
            "F     F",
            " AF FA ",
            " FPPPF ",
            "  P P  ",
            " FPPPF ",
            " AF FA ",
            "F     F"
        },
        {
            "PF   FP",
            "FPPFPPF",
            " P   P ",
            " F   F ",
            " P   P ",
            "FPPFPPF",
            "PF   FP"
        }};
    public static final String STRUCTURE_TIER_3 = "tier3";
    public static final String[][] SHAPE_TIER_3 = new String[][]{
        {
            "         ",
            "         ",
            "         ",
            "   F F   ",
            "    Q    ",
            "   F F   ",
            "         ",
            "         ",
            "         "
        },
        {
            "         ",
            "         ",
            "  F   F  ",
            "   AFA   ",
            "   F F   ",
            "   AFA   ",
            "  F   F  ",
            "         ",
            "         "
        },
        {
            "         ",
            " F     F ",
            "  AF FA  ",
            "  FPPPF  ",
            "   P P   ",
            "  FPPPF  ",
            "  AF FA  ",
            " F     F ",
            "         "
        },
        {
            "F       F",
            " AF   FA ",
            " FPPFPPF ",
            "  P   P  ",
            "  F   F  ",
            "  P   P  ",
            " FPPFPPF ",
            " AF   FA ",
            "F       F"
        },
        {
            "PF     FP",
            "FPPF FPPF",
            " P  P  P ",
            " F     F ",
            "  P   P  ",
            " F     F ",
            " P  P  P ",
            "FPPF FPPF",
            "PF     FP"
        }};
    public static final String STRUCTURE_TIER_4 = "tier4";
    public static final String[][] SHAPE_TIER_4 = new String[][]{
        {
            "           ",
            "           ",
            "           ",
            "           ",
            "    F F    ",
            "     Q     ",
            "    F F    ",
            "           ",
            "           ",
            "           ",
            "           "
        },
        {
            "           ",
            "           ",
            "           ",
            "   F   F   ",
            "    AFA    ",
            "    F F    ",
            "    AFA    ",
            "   F   F   ",
            "           ",
            "           ",
            "           "
        },
        {
            "           ",
            "           ",
            "  F     F  ",
            "   AF FA   ",
            "   FPPPF   ",
            "    P P    ",
            "   FPPPF   ",
            "   AF FA   ",
            "  F     F  ",
            "           ",
            "           "
        },
        {
            "           ",
            " F       F ",
            "  AF   FA  ",
            "  FPPFPPF  ",
            "   P   P   ",
            "   F   F   ",
            "   P   P   ",
            "  FPPFPPF  ",
            "  AF   FA  ",
            " F       F ",
            "           "
        },
        {
            "F         F",
            " AF     FA ",
            " FPPF FPPF ",
            "  P  P  P  ",
            "  F     F  ",
            "   P   P   ",
            "  F     F  ",
            "  P  P  P  ",
            " FPPF FPPF ",
            " AF     FA ",
            "F         F"
        },
        {
            "PF       FP",
            "FPPF   FPPF",
            " P  PFP  P ",
            " F       F ",
            "  P     P  ",
            "  F     F  ",
            "  P     P  ",
            " F       F ",
            " P  PFP  P ",
            "FPPF   FPPF",
            "PF       FP"
        }};
    // spotless:on

    public static final int[][] TIER_OFFSET = { { 2, 0, 2 }, { 3, 0, 3 }, { 4, 0, 4 }, { 5, 0, 5 } };
    public static IStructureDefinition<TENanoBotBeaconT1> STRUCTURE_DEFINITION_TIER_1;
    public static IStructureDefinition<TENanoBotBeaconT2> STRUCTURE_DEFINITION_TIER_2;
    public static IStructureDefinition<TENanoBotBeaconT3> STRUCTURE_DEFINITION_TIER_3;
    public static IStructureDefinition<TENanoBotBeaconT4> STRUCTURE_DEFINITION_TIER_4;

    @SuppressWarnings("unchecked")
    public static void registerStructureInfo() {
        StructureDefinition.Builder<TENanoBotBeaconT1> builder1 = StructureDefinition.builder();
        StructureDefinition.Builder<TENanoBotBeaconT2> builder2 = StructureDefinition.builder();
        StructureDefinition.Builder<TENanoBotBeaconT3> builder3 = StructureDefinition.builder();
        StructureDefinition.Builder<TENanoBotBeaconT4> builder4 = StructureDefinition.builder();

        builder1.addShape(STRUCTURE_TIER_1, transpose(SHAPE_TIER_1))
            .addElement('Q', ofBlock(ModBlocks.NANO_BOT_BEACON.get(), 0))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TENanoBotBeacon::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FIRE_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FLIGHT.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NIGHT_VISION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_WATER_BREATHING.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_STRENGTH.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_HASTE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_REGENERATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SATURATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_JUMP_BOOST.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 0),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 4),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 8)));

        builder2.addShape(STRUCTURE_TIER_2, transpose(SHAPE_TIER_2))
            .addElement('Q', ofBlock(ModBlocks.NANO_BOT_BEACON.get(), 1))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TENanoBotBeacon::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FIRE_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FLIGHT.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NIGHT_VISION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_WATER_BREATHING.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_STRENGTH.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_HASTE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_REGENERATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SATURATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_JUMP_BOOST.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 1),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 5),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 9)));

        builder3.addShape(STRUCTURE_TIER_3, transpose(SHAPE_TIER_3))
            .addElement('Q', ofBlock(ModBlocks.NANO_BOT_BEACON.get(), 2))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TENanoBotBeacon::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FIRE_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FLIGHT.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NIGHT_VISION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_WATER_BREATHING.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_STRENGTH.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_HASTE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_REGENERATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SATURATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_JUMP_BOOST.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 2),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 6),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 10)));

        builder4.addShape(STRUCTURE_TIER_4, transpose(SHAPE_TIER_4))
            .addElement('Q', ofBlock(ModBlocks.NANO_BOT_BEACON.get(), 3))
            .addElement('P', ofBlockAnyMeta(ModBlocks.MACHINE_BASE.get(), 0))
            .addElement(
                'A',
                ofChain(
                    ofBlockAdderWithPos(TENanoBotBeacon::addToMachine, ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NULL.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FIRE_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_FLIGHT.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_NIGHT_VISION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_WATER_BREATHING.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_STRENGTH.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_HASTE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_REGENERATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SATURATION.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_RESISTANCE.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_JUMP_BOOST.get(), 0),
                    ofBlock(ModBlocks.MODIFIER_SPEED.get(), 0)))
            .addElement(
                'F',
                ofChain(
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 3),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 7),
                    ofBlock(ModBlocks.STRUCTURE_FRAME.get(), 11)));

        IStructureDefinition<TENanoBotBeaconT1> definition1 = builder1.build();
        STRUCTURE_DEFINITION_TIER_1 = definition1;
        IStructureDefinition<TENanoBotBeaconT2> definition2 = builder2.build();
        STRUCTURE_DEFINITION_TIER_2 = definition2;
        IStructureDefinition<TENanoBotBeaconT3> definition3 = builder3.build();
        STRUCTURE_DEFINITION_TIER_3 = definition3;
        IStructureDefinition<TENanoBotBeaconT4> definition4 = builder4.build();
        STRUCTURE_DEFINITION_TIER_4 = definition4;

        IMultiblockInfoContainer.registerTileClass(TENanoBotBeaconT1.class, new MultiblockInfoContainerT1(definition1));
        IMultiblockInfoContainer.registerTileClass(TENanoBotBeaconT2.class, new MultiblockInfoContainerT2(definition2));
        IMultiblockInfoContainer.registerTileClass(TENanoBotBeaconT3.class, new MultiblockInfoContainerT3(definition3));
        IMultiblockInfoContainer.registerTileClass(TENanoBotBeaconT4.class, new MultiblockInfoContainerT4(definition4));

        Logger.info("Register Solar Array Structure Info");
    }

    private static class MultiblockInfoContainerT1 implements IMultiblockInfoContainer<TENanoBotBeaconT1> {

        private final IStructureDefinition<TENanoBotBeaconT1> structure;

        public MultiblockInfoContainerT1(IStructureDefinition<TENanoBotBeaconT1> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TENanoBotBeaconT1 ctx, ExtendedFacing aSide) {
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
            TENanoBotBeaconT1 ctx, ExtendedFacing aSide) {
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

    private static class MultiblockInfoContainerT2 implements IMultiblockInfoContainer<TENanoBotBeaconT2> {

        private final IStructureDefinition<TENanoBotBeaconT2> structure;

        public MultiblockInfoContainerT2(IStructureDefinition<TENanoBotBeaconT2> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TENanoBotBeaconT2 ctx, ExtendedFacing aSide) {
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
            TENanoBotBeaconT2 ctx, ExtendedFacing aSide) {
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

    private static class MultiblockInfoContainerT3 implements IMultiblockInfoContainer<TENanoBotBeaconT3> {

        private final IStructureDefinition<TENanoBotBeaconT3> structure;

        public MultiblockInfoContainerT3(IStructureDefinition<TENanoBotBeaconT3> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TENanoBotBeaconT3 ctx, ExtendedFacing aSide) {
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
            TENanoBotBeaconT3 ctx, ExtendedFacing aSide) {
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

    private static class MultiblockInfoContainerT4 implements IMultiblockInfoContainer<TENanoBotBeaconT4> {

        private final IStructureDefinition<TENanoBotBeaconT4> structure;

        public MultiblockInfoContainerT4(IStructureDefinition<TENanoBotBeaconT4> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TENanoBotBeaconT4 ctx, ExtendedFacing aSide) {
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
            TENanoBotBeaconT4 ctx, ExtendedFacing aSide) {
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
