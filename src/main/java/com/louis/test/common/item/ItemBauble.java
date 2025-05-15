package com.louis.test.common.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.louis.test.core.helper.ItemNBTHelper;
import com.louis.test.core.helper.RenderHelper;
import com.louis.test.lib.LibMisc;
import com.louis.test.entity.EntityDoppleganger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public abstract class ItemBauble extends ItemMod implements IBauble {
    private static final String TAG_HASHCODE = "playerHashcode";
    private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
    private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";

    public ItemBauble(String name) {
        super();
        setMaxStackSize(1);
        setUnlocalizedName(name);
    }

    public ItemBauble(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if(!EntityDoppleganger.isTruePlayer(par3EntityPlayer))
            return par1ItemStack;

        if(canEquip(par1ItemStack, par3EntityPlayer)) {
            InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(par3EntityPlayer);
            for(int i = 0; i < baubles.getSizeInventory(); i++) {
                if(baubles.isItemValidForSlot(i, par1ItemStack)) {
                    ItemStack stackInSlot = baubles.getStackInSlot(i);
                    if(stackInSlot == null || ((IBauble) stackInSlot.getItem()).canUnequip(stackInSlot, par3EntityPlayer)) {
                        if(!par2World.isRemote) {
                            baubles.setInventorySlotContents(i, par1ItemStack.copy());
                            if(!par3EntityPlayer.capabilities.isCreativeMode)
                                par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null);
                        }

                        if(stackInSlot != null) {
                            ((IBauble) stackInSlot.getItem()).onUnequipped(stackInSlot, par3EntityPlayer);
                            return stackInSlot.copy();
                        }
                        break;
                    }
                }
            }
        }

        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if(GuiScreen.isShiftKeyDown())
            addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4);
        else addStringToTooltip(StatCollector.translateToLocal( LibMisc.MOD_ID + "misc.shiftinfo"), par3List);
    }

    public void addHiddenTooltip(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        BaubleType type = getBaubleType(par1ItemStack);
        addStringToTooltip(StatCollector.translateToLocal( LibMisc.MOD_ID + ".baubletype."+ type.name().toLowerCase()), par3List);

        String key = RenderHelper.getKeyDisplayString("Baubles Inventory");

        if(key != null)
            addStringToTooltip(StatCollector.translateToLocal( LibMisc.MOD_ID + ".baubletooltip").replaceAll("%key%", key), par3List);

    }

    void addStringToTooltip(String s, List<String> tooltip) {
        tooltip.add(s.replaceAll("&", "\u00a7"));
    }

    @Override
    public boolean canEquip(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if(getLastPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(stack, player);
            setLastPlayerHashcode(stack, player.hashCode());
        }
    }

    public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
        // NO-OP
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
        if(player != null) {
//            if(!player.worldObj.isRemote)
//                player.worldObj.playSoundAtEntity(player, "botania:equipBauble", 0.1F, 1.3F);
            onEquippedOrLoadedIntoWorld(stack, player);
            setLastPlayerHashcode(stack, player.hashCode());
        }
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        // NO-OP
    }

    public static UUID getBaubleUUID(ItemStack stack) {
        long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
        if(most == 0) {
            UUID uuid = UUID.randomUUID();
            ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_MOST, uuid.getMostSignificantBits());
            ItemNBTHelper.setLong(stack, TAG_BAUBLE_UUID_LEAST, uuid.getLeastSignificantBits());
            return getBaubleUUID(stack);
        }

        long least = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_LEAST, 0);
        return new UUID(most, least);
    }

    public static void setLastPlayerHashcode(ItemStack stack, int hash) {
        ItemNBTHelper.setInt(stack, TAG_HASHCODE, hash);
    }

    public static int getLastPlayerHashcode(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_HASHCODE, 0);
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
        return false;
    }
}
