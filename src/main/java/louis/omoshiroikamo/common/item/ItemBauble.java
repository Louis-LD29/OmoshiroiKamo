package louis.omoshiroikamo.common.item;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.client.render.RenderHelper;
import louis.omoshiroikamo.common.entity.EntityDoppleganger;
import louis.omoshiroikamo.common.util.helper.ItemNBTHelper;
import louis.omoshiroikamo.common.util.lib.LibResources;
import tconstruct.library.accessory.IAccessory;

@Optional.InterfaceList({ @Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"),
    @Optional.Interface(iface = "tconstruct.library.accessory.IAccessory", modid = "TConstruct") })
public abstract class ItemBauble extends ItemOK implements IBauble, IAccessory {

    private static final String TAG_HASHCODE = "playerHashcode";
    private static final String TAG_BAUBLE_UUID_MOST = "baubleUUIDMost";
    private static final String TAG_BAUBLE_UUID_LEAST = "baubleUUIDLeast";

    protected boolean disableRightClickEquip;

    public ItemBauble(String name, boolean disableRightClickEquip) {
        super(name);
        this.disableRightClickEquip = disableRightClickEquip;
        setMaxStackSize(1);
    }

    public ItemBauble(String name) {
        this(name, true);
    }

    public static UUID getBaubleUUID(ItemStack stack) {
        long most = ItemNBTHelper.getLong(stack, TAG_BAUBLE_UUID_MOST, 0);
        if (most == 0) {
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

    public void disableRightClickEquip() {
        this.disableRightClickEquip = false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (disableRightClickEquip) {
            return itemStack;
        }

        if (!EntityDoppleganger.isTruePlayer(player)) return itemStack;

        if (canEquip(itemStack, player)) {
            InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                if (baubles.isItemValidForSlot(i, itemStack)) {
                    ItemStack stackInSlot = baubles.getStackInSlot(i);
                    if (stackInSlot == null || ((IBauble) stackInSlot.getItem()).canUnequip(stackInSlot, player)) {
                        if (!world.isRemote) {
                            baubles.setInventorySlotContents(i, itemStack.copy());
                            if (!player.capabilities.isCreativeMode)
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }

                        if (stackInSlot != null) {
                            ((IBauble) stackInSlot.getItem()).onUnequipped(stackInSlot, player);
                            return stackInSlot.copy();
                        }
                        break;
                    }
                }
            }
        }

        return itemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (GuiScreen.isShiftKeyDown()) addHiddenTooltip(itemStack, player, list, par4);
        else addStringToTooltip(StatCollector.translateToLocal("misc.shiftinfo"), list);
    }

    public void addHiddenTooltip(ItemStack itemStack, EntityPlayer player, List par3List, boolean par4) {
        BaubleType type = getBaubleType(itemStack);
        addStringToTooltip(
            StatCollector.translateToLocal(
                "baubletype." + type.name()
                    .toLowerCase()),
            par3List);

        String key = RenderHelper.getKeyDisplayString("Baubles Inventory");

        if (key != null) addStringToTooltip(
            StatCollector.translateToLocal("baubletooltip")
                .replaceAll("%key%", key),
            par3List);

    }

    void addStringToTooltip(String s, List tooltip) {
        tooltip.add(s.replaceAll("&", "\u00a7"));
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
        return false;
    }

    // Bauble

    @Override
    @Optional.Method(modid = "Baubles")
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    @Optional.Method(modid = "Baubles")
    public boolean canEquip(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "Baubles")
    public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    @Optional.Method(modid = "Baubles")
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
        if (player != null) {
            if (!player.worldObj.isRemote)
                player.worldObj.playSoundAtEntity(player, LibResources.PREFIX_MOD + "equipBauble", 0.1F, 1.3F);
            onEquippedOrLoadedIntoWorld(stack, player);

            setLastPlayerHashcode(stack, player.hashCode());

        }
    }

    public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
        // NO-OP
    }

    @Override
    @Optional.Method(modid = "Baubles")
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        // NO-OP
    }

    @Override
    @Optional.Method(modid = "Baubles")
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if (getLastPlayerHashcode(stack) != player.hashCode()) {
            onEquippedOrLoadedIntoWorld(stack, player);
            setLastPlayerHashcode(stack, player.hashCode());
        }
    }

    // TConstruct

    @Override
    @Optional.Method(modid = "TConstruct")
    public boolean canEquipAccessory(ItemStack itemStack, int slot) {
        return slot == 0;
    }

}
