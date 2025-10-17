package ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.enderio.core.common.util.BlockCoord;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import ruiseki.omoshiroikamo.api.energy.IPowerContainer;
import ruiseki.omoshiroikamo.api.energy.PowerHandlerUtil;
import ruiseki.omoshiroikamo.api.multiblock.IModifierBlock;
import ruiseki.omoshiroikamo.common.achievement.ModAchievements;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.block.multiblock.AbstractMultiBlockModifierTE;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.ModifierAttributes;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.ModifierHandler;
import ruiseki.omoshiroikamo.common.network.PacketHandler;
import ruiseki.omoshiroikamo.common.network.PacketNBBClientFlight;
import ruiseki.omoshiroikamo.common.network.PacketPowerStorage;
import ruiseki.omoshiroikamo.common.util.PlayerUtils;

public abstract class TENanoBotBeacon extends AbstractMultiBlockModifierTE implements IEnergyReceiver, IPowerContainer {

    private int storedEnergyRF = 0;
    protected float lastSyncPowerStored = -1;
    private final EnergyStorage energyStorage;
    private boolean dealsWithFlight = false;
    private boolean markNeedsToUpdatePlayer = false;
    protected ModifierHandler modifierHandler;
    protected List<BlockCoord> modifiers;

    public TENanoBotBeacon(int eBuffSize) {
        this.energyStorage = new EnergyStorage(eBuffSize);
        this.modifierHandler = new ModifierHandler();
        this.modifiers = new ArrayList<>();
    }

    @Override
    public boolean addToMachine(Block block, int meta, int x, int y, int z) {
        if (block == null) {
            return false;
        }

        BlockCoord coord = new BlockCoord(x, y, z);
        if (this.modifiers.contains(coord)) {
            return false;
        }

        boolean added = false;
        if (block == ModBlocks.blockModifierSpeed) {
            added = true;
        } else if (block == ModBlocks.blockModifierFlight) {
            added = true;
        } else if (block == ModBlocks.blockModifierNightVision) {
            added = true;
        } else if (block == ModBlocks.blockModifierHaste) {
            added = true;
        } else if (block == ModBlocks.blockModifierStrength) {
            added = true;
        } else if (block == ModBlocks.blockModifierWaterBreathing) {
            added = true;
        } else if (block == ModBlocks.blockModifierRegeneration) {
            added = true;
        } else if (block == ModBlocks.blockModifierSaturation) {
            added = true;
        } else if (block == ModBlocks.blockModifierResistance) {
            added = true;
        } else if (block == ModBlocks.blockModifierJumpBoost) {
            added = true;
        } else if (block == ModBlocks.blockModifierFireResistance) {
            added = true;
        }

        if (added) {
            this.modifiers.add(coord);
        }

        return added;
    }

    @Override
    protected void clearStructureParts() {
        this.modifiers = new ArrayList<>();
        this.modifierHandler = new ModifierHandler();
        this.removePlayerEffects();
        this.dealsWithFlight = false;
    }

