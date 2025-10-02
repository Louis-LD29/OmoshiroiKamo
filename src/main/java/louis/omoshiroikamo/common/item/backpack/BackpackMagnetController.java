package louis.omoshiroikamo.common.item.backpack;

import static louis.omoshiroikamo.config.MagnetConfig.magnetConfig;
import static louis.omoshiroikamo.plugin.botania.BotaniaUtil.hasSolegnoliaAround;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.client.gui.BackpackGui;
import louis.omoshiroikamo.client.gui.modularui2.handler.UpgradeItemStackHandler;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.network.PacketMagnetState;
import louis.omoshiroikamo.plugin.baubles.BaublesUtil;
import vazkii.botania.common.core.helper.Vector3;

@EventBusSubscriber()
public class BackpackMagnetController {

    private static final String TAG_COOLDOWN = "cooldown";
    protected static Random rand = new Random();
    private static List<Item> blacklist = null;

    private static final double collisionDistanceSq = 1.25 * 1.25;

    public BackpackMagnetController() {
        PacketHandler.INSTANCE
            .registerMessage(PacketMagnetState.class, PacketMagnetState.class, PacketHandler.nextID(), Side.SERVER);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        ActiveMagnet mag = getActiveMagnet(event.player);
        if (mag != null && event.player.getHealth() > 0f) {
            doHoover(event.player, mag);
        }
    }

    @SubscribeEvent
    public static void onTossItemWrapper(ItemTossEvent event) {
        onTossItem(event);
    }

