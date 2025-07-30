package louis.omoshiroikamo.client.render.block.connectable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import louis.omoshiroikamo.client.ClientUtils;
import louis.omoshiroikamo.common.block.energyConnector.BlockConnectable;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorEV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorHV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorIV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorLV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorMV;
import louis.omoshiroikamo.common.block.energyConnector.TEConnectorULV;
import louis.omoshiroikamo.common.block.energyConnector.TEInsulator;
import louis.omoshiroikamo.common.block.energyConnector.TETransformer;

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
                ClientUtils.handleStaticTileRenderer(tileInsulator);
                return true;
            case BlockConnectable.META_connectorULV:
                TEConnectorULV tileConnectorULV = (TEConnectorULV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorULV);
                return true;
            case BlockConnectable.META_connectorLV:
                TEConnectorLV tileConnectorLV = (TEConnectorLV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorLV);
                return true;
            case BlockConnectable.META_connectorMV:
                TEConnectorMV tileConnectorMV = (TEConnectorMV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorMV);
                return true;
            case BlockConnectable.META_connectorHV:
                TEConnectorHV tileConnectorHV = (TEConnectorHV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorHV);
                return true;
            case BlockConnectable.META_connectorEV:
                TEConnectorEV tileConnectorEV = (TEConnectorEV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorEV);
                return true;
            case BlockConnectable.META_connectorIV:
                TEConnectorIV tileConnectorIV = (TEConnectorIV) te;
                ClientUtils.handleStaticTileRenderer(tileConnectorIV);
                return true;
            case BlockConnectable.META_transformer:
                TETransformer tileTransformer = (TETransformer) te;
                ClientUtils.handleStaticTileRenderer(tileTransformer);
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
                    ClientUtils.handleStaticTileItemRenderer(insulator);
                    break;
                case BlockConnectable.META_connectorULV:
                    ClientUtils.handleStaticTileItemRenderer(connectorULV);
                    break;
                case BlockConnectable.META_connectorLV:
                    ClientUtils.handleStaticTileItemRenderer(connectorLV);
                    break;
                case BlockConnectable.META_connectorMV:
                    ClientUtils.handleStaticTileItemRenderer(connectorMV);
                    break;
                case BlockConnectable.META_connectorHV:
                    ClientUtils.handleStaticTileItemRenderer(connectorHV);
                    break;
                case BlockConnectable.META_connectorEV:
                    ClientUtils.handleStaticTileItemRenderer(connectorEV);
                    break;
                case BlockConnectable.META_connectorIV:
                    ClientUtils.handleStaticTileItemRenderer(connectorIV);
                    break;
                case BlockConnectable.META_transformer:
                    ClientUtils.handleStaticTileItemRenderer(transformer);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPopMatrix();
    }
}
