package com.louis.test.client.render.meta;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;
import com.louis.test.api.mte.MetaTileEntity;
import com.louis.test.common.block.meta.TEMeta;
import com.louis.test.common.block.meta.energyConnector.AbstractMTEConnector;
import com.louis.test.common.plugin.compat.IECompat;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@ThreadSafeISBRH(perThread = true)
public class MTEISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static int renderMetaId;
    private static final TEMeta te = new TEMeta();

    public MTEISBRH() {
        renderMetaId = RenderingRegistry.getNextAvailableRenderId();
    }

    // ------- Block

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        int metadata = world.getBlockMetadata(x, y, z);
        MetaTileEntity mteType = MetaTileEntity.fromMeta(metadata);
        if (mteType == null || mteType.getRenderType() == 0) {
            return renderer.renderStandardBlock(block, x, y, z);
        }

        if (mteType.getRenderType() != -1) {
            return false;
        }

        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof TEMeta tile)) return false;

        if (tile.getMte() instanceof AbstractMTEConnector) {
            ClientUtils.renderAttachedConnections(tile);
            if (metadata == MetaTileEntity.INSULATOR.getBaseMeta()) {
                IECompat.handleStaticTileRenderer(tile);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderMetaId;
    }

    // ------- Item

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        int meta = item.getItemDamage();

        GL11.glPushMatrix();
        try {
            IECompat.handleStaticTileItemRenderer(te);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPopMatrix();
    }
}
