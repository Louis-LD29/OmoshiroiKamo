package com.louis.test.common.block.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.block.ModBlocks;

public class MultiBlockStructure {

    private static final String[][] SHAPE3x3x3 = new String[][] { { "MMM", "MMM", "MMM" }, { "MMM", "MMM", "MMM" },
        { "MAM", "MMM", "MMM" } };
    public static final String SHAPE_NAME = "shape3x3x3";
    public static final int[] SHAPE_OFFSET = { 1, 2, 0 };
    public static IStructureDefinition<TileMultiBlock> STRUCTURE_DEFINITION;

    public static void registerAltarStructureInfo() {
        StructureDefinition.Builder<TileMultiBlock> builder = IStructureDefinition.builder();
        builder.addShape(SHAPE_NAME, transpose(SHAPE3x3x3))
            .addElement('A', ofBlock(ModBlocks.blockMultiBlock, 0))
            .addElement(
                'M',
                ofChain(
                    ofTileAdder(
                        (tileMultiBlock, tileEntity) -> tileMultiBlock.addToMachine((AbstractTE) tileEntity),
                        Blocks.iron_block,
                        0),
                    ofBlock(Blocks.iron_block, 0),
                    ofBlockAnyMeta(ModBlocks.blockEnergyInOut),
                    ofBlockAnyMeta(ModBlocks.blockItemInput),
                    ofBlockAnyMeta(ModBlocks.blockItemOutput),
                    ofBlockAnyMeta(ModBlocks.blockFluidInOut)));

        IStructureDefinition<TileMultiBlock> definition = builder.build();
        STRUCTURE_DEFINITION = definition;

        IMultiblockInfoContainer
            .registerTileClass(TileMultiBlock.class, new MultiBlockStructure.MultiBlockInfoContainer(definition));
    }

    public static class MultiBlockInfoContainer implements IMultiblockInfoContainer<TileMultiBlock> {

        private final IStructureDefinition<TileMultiBlock> structure;

        public MultiBlockInfoContainer(IStructureDefinition<TileMultiBlock> structure) {
            this.structure = structure;
        }

        @Override
        public void construct(ItemStack triggerStack, boolean hintsOnly, TileMultiBlock ctx, ExtendedFacing aSide) {
            structure.buildOrHints(
                ctx,
                triggerStack,
                SHAPE_NAME,
                ctx.getWorldObj(),
                ctx.getExtendedFacing(),
                ctx.xCoord,
                ctx.yCoord,
                ctx.zCoord,
                SHAPE_OFFSET[0],
                SHAPE_OFFSET[1],
                SHAPE_OFFSET[2],
                hintsOnly);
        }

        @Override
        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env,
            TileMultiBlock tileEntity, ExtendedFacing aSide) {
            return structure.survivalBuild(
                tileEntity,
                stackSize,
                SHAPE_NAME,
                tileEntity.getWorldObj(),
                tileEntity.getExtendedFacing(),
                tileEntity.xCoord,
                tileEntity.yCoord,
                tileEntity.zCoord,
                SHAPE_OFFSET[0],
                SHAPE_OFFSET[1],
                SHAPE_OFFSET[2],
                elementBudget,
                env,
                true);
        }

        @Override
        public String[] getDescription(ItemStack stackSize) {
            return new String[] {};
        }
    }
}
