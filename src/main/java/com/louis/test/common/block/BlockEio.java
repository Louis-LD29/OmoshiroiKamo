package com.louis.test.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.BlockEnder;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.block.machine.AbstractMachineEntity;

import crazypants.enderio.api.tool.ITool;
import crazypants.enderio.tool.ToolUtil;

public class BlockEio extends BlockEnder {

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass) {
        super(name, teClass);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    protected BlockEio(String name, Class<? extends TileEntityEio> teClass, Material mat) {
        super(name, teClass, mat);
        setCreativeTab(TestCreativeTab.INSTANCE);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float par7,
        float par8, float par9) {

        if (shouldWrench(world, x, y, z, entityPlayer, side)
            && ToolUtil.breakBlockWithTool(this, world, x, y, z, entityPlayer)) {
            return true;
        }
        TileEntity te = world.getTileEntity(x, y, z);

        ITool tool = ToolUtil.getEquippedTool(entityPlayer);
        if (tool != null && !entityPlayer.isSneaking()
            && tool.canUse(entityPlayer.getCurrentEquippedItem(), entityPlayer, x, y, z)) {
            if (te instanceof AbstractMachineEntity) {
                ((AbstractMachineEntity) te).toggleIoModeForFace(ForgeDirection.getOrientation(side));
                world.markBlockForUpdate(x, y, z);
                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, entityPlayer, side, par7, par8, par9);
    }
}
