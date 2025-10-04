package louis.omoshiroikamo.common.item.backpack;

import static louis.omoshiroikamo.config.general.MagnetConfig.magnetConfig;
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
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import louis.omoshiroikamo.client.gui.BackpackGui;
import louis.omoshiroikamo.client.gui.modularui2.handler.UpgradeItemStackHandler;
import louis.omoshiroikamo.common.network.PacketBackPackState;
import louis.omoshiroikamo.common.network.PacketHandler;
import louis.omoshiroikamo.common.util.lib.LibObfuscation;
import vazkii.botania.common.core.helper.Vector3;

@EventBusSubscriber()
public class BackpackMagnetController {

    private static final String TAG_COOLDOWN = "magnetCooldown";
    protected static Random rand = new Random();
    private static List<Item> filter = null;

    private static final double collisionDistanceSq = 1.25 * 1.25;

    public BackpackMagnetController() {
        PacketHandler.INSTANCE
            .registerMessage(PacketBackPackState.class, PacketBackPackState.class, PacketHandler.nextID(), Side.SERVER);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        BackpackUtil.ActiveBackPack mag = getActiveMagnet(event.player);
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
            BackpackUtil.setCooldown(stack, 100, TAG_COOLDOWN);
        }
        InventoryBaubles baubleInv = PlayerHandler.getPlayerBaubles(event.player);
        for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
            ItemStack stack = baubleInv.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBackpack) {
                BackpackUtil.setCooldown(stack, 100, TAG_COOLDOWN);
                baubleInv.markDirty();
            }
        }
    }

    private static BackpackUtil.ActiveBackPack getActiveMagnet(EntityPlayer player) {
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
            if (tag == null || !tag.hasKey(BackpackGui.BACKPACKUPGRADE)) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag(BackpackGui.BACKPACKUPGRADE));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemMagnetUpgrade) {
                    return new BackpackUtil.ActiveBackPack(stack, slot);
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
            if (tag == null || !tag.hasKey(BackpackGui.BACKPACKUPGRADE)) {
                continue;
            }

            upgradeHandler.deserializeNBT(tag.getCompoundTag(BackpackGui.BACKPACKUPGRADE));

            for (int slot = 0; slot < upgradeHandler.getSlots(); slot++) {
                ItemStack upgrade = upgradeHandler.getStackInSlot(slot);
                if (upgrade != null && upgrade.getItem() instanceof ItemMagnetUpgrade) {
                    return new BackpackUtil.ActiveBackPack(stack, slot);
                }
            }
        }

        return null;
    }

    public static void doHoover(EntityPlayer player, BackpackUtil.ActiveBackPack mag) {
        int cooldown = BackpackUtil.getCooldown(mag.item, TAG_COOLDOWN);

        initFilter(mag);

        if (cooldown > 0) {
            BackpackUtil.setCooldown(mag.item, cooldown - 1, TAG_COOLDOWN);
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

            BackpackUtil.setCooldown(mag.item, 2, TAG_COOLDOWN);
        }
    }

    private static void initFilter(BackpackUtil.ActiveBackPack mag) {
        Set<String> names = new HashSet<>();
        names.addAll(Arrays.asList(magnetConfig.magnetBlacklist));
        names.addAll(BackpackUtil.getFilter(mag.item, BackpackGui.MAGNET_FILTER));

        List<Item> itemList = new ArrayList<>();

        for (String name : names) {
            BackpackUtil.addItemToFilter(itemList, name);
        }

        filter = itemList;
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
                                if (gotOne && !filter.isEmpty()) {
                                    final Item item = ((EntityItem) entity).getEntityItem()
                                        .getItem();
                                    for (Item blacklisted : filter) {
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

            String field_145802_g = ReflectionHelper.getPrivateValue(EntityItem.class, item, LibObfuscation.POTION_ID);

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
}