    private void addPlayerEffects() {
        if (this.player != null && PlayerUtils.doesPlayerExist(this.worldObj, this.player.getId())) {
            EntityPlayer plr = PlayerUtils.getPlayerFromWorld(this.worldObj, this.player.getId());
            if (plr != null) {

                int potionDuration = this.getBaseDuration() * 2 + 300;
                if (this.modifierHandler != null) {
                    int ecost = (int) this.modifierHandler.getAttributeMultiplier("energycost_fixed")
                        * this.getBaseDuration();

                    if (this.getEnergyStored() >= ecost) {
                        setEnergyStored(getEnergyStored() - ecost);

                        if (this.modifierHandler
                            .hasAttribute(ModifierAttributes.E_FLIGHT_CREATIVE.getAttributeName())) {
                            plr.capabilities.allowFlying = true;
                            this.dealsWithFlight = true;
                            PacketHandler
                                .sendToAllAround(new PacketNBBClientFlight(this.player.getId(), true), plr, 1.0F);
                        } else if (this.dealsWithFlight) {
                            plr.capabilities.allowFlying = false;
                            this.dealsWithFlight = false;
                            PacketHandler
                                .sendToAllAround(new PacketNBBClientFlight(this.player.getId(), false), plr, 1.0F);
                        }

                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_SPEED.getAttributeName(),
                            potionDuration,
                            Potion.moveSpeed);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_NIGHT_VISION.getAttributeName(),
                            potionDuration,
                            Potion.nightVision);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_HASTE.getAttributeName(),
                            potionDuration,
                            Potion.digSpeed);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_STRENGTH.getAttributeName(),
                            potionDuration,
                            Potion.damageBoost);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_WATER_BREATHING.getAttributeName(),
                            potionDuration,
                            Potion.waterBreathing);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_REGEN.getAttributeName(),
                            potionDuration,
                            Potion.regeneration);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_SATURATION.getAttributeName(),
                            potionDuration,
                            Potion.field_76443_y);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_RESISTANCE.getAttributeName(),
                            potionDuration,
                            Potion.resistance);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_JUMP_BOOST.getAttributeName(),
                            potionDuration,
                            Potion.jump);
                        this.addPotionEffect(
                            plr,
                            ModifierAttributes.P_FIRE_RESISTANCE.getAttributeName(),
                            potionDuration,
                            Potion.fireResistance);
                    } else {
                        this.removePlayerEffects();
                        this.markNeedsToUpdatePlayer = true;
                    }
                }
            }
        }
    }

    private void addPotionEffect(EntityPlayer plr, String pe, int potionDuration, Potion effect) {
        if (this.modifierHandler.hasAttribute(pe)) {
            int level = (int) Math
                .min(this.modifierHandler.getAttributeMultiplier(pe) - 1.0F, (float) (this.maxPotionLevel(pe) - 1));

            if (level >= 0) {
                plr.addPotionEffect(new PotionEffect(effect.id, potionDuration, level, true));
            }
        }
    }

    private void removePlayerEffects() {
        if (this.player == null) {
            return;
        }
        EntityPlayer plr = PlayerUtils.getPlayerFromWorld(this.worldObj, this.player.getId());
        if (plr == null || this.modifierHandler == null) {
            return;
        }
        boolean hadFlight = this.dealsWithFlight;
        boolean hasFlightAttribute = this.modifierHandler
            .hasAttribute(ModifierAttributes.E_FLIGHT_CREATIVE.getAttributeName());
        if (hadFlight || hasFlightAttribute) {
            if (plr.capabilities.allowFlying) {
                plr.capabilities.allowFlying = false;
            }
            if (plr.capabilities.isFlying) {
                plr.capabilities.isFlying = false;
            }
            PacketHandler.sendToAllAround(new PacketNBBClientFlight(this.player.getId(), false), plr, 1.0F);
            this.dealsWithFlight = false;
        }
    }

    public void onChunkUnload() {
        this.removePlayerEffects();
        this.markNeedsToUpdatePlayer = true;
        super.onChunkUnload();
    }

    protected abstract int maxPotionLevel(String var1);

    @Override
    public int getBaseDuration() {
        return 40;
    }

    @Override
    public int getMinDuration() {
        return getBaseDuration();
    }

    @Override
    public int getMaxDuration() {
        return getBaseDuration();
    }

    @Override
    public float getSpeedMultiplier() {
        return 0.0F;
    }

    @Override
    public boolean canProcess() {
        List<IModifierBlock> mods = new ArrayList<>();
        for (BlockCoord coord : this.modifiers) {
            Block blk = coord.getBlock(worldObj);
            if (blk instanceof IModifierBlock) {
                mods.add((IModifierBlock) blk);
            }
        }

        this.modifierHandler.setModifiers(mods);
        this.modifierHandler.calculateAttributeMultipliers();

        return this.player != null;
    }

    @Override
    public void onProcessTick() {

    }

    @Override
    public void onProcessComplete() {
        this.addPlayerEffects();
    }

    @Override
    public void onFormed() {
        if (this.player == null) {
            return;
        }
        EntityPlayer player = PlayerUtils.getPlayerFromWorld(this.worldObj, this.player.getId());
        if (player == null) {
            return;
        }
        TileEntity tileEntity = getLocation().getTileEntity(worldObj);
        if (tileEntity instanceof TENanoBotBeaconT1) {
            player.triggerAchievement(ModAchievements.assemble_nano_bot_beacon_t1);
        }
        if (tileEntity instanceof TENanoBotBeaconT4) {
            player.triggerAchievement(ModAchievements.assemble_nano_bot_beacon_t4);
        }
    }

    @Override
    public String getMachineName() {
        return "nanoBotBeacon";
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        boolean powerChanged = (lastSyncPowerStored != storedEnergyRF && shouldDoWorkThisTick(5));
        if (powerChanged) {
            lastSyncPowerStored = storedEnergyRF;
            PacketHandler.sendToAllAround(new PacketPowerStorage(this), this);
        }
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int result = Math.min(getMaxEnergyReceived(), maxReceive);
        result = Math.min(getMaxEnergyStored() - getEnergyStored(), result);
        result = Math.max(0, result);
        if (result > 0 && !simulate) {
            setEnergyStored(getEnergyStored() + result);
        }
        return result;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public int getEnergyStored() {
        return storedEnergyRF;
    }

    @Override
    public void setEnergyStored(int storedEnergy) {
        storedEnergyRF = Math.min(storedEnergy, getMaxEnergyStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    public int getMaxEnergyReceived() {
        return energyStorage.getMaxReceive();
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
        int energy;
        if (root.hasKey("storedEnergy")) {
            float storedEnergyMJ = root.getFloat("storedEnergy");
            energy = (int) (storedEnergyMJ * 10);
        } else {
            energy = root.getInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY);
        }
        setEnergyStored(energy);
        root.setBoolean("dflight", this.dealsWithFlight);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
        this.storedEnergyRF = root.getInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY);
        this.dealsWithFlight = root.getBoolean("dflight");
    }

}
