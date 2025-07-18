package com.louis.test.client.render.block.connectable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.louis.test.common.block.energyConnector.*;
import com.louis.test.common.plugin.compat.IECompat;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ConnectableISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static int renderConnectableId;

    private static final TEInsulator insulator = new TEInsulator();
    private static final TEConnectorULV connectorULV = new TEConnectorULV();
    private static final TEConnectorLV connectorLV = new TEConnectorLV();
    private static final TEConnectorMV connectorMV = new TEConnectorMV();
    private static final TEConnectorHV connectorHV = new TEConnectorHV();
    private static final TEConnectorEV connectorEV = new TEConnectorEV();
    private static final TEConnectorIV connectorIV = new TEConnectorIV();
    private static final TETransformer transformer = new TETransformer();

    public ConnectableISBRH() {
        renderConnectableId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);

        ClientUtils.renderAttachedConnections(te);

        switch (meta) {
            case BlockConnectable.META_insulator:
                TEInsulator tileInsulator = (TEInsulator) te;
                IECompat.handleStaticTileRenderer(tileInsulator);
                return true;
            case BlockConnectable.META_connectorULV:
                TEConnectorULV tileConnectorULV = (TEConnectorULV) te;
                IECompat.handleStaticTileRenderer(tileConnectorULV);
                return true;
            case BlockConnectable.META_connectorLV:
                TEConnectorLV tileConnectorLV = (TEConnectorLV) te;
                IECompat.handleStaticTileRenderer(tileConnectorLV);
                return true;
            case BlockConnectable.META_connectorMV:
                TEConnectorMV tileConnectorMV = (TEConnectorMV) te;
                IECompat.handleStaticTileRenderer(tileConnectorMV);
                return true;
            case BlockConnectable.META_connectorHV:
                TEConnectorHV tileConnectorHV = (TEConnectorHV) te;
                IECompat.handleStaticTileRenderer(tileConnectorHV);
                return true;
            case BlockConnectable.META_connectorEV:
                TEConnectorEV tileConnectorEV = (TEConnectorEV) te;
                IECompat.handleStaticTileRenderer(tileConnectorEV);
                return true;
            case BlockConnectable.META_connectorIV:
                TEConnectorIV tileConnectorIV = (TEConnectorIV) te;
                IECompat.handleStaticTileRenderer(tileConnectorIV);
                return true;
            case BlockConnectable.META_transformer:
                TETransformer tileTransformer = (TETransformer) te;
                IECompat.handleStaticTileRenderer(tileTransformer);
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
        return renderConnectableId;
    }

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
            switch (meta) {
                case BlockConnectable.META_insulator:
                    IECompat.handleStaticTileItemRenderer(insulator);
                    break;
                case BlockConnectable.META_connectorULV:
                    IECompat.handleStaticTileItemRenderer(connectorULV);
                    break;
                case BlockConnectable.META_connectorLV:
                    IECompat.handleStaticTileItemRenderer(connectorLV);
                    break;
                case BlockConnectable.META_connectorMV:
                    IECompat.handleStaticTileItemRenderer(connectorMV);
                    break;
                case BlockConnectable.META_connectorHV:
                    IECompat.handleStaticTileItemRenderer(connectorHV);
                    break;
                case BlockConnectable.META_connectorEV:
                    IECompat.handleStaticTileItemRenderer(connectorEV);
                    break;
                case BlockConnectable.META_connectorIV:
                    IECompat.handleStaticTileItemRenderer(connectorIV);
                    break;
                case BlockConnectable.META_transformer:
                    IECompat.handleStaticTileItemRenderer(transformer);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPopMatrix();
    }
}
