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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.common.block.multiblock.solarArray.TESolarArray;
import louis.omoshiroikamo.common.util.lib.LibResources;

@SideOnly(Side.CLIENT)
public class SolarTESR extends TileEntitySpecialRenderer implements IItemRenderer {

    private IModelCustom model;
    private static final String MODEL = LibResources.PREFIX_MODEL + "controller_solar.obj";

    private static final ResourceLocation controllerBase = new ResourceLocation(
        LibResources.PREFIX_BLOCK + "basalt_normal.png");
    private static final ResourceLocation brain = new ResourceLocation(LibResources.PREFIX_BLOCK + "solar_tex2.png");

    private static final ResourceLocation TEX_IRON = new ResourceLocation("minecraft:textures/blocks/iron_block.png");
    private static final ResourceLocation TEX_GOLD = new ResourceLocation("minecraft:textures/blocks/gold_block.png");
    private static final ResourceLocation TEX_DIAMOND = new ResourceLocation(
        "minecraft:textures/blocks/diamond_block.png");
    private static final ResourceLocation TEX_EMERALD = new ResourceLocation(
        "minecraft:textures/blocks/emerald_block.png");

    public SolarTESR() {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(MODEL));
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if (!(te instanceof TESolarArray solarArray)) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);

        RenderUtil.bindTexture(controllerBase);
        model.renderOnly("ControllerBase");

        RenderUtil.bindTexture(brain);
        model.renderOnly("Brain");

        int meta = solarArray.getMeta();
        ResourceLocation tierTexture;
        switch (meta) {
            case 0:
                tierTexture = TEX_IRON;
                break;
            case 1:
                tierTexture = TEX_GOLD;
                break;
            case 2:
                tierTexture = TEX_DIAMOND;
                break;
            case 3:
            default:
                tierTexture = TEX_EMERALD;
                break;
        }

        RenderUtil.bindTexture(tierTexture);
        model.renderOnly("rod_1", "rod_2", "rod_3", "rod_4", "rod_5", "rod_6", "rod_7", "rod_8");
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
        model.renderOnly("ControllerBase");

        RenderUtil.bindTexture(brain);
        model.renderOnly("Brain");

        ResourceLocation tierTexture;
        switch (meta) {
            case 0:
                tierTexture = TEX_IRON;
                break;
            case 1:
                tierTexture = TEX_GOLD;
                break;
            case 2:
                tierTexture = TEX_DIAMOND;
                break;
            case 3:
            default:
                tierTexture = TEX_EMERALD;
                break;
        }

        RenderUtil.bindTexture(tierTexture);
        model.renderOnly("rod_1", "rod_2", "rod_3", "rod_4", "rod_5", "rod_6", "rod_7", "rod_8");
        GL11.glPopMatrix();
    }
}
