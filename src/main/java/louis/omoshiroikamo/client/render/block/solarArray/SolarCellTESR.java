package louis.omoshiroikamo.client.render.block.solarArray;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.RenderUtil;

import louis.omoshiroikamo.common.util.lib.LibResources;

public class SolarCellTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    private IModelCustom model;
    private static final String MODEL = LibResources.PREFIX_MODEL + "solar_cell.obj";
    private static final ResourceLocation texture = new ResourceLocation(LibResources.PREFIX_BLOCK + "solar_cell.png");

    public SolarCellTESR() {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        RenderUtil.bindTexture(texture);
        model.renderAll();
        GL11.glPopMatrix();
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
        GL11.glTranslatef(0.5f, 0f, 0.5f);
        RenderUtil.bindTexture(texture);
        model.renderAll();
        GL11.glPopMatrix();
    }
}