    public static void onTossItem(ItemTossEvent event) {
        ItemStack[] inv = event.player.inventory.mainInventory;
        for (ItemStack stack : inv) {
            if (stack == null || !(stack.getItem() instanceof ItemBackpack)) {
                continue;
            }
            setCooldown(stack, 100);
        }
        InventoryBaubles baubleInv = PlayerHandler.getPlayerBaubles(event.player);
        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
            ItemStack stack = baubleInv.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBackpack) {
                setCooldown(stack, 100);
                baubleInv.markDirty();
            }
        }
    }

    private static ActiveMagnet getActiveMagnet(EntityPlayer player) {
        ItemStack[] inv = player.inventory.mainInventory;
        int maxSlot = magnetConfig.magnetAllowInMainInventory ? 4 * 9 : 9;
        InventoryBaubles baubleInv = PlayerHandler.getPlayerBaubles(player);
        UpgradeItemStackHandler upgradeHandler = new UpgradeItemStackHandler(BackpackGui.upgradeSlot);

        for (int i = 0; i < maxSlot; i++) {
            ItemStack stack = inv[i];
            if (stack == null || !(stack.getItem() instanceof ItemBackpack)) {
                continue;
            }

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey("BackpackUpgrade")) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag("BackpackUpgrade"));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemMagnetUpgrade) {
                    return new ActiveMagnet(stack, slot);
                }
            }
        }

        if (!magnetConfig.magnetAllowInBaublesSlot) {
            return null;
        }
        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
            ItemStack stack = baubleInv.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemBackpack)) {
                continue;
            }

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null || !tag.hasKey("BackpackUpgrade")) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag("BackpackUpgrade"));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemMagnetUpgrade) {
                    return new ActiveMagnet(stack, slot);
                }
            }
        }

        return null;
    }

    public static void doHoover(EntityPlayer player, ActiveMagnet mag) {
        int cooldown = getCooldown(mag.item);

        initBlacklist(mag);

        if (cooldown > 0) {
            setCooldown(mag.item, cooldown - 1);
            return;
        }

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
            player.posX - magnetConfig.magnetRange,
            player.posY - magnetConfig.magnetRange,
            player.posZ - magnetConfig.magnetRange,
            player.posX + magnetConfig.magnetRange,
            player.posY + magnetConfig.magnetRange,
            player.posZ + magnetConfig.magnetRange);

        List<Entity> interestingItems = selectEntitiesWithinAABB(player.worldObj, aabb);

        if (interestingItems != null) {
            int pulled = 0;
            for (Entity entity : interestingItems) {
                double x = player.posX + 0.5D - entity.posX;
                double y = player.posY + 1D - entity.posY;
                double z = player.posZ + 0.5D - entity.posZ;

                double distance = x * x + y * y + z * z;
                if (distance < collisionDistanceSq) {
                    onCollideWithPlayer(player, entity);
                } else {
                    if (pulled > 200) {
                        break;
                    }
                    Vector3 target = new Vector3(
                        player.posX,
                        player.posY - (player.worldObj.isRemote ? 1.62 : 0) + 0.75,
                        player.posZ);
                    setEntityMotionFromVector(entity, target, 0.45F);
                    pulled++;
                }
            }

            setCooldown(mag.item, 2);
        }
    }

    private static void initBlacklist(ActiveMagnet mag) {
        Set<String> names = new HashSet<>();
        names.addAll(Arrays.asList(magnetConfig.magnetBlacklist));
        names.addAll(BackpackGui.getBlacklist(mag.item));

        List<Item> newBlacklist = new ArrayList<>();

        for (String name : names) {
            addItemToBlacklist(newBlacklist, name);
        }

        blacklist = newBlacklist;
    }

    private static void addItemToBlacklist(List<Item> list, String name) {
        String[] parts = name.split(":");
        if (parts.length == 2) {
            Item item = GameRegistry.findItem(parts[0], parts[1]);
            if (item != null) {
                if (!list.contains(item)) {
                    list.add(item);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Entity> selectEntitiesWithinAABB(World world, AxisAlignedBB bb) {
        List<Entity> arraylist = null;

        int itemsRemaining = magnetConfig.magnetMaxItems;
        if (itemsRemaining <= 0) {
            itemsRemaining = Integer.MAX_VALUE;
        }

        final int minChunkX = MathHelper.floor_double((bb.minX) / 16.0D);
        final int maxChunkX = MathHelper.floor_double((bb.maxX) / 16.0D);
        final int minChunkZ = MathHelper.floor_double((bb.minZ) / 16.0D);
        final int maxChunkZ = MathHelper.floor_double((bb.maxZ) / 16.0D);
        final int minChunkY = MathHelper.floor_double((bb.minY) / 16.0D);
        final int maxChunkY = MathHelper.floor_double((bb.maxY) / 16.0D);

        for (int chunkX = minChunkX; chunkX <= maxChunkX; ++chunkX) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; ++chunkZ) {
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
                final int minChunkYClamped = MathHelper.clamp_int(minChunkY, 0, chunk.entityLists.length - 1);
                final int maxChunkYClamped = MathHelper.clamp_int(maxChunkY, 0, chunk.entityLists.length - 1);
                for (int chunkY = minChunkYClamped; chunkY <= maxChunkYClamped; ++chunkY) {
                    for (Entity entity : (List<Entity>) chunk.entityLists[chunkY]) {
                        if (!entity.isDead) {
                            boolean gotOne = false;
                            if (entity instanceof EntityItem && entity.boundingBox.intersectsWith(bb)) {
                                gotOne = !hasSolegnoliaAround(entity);
                                if (gotOne && !blacklist.isEmpty()) {
                                    final Item item = ((EntityItem) entity).getEntityItem()
                                        .getItem();
                                    for (Item blacklisted : blacklist) {
                                        if (blacklisted == item) {
                                            gotOne = false;
                                            break;
                                        }
                                    }
                                }
                            } else if (entity instanceof EntityXPOrb && entity.boundingBox.intersectsWith(bb)) {
                                gotOne = true;
                            }
                            if (gotOne) {
                                if (arraylist == null) {
                                    arraylist = new ArrayList<Entity>(
                                        magnetConfig.magnetMaxItems > 0 ? magnetConfig.magnetMaxItems : 20);
                                }
                                arraylist.add(entity);
                                if (itemsRemaining-- <= 0) {
                                    return arraylist;
                                }
                            }
                        }
                    }
                }
            }
        }

        return arraylist;
    }

    private static class ActiveMagnet {

        ItemStack item;
        int slot;

        ActiveMagnet(ItemStack item, int slot) {
            this.item = item;
            this.slot = slot;
        }
    }

    public static void setMagnetActive(EntityPlayerMP player, PacketMagnetState.SlotType type, int slot,
        boolean isActive) {
        ItemStack stack = null;
        IInventory baubles = null;
        int dropOff = -1;
        switch (type) {
            case INVENTORY:
                stack = player.inventory.getStackInSlot(slot);
                break;
            case ARMOR:
                return;
            case BAUBLES:
                baubles = BaublesUtil.instance()
                    .getBaubles(player);
                if (baubles != null) {
                    stack = baubles.getStackInSlot(slot);
                }
                break;
        }
        if (stack == null || stack.getItem() == null) {
            return;
        }
        if (type == PacketMagnetState.SlotType.BAUBLES && !isActive) {
            ItemStack[] inv = player.inventory.mainInventory;
            for (int i = 0; i < inv.length && dropOff < 0; i++) {
                if (inv[i] == null) {
                    dropOff = i;
                }
            }
            if (dropOff < 0) {
                return;
            }
        }
        switch (type) {
            case INVENTORY:
                player.inventory.setInventorySlotContents(slot, stack);
                player.inventory.markDirty();
                break;
            case ARMOR:
                return;
            case BAUBLES:
                if (dropOff < 0) {
                    baubles.setInventorySlotContents(slot, stack);
                } else {
                    baubles.setInventorySlotContents(slot, null);
                    player.inventory.setInventorySlotContents(dropOff, stack);
                }
                player.inventory.markDirty();
                break;
        }
    }

    public static void onCollideWithPlayer(EntityPlayer player, Entity entity) {
        if (!entity.worldObj.isRemote) {
            if (entity instanceof EntityXPOrb) {
                return;
            }
            EntityItem item = (EntityItem) entity;

            EntityItemPickupEvent event = new EntityItemPickupEvent(player, item);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }

            ItemStack itemstack = item.getEntityItem();
            int i = itemstack.stackSize;

            boolean canPickup = (item.field_145802_g == null || item.lifespan - item.age <= 200
                || item.field_145802_g.equals(player.getCommandSenderName()))
                && (event.getResult() == Event.Result.ALLOW || i <= 0
                    || player.inventory.addItemStackToInventory(itemstack));

            if (canPickup) {

                FMLCommonHandler.instance()
                    .firePlayerItemPickupEvent(player, item);

                item.worldObj.playSoundAtEntity(
                    player,
                    "random.pop",
                    0.2F,
                    ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(item, i);

                if (itemstack.stackSize <= 0) {
                    item.setDead();
                }
            }
        }
    }

    public static void setEntityMotionFromVector(Entity entity, Vector3 originalPosVector, float modifier) {
        Vector3 entityVector = Vector3.fromEntityCenter(entity);
        Vector3 finalVector = originalPosVector.copy()
            .subtract(entityVector);

        if (finalVector.mag() > 1) {
            finalVector.normalize();
        }

        entity.motionX = finalVector.x * modifier;
        entity.motionY = finalVector.y * modifier;
        entity.motionZ = finalVector.z * modifier;
    }

    public static void setCooldown(ItemStack stack, int cooldown) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null) {
            root = new NBTTagCompound();
            stack.setTagCompound(root);
        }
        root.setInteger(TAG_COOLDOWN, cooldown);

    }

    public static int getCooldown(ItemStack stack) {
        NBTTagCompound root = stack.getTagCompound();
        if (root == null) {
            root = new NBTTagCompound();
            stack.setTagCompound(root);
        }
        if (!root.hasKey(TAG_COOLDOWN)) {
            root.setInteger(TAG_COOLDOWN, 2);
        }
        int cd = root.getInteger(TAG_COOLDOWN);
        return cd;
    }
}
