package com.louis.test.common.item;

import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.enums.VoltageTier;
import com.louis.test.api.interfaces.IAdvancedTooltipProvider;
import com.louis.test.api.interfaces.energy.MaterialWireType;
import com.louis.test.common.TestCreativeTab;
import com.louis.test.common.plugin.compat.IE.IECompat;
import com.louis.test.core.helper.ItemNBTHelper;
import com.louis.test.core.lib.LibResources;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.IWireCoil;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.IEAchievements;
import blusunrize.immersiveengineering.common.util.Utils;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

@Optional.InterfaceList({ @Optional.Interface(
    iface = "blusunrize.immersiveengineering.common.util.IEAchievements",
    modid = "ImmersiveEngineering") })
public class ItemWireCoil extends Item implements IWireCoil, IAdvancedTooltipProvider {

    @SideOnly(Side.CLIENT)
    protected IIcon overlayIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon baseIcon;

    public static ItemWireCoil create() {
        ItemWireCoil wireCoil = new ItemWireCoil();
        wireCoil.init();
        return wireCoil;
    }

    protected ItemWireCoil() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(TestCreativeTab.INSTANCE);
        setUnlocalizedName(ModObject.itemWireCoil.unlocalisedName);
    }

    private void init() {
        GameRegistry.registerItem(this, ModObject.itemWireCoil.unlocalisedName);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        Material material = Material.fromMeta(meta);
        return super.getUnlocalizedName(stack) + "."
            + material.name()
                .toLowerCase();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (int i = 0; i < Material.values().length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderPasses(int metadata) {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return pass == 0 ? baseIcon : overlayIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return baseIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass == 1) {
            Material mat = Material.fromMeta(stack.getItemDamage() % 100);
            return mat.getColor();
        }
        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        baseIcon = iconRegister.registerIcon(LibResources.PREFIX_MOD + "wireCoil");
        overlayIcon = iconRegister.registerIcon(LibResources.PREFIX_MOD + "wireCoil_color");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
        if (stack.getTagCompound() != null && stack.getTagCompound()
            .hasKey("linkingPos")) {
            int[] link = stack.getTagCompound()
                .getIntArray("linkingPos");
            if (link != null && link.length > 3) list.add(
                StatCollector.translateToLocalFormatted(
                    LibResources.DESC_INFO + "attachedToDim",
                    link[1],
                    link[2],
                    link[3],
                    link[0]));
        }
    }

    @Override
    public WireType getWireType(ItemStack stack) {
        Material material = Material.fromMeta(stack.getItemDamage() % 100);
        return MaterialWireType.materialWireTypes.get(material);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntity).canConnect()) {
                TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);

                if (Loader.isModLoaded("ImmersiveEngineering")) {
                    if (getWireType(stack) instanceof MaterialWireType materialType) {
                        VoltageTier tier = materialType.getMaterial()
                            .getVoltageTier();
                        if (!IECompat.isCableTierCompatible(tileEntity, tier)) {
                            player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "wrongCable"));
                            return false;
                        }
                    }
                }

                if (!((IImmersiveConnectable) tileEntity).canConnectCable(getWireType(stack), target)) {
                    player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "wrongCable"));
                    return false;
                }

                if (!ItemNBTHelper.verifyExistance(stack, "linkingPos")) {
                    ItemNBTHelper.setIntArray(stack, "linkingPos", new int[] { world.provider.dimensionId, x, y, z });
                    target.writeToNBT(stack.getTagCompound());
                } else {
                    WireType type = getWireType(stack);
                    int[] pos = ItemNBTHelper.getIntArray(stack, "linkingPos", 0);
                    TileEntity tileEntityLinkingPos = world.getTileEntity(pos[1], pos[2], pos[3]);
                    int distance = (int) Math.ceil(
                        Math.sqrt(
                            (pos[1] - x) * (pos[1] - x) + (pos[2] - y) * (pos[2] - y) + (pos[3] - z) * (pos[3] - z)));
                    if (pos[0] != world.provider.dimensionId)
                        player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "wrongDimension"));
                    else if (pos[1] == x && pos[2] == y && pos[3] == z)
                        player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "sameConnection"));
                    else if (distance > type.getMaxLength())
                        player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "tooFar"));
                    else if (!(tileEntityLinkingPos instanceof IImmersiveConnectable)
                        || !((IImmersiveConnectable) tileEntityLinkingPos).canConnectCable(getWireType(stack), target))
                        player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "invalidPoint"));
                    else {
                        IImmersiveConnectable nodeHere = (IImmersiveConnectable) tileEntity;
                        IImmersiveConnectable nodeLink = (IImmersiveConnectable) tileEntityLinkingPos;
                        boolean connectionExists = false;
                        Set<Connection> outputs = ImmersiveNetHandler.INSTANCE
                            .getConnections(world, Utils.toCC(nodeHere));
                        if (outputs != null) for (Connection con : outputs) {
                            if (con.end.equals(Utils.toCC(nodeLink))) connectionExists = true;
                        }
                        if (connectionExists) player
                            .addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "connectionExists"));
                        else {
                            Vec3 rtOff0 = nodeHere.getRaytraceOffset(nodeLink)
                                .addVector(x, y, z);
                            Vec3 rtOff1 = nodeLink.getRaytraceOffset(nodeHere)
                                .addVector(pos[1], pos[2], pos[3]);
                            boolean canSee = Utils.canBlocksSeeOther(
                                world,
                                new ChunkCoordinates(x, y, z),
                                new ChunkCoordinates(pos[1], pos[2], pos[3]),
                                rtOff0,
                                rtOff1);
                            if (canSee) {
                                TargetingInfo targetLink = TargetingInfo.readFromNBT(stack.getTagCompound());
                                ImmersiveNetHandler.INSTANCE
                                    .addConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink), distance, type);
                                nodeHere.connectCable(type, target);
                                nodeLink.connectCable(type, targetLink);
                                IESaveData.setDirty(world.provider.dimensionId);
                                if (Loader.isModLoaded("ImmersiveEngineering")) {
                                    player.triggerAchievement(IEAchievements.connectWire);
                                }

                                if (!player.capabilities.isCreativeMode) stack.stackSize--;
                                ((TileEntity) nodeHere).markDirty();
                                world.addBlockEvent(x, y, z, ((TileEntity) nodeHere).getBlockType(), -1, 0);
                                world.markBlockForUpdate(x, y, z);
                                ((TileEntity) nodeLink).markDirty();
                                world.addBlockEvent(
                                    pos[1],
                                    pos[2],
                                    pos[3],
                                    ((TileEntity) nodeLink).getBlockType(),
                                    -1,
                                    0);
                                world.markBlockForUpdate(pos[1], pos[2], pos[3]);
                            } else
                                player.addChatMessage(new ChatComponentTranslation(LibResources.CHAT_WARN + "cantSee"));
                        }
                    }
                    ItemNBTHelper.remove(stack, "linkingPos");
                    ItemNBTHelper.remove(stack, "side");
                    ItemNBTHelper.remove(stack, "hitX");
                    ItemNBTHelper.remove(stack, "hitY");
                    ItemNBTHelper.remove(stack, "hitZ");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void addCommonEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }

    @Override
    public void addBasicEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        int meta = itemstack.getItemDamage();
        Material material = Material.fromMeta(meta);
        MaterialWireType materialWireType = MaterialWireType.materialWireTypes.get(material);
        list.add(String.format("§7Material:§f %s", material.getDisplayName()));
        list.add(
            String.format(
                "§7Voltage Tier:§f %s",
                material.getVoltageTier()
                    .getDisplayName()));
        list.add(String.format("§7Max Transfer:§f %,d RF/t", materialWireType.getTransferRate()));
        list.add(String.format("§7Max Length:§f %,d", materialWireType.getMaxLength()));
        list.add(String.format("§7Loss Ratio:§f %.3f", materialWireType.getLossRatio()));

    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {

    }
}
