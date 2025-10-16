package louis.omoshiroikamo.client.render.block.nanoBotBeacon;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.enderio.core.client.render.RenderUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.TENanoBotBeacon;
import louis.omoshiroikamo.common.util.lib.LibResources;

@SideOnly(Side.CLIENT)
public class NanoBotBeaconTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    private IModelCustom model;
    private static final String MODEL = LibResources.PREFIX_MODEL + "nano_bot_beacon.obj";

    private static final ResourceLocation controllerBase = new ResourceLocation(
        LibResources.PREFIX_BLOCK + "basalt_normal.png");
    private static final ResourceLocation laser = new ResourceLocation(LibResources.PREFIX_BLOCK + "laser_core.png");

    private static final ResourceLocation TEX_IRON = new ResourceLocation(LibResources.PREFIX_BLOCK + "cont_tier.png");

    public NanoBotBeaconTESR() {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        TENanoBotBeacon nanoBotBeacon = (TENanoBotBeacon) te;
        int tier = nanoBotBeacon.getTier();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);

        RenderUtil.bindTexture(controllerBase);
        model.renderOnly("Base");

        RenderUtil.bindTexture(laser);
        model.renderOnly("Core_1", "Core_2");
        switch (tier) {
            case 1:
                GL11.glColor3f(1.0f, 0.85f, 0.3f);
                break;
            case 2:
                GL11.glColor3f(0.3f, 0.9f, 1.0f);
                break;
            case 3:
                GL11.glColor3f(0.063f, 0.369f, 0.318f);
                break;
            case 4:
                GL11.glColor3f(0.8f, 0.9f, 1.0f);
                break;
            default:
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                break;
        }

        RenderUtil.bindTexture(TEX_IRON);
        model.renderOnly("rod_1", "rod_2", "rod_3", "rod_4", "rod_5", "rod_6", "rod_7", "rod_8", "Anim");
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
        int meta = item.getItemDamage();

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0f, 0.5f);

        RenderUtil.bindTexture(controllerBase);
        model.renderOnly("Base");

        RenderUtil.bindTexture(laser);
        model.renderOnly("Core_1", "Core_2");

        switch (meta) {
            case 0:
                GL11.glColor3f(1.0f, 0.85f, 0.3f);
                break;
            case 1:
                GL11.glColor3f(0.3f, 0.9f, 1.0f);
                break;
            case 2:
                GL11.glColor3f(0.063f, 0.369f, 0.318f);
                break;
            case 3:
                GL11.glColor3f(0.8f, 0.9f, 1.0f);
                break;
            default:
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                break;
        }

        RenderUtil.bindTexture(TEX_IRON);
        model.renderOnly("rod_1", "rod_2", "rod_3", "rod_4", "rod_5", "rod_6", "rod_7", "rod_8", "Anim");
        GL11.glPopMatrix();
    }
}
