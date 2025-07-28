package louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import louis.omoshiroikamo.common.core.helper.ItemNBTHelper;
import louis.omoshiroikamo.common.core.lib.LibResources;
import louis.omoshiroikamo.common.item.ModItems;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.api.energy.WireType;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.client.models.ModelIEObj;

public class ClientEventHandler {

    public static IIcon iconItemBlank;
    public static int itemSheetWidth;
    public static int itemSheetHeight;

    @SubscribeEvent()
    public void textureStich(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) {
            WireType.iconDefaultWire = event.map.registerIcon(LibResources.PREFIX_MOD.toLowerCase() + "wire");
        }
        if (event.map.getTextureType() == 1) {
            iconItemBlank = event.map.registerIcon(LibResources.PREFIX_MOD.toLowerCase() + "white");
        }

    }

    @SubscribeEvent()
    public void textureStich(TextureStitchEvent.Post event) {
        if (event.map.getTextureType() == 0) for (ModelIEObj modelIE : ModelIEObj.existingStaticRenders) {
            WavefrontObject model = modelIE.rebindModel();
            rebindUVsToIcon(model, modelIE);
        }
        if (event.map.getTextureType() == 1) {
            itemSheetWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            itemSheetHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        }
    }

    void rebindUVsToIcon(WavefrontObject model, ModelIEObj modelIE) {
        for (GroupObject groupObject : model.groupObjects) {
            IIcon icon = modelIE.getBlockIcon(groupObject.name);
            if (icon == null) continue;
            float minU = icon.getInterpolatedU(0);
            float sizeU = icon.getInterpolatedU(16) - minU;
            float minV = icon.getInterpolatedV(0);
            float sizeV = icon.getInterpolatedV(16) - minV;
            float baseOffsetU = (16f / icon.getIconWidth()) * .0005F;
            float baseOffsetV = (16f / icon.getIconHeight()) * .0005F;
            for (Face face : groupObject.faces) {
                float averageU = 0F;
                float averageV = 0F;
                if (face.textureCoordinates != null && face.textureCoordinates.length > 0) {
                    for (int i = 0; i < face.textureCoordinates.length; ++i) {
                        averageU += face.textureCoordinates[i].u;
                        averageV += face.textureCoordinates[i].v;
                    }
                    averageU = averageU / face.textureCoordinates.length;
                    averageV = averageV / face.textureCoordinates.length;
                }

                for (int i = 0; i < face.vertices.length; ++i) {
                    float offsetU, offsetV;
                    TextureCoordinate textureCoordinate = face.textureCoordinates[i];
                    offsetU = baseOffsetU;
                    offsetV = baseOffsetV;
                    if (face.textureCoordinates[i].u > averageU) offsetU = -offsetU;
                    if (face.textureCoordinates[i].v > averageV) offsetV = -offsetV;

                    face.textureCoordinates[i] = new TextureCoordinate(
                        minU + sizeU * (textureCoordinate.u + offsetU),
                        minV + sizeV * (textureCoordinate.v + offsetV));
                }
            }
        }
    }

    @SubscribeEvent()
    public void onRenderOverlayPost(RenderGameOverlayEvent.Post event) {
        if (ClientUtils.mc().thePlayer != null && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            EntityPlayer player = ClientUtils.mc().thePlayer;
            if (player.getCurrentEquippedItem() != null) {
                ItemStack equipped = player.getCurrentEquippedItem();
                if (OreDictionary.itemMatches(
                    new ItemStack(ModItems.itemWireCoil, 1, OreDictionary.WILDCARD_VALUE),
                    equipped,
                    false)) {
                    if (ItemNBTHelper.verifyExistance(equipped, "linkingPos")) {
                        int[] link = ItemNBTHelper.getIntArray(equipped, "linkingPos", 0);
                        if (link != null && link.length > 3) {
                            String s = StatCollector.translateToLocalFormatted(
                                LibResources.DESC_INFO + "attachedTo",
                                link[1],
                                link[2],
                                link[3]);
                            ClientUtils.font()
                                .drawString(
                                    s,
                                    event.resolution.getScaledWidth() / 2 - ClientUtils.font()
                                        .getStringWidth(s) / 2,
                                    event.resolution.getScaledHeight() - GuiIngameForge.left_height - 10,
                                    WireType.ELECTRUM.getColour(null),
                                    true);
                        }
                    }
                }
            }
        }
    }

}
