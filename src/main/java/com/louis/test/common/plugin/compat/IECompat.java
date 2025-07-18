package com.louis.test.common.plugin.compat;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.louis.test.api.enums.VoltageTier;
import com.louis.test.client.render.AbstractMTESR;
import com.louis.test.common.config.Config;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.EventHandler;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorHV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV;
import blusunrize.immersiveengineering.common.util.IELogger;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;

@Optional.InterfaceList({
    @Optional.Interface(
        iface = "blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV",
        modid = "ImmersiveEngineering"),
    @Optional.Interface(
        iface = "blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorMV",
        modid = "ImmersiveEngineering"),
    @Optional.Interface(
        iface = "blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorHV",
        modid = "ImmersiveEngineering"), })
public class IECompat {

    public static void preInit() {
        WireType.cableLossRatio = Config.cableLossRatio;
        WireType.cableTransferRate = Config.cableTransferRate;
        WireType.cableColouration = Config.cableColouration;
        WireType.cableLength = Config.cableLength;
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new EventHandler());
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

    public static void handleStaticTileRenderer(TileEntity tile) {
        handleStaticTileRenderer(tile, true);
    }

    public static void handleStaticTileRenderer(TileEntity tile, boolean translate) {
        TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);
        if (tesr instanceof AbstractMTESR) {
            Matrix4 matrixT = new Matrix4();
            if (translate) matrixT.translate(tile.xCoord, tile.yCoord, tile.zCoord);
            ((AbstractMTESR) tesr).renderStatic(tile, Tessellator.instance, matrixT, new Matrix4());
        }
    }

    public static void handleStaticTileItemRenderer(TileEntity tile) {
        handleStaticTileItemRenderer(tile, true);
    }

    public static void handleStaticTileItemRenderer(TileEntity tile, boolean translate) {
        TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);
        if (tesr instanceof AbstractMTESR) {
            Matrix4 matrixT = new Matrix4();
            if (translate) matrixT.translate(tile.xCoord, tile.yCoord, tile.zCoord);
            ((AbstractMTESR) tesr).renderItem(tile, Tessellator.instance, matrixT, new Matrix4());
        }
    }

    @Optional.Method(modid = "IC2")
    public static boolean isCableTierCompatible(TileEntity tile, VoltageTier tier) {
        if (!(tile instanceof TileEntityConnectorLV || tile instanceof TileEntityConnectorMV
            || tile instanceof TileEntityConnectorHV)) {
            return true;
        }

        return (tile.getClass() == TileEntityConnectorLV.class && tier.equals(VoltageTier.LV))
            || (tile.getClass() == TileEntityConnectorMV.class && tier.equals(VoltageTier.MV))
            || (tile.getClass() == TileEntityConnectorHV.class && tier.equals(VoltageTier.HV));
    }

}
