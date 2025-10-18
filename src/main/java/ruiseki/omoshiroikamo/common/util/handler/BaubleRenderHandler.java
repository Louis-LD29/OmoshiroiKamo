/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [Aug 27, 2014, 8:55:00 PM (GMT)]
 */
package ruiseki.omoshiroikamo.common.util.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ruiseki.omoshiroikamo.api.item.IBaubleRender;
import ruiseki.omoshiroikamo.config.GeneralConfig;

@EventBusSubscriber
public class BaubleRenderHandler {

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Specials.Post event) {
        if (!GeneralConfig.renderBaubles || event.entityLiving.getActivePotionEffect(Potion.invisibility) != null) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        InventoryBaubles inv = PlayerHandler.getPlayerBaubles(player);

        dispatchRenders(inv, event, IBaubleRender.RenderType.BODY);

        float yaw = player.prevRotationYawHead
            + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
        float yawOffset = player.prevRenderYawOffset
            + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
        float pitch = player.prevRotationPitch
            + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;

        GL11.glPushMatrix();
        GL11.glRotatef(yawOffset, 0, -1, 0);
        GL11.glRotatef(yaw - 270, 0, 1, 0);
        GL11.glRotatef(pitch, 0, 0, 1);
        dispatchRenders(inv, event, IBaubleRender.RenderType.HEAD);

        GL11.glPopMatrix();
    }

    private static void dispatchRenders(InventoryBaubles inv, RenderPlayerEvent event, IBaubleRender.RenderType type) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                Item item = stack.getItem();

                if (item instanceof IBaubleRender) {
                    GL11.glPushMatrix();
                    GL11.glColor4f(1F, 1F, 1F, 1F);
                    ((IBaubleRender) stack.getItem()).onPlayerBaubleRender(stack, event, type);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}
