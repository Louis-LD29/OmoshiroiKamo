package louis.omoshiroikamo.client.render.block.anvil;

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
import louis.omoshiroikamo.common.block.anvil.TEAnvil;

public class AnvilISBRH implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static int renderAnvilId;

    private static final TEAnvil anvil = new TEAnvil();

    public AnvilISBRH() {
        renderAnvilId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (!(te instanceof TEAnvil teAnvil)) {
            return false;
        }

        ClientUtils.handleStaticTileRenderer(teAnvil);

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderAnvilId;
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
        GL11.glPushMatrix();
        try {
            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslatef(0f, -0.2f, 0f);
            } else if (type == ItemRenderType.EQUIPPED) {
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
            ClientUtils.handleStaticTileItemRenderer(anvil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPopMatrix();
    }
}
