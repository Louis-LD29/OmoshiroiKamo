package louis.omoshiroikamo.common.plugin.compat;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.IELogger;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.client.ClientEventHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorHV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import louis.omoshiroikamo.api.enums.VoltageTier;
import louis.omoshiroikamo.client.render.AbstractMTESR;
import louis.omoshiroikamo.common.core.helper.Logger;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.EventHandler;
import louis.omoshiroikamo.shadow.blusunrize.immersiveengineering.immersiveengineering.common.util.chickenbones.Matrix4;

public class IECompat {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandler());
        Logger.info("Loaded IECompat");
    }

    public static void serverLoad() {
        if (ImmersiveNetHandler.INSTANCE == null) ImmersiveNetHandler.INSTANCE = new ImmersiveNetHandler();

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.SERVER) {
            World world = MinecraftServer.getServer()
                .getEntityWorld();
            if (!world.isRemote) {
                IELogger.info("WorldData loading");
                IESaveData worldData = (IESaveData) world.loadItemData(IESaveData.class, IESaveData.dataName);
                if (worldData == null) {
                    IELogger.info("WorldData not found");
                    worldData = new IESaveData(IESaveData.dataName);
                    world.setItemData(IESaveData.dataName, worldData);
                } else IELogger.info("WorldData retrieved");
                IESaveData.setInstance(world.provider.dimensionId, worldData);
            }
        }

    }

}
