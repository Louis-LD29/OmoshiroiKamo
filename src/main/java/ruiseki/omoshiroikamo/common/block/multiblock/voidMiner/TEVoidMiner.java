package ruiseki.omoshiroikamo.common.block.multiblock.voidMiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.utils.item.ItemHandlerHelper;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.DyeColor;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.api.energy.IPowerContainer;
import ruiseki.omoshiroikamo.api.energy.PowerHandlerUtil;
import ruiseki.omoshiroikamo.api.item.IFocusableRegistry;
import ruiseki.omoshiroikamo.api.item.WeightedStackBase;
import ruiseki.omoshiroikamo.api.multiblock.IModifierBlock;
import ruiseki.omoshiroikamo.common.achievement.ModAchievements;
import ruiseki.omoshiroikamo.common.block.ModBlocks;
import ruiseki.omoshiroikamo.common.block.multiblock.AbstractMultiBlockModifierTE;
import ruiseki.omoshiroikamo.common.block.multiblock.modifier.ModifierHandler;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.lens.BlockLaserLens;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.TEVoidOreMinerT1;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.TEVoidOreMinerT4;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.TEVoidResMinerT1;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.TEVoidResMinerT4;
import ruiseki.omoshiroikamo.common.network.PacketHandler;
import ruiseki.omoshiroikamo.common.network.PacketPowerStorage;
import ruiseki.omoshiroikamo.common.util.PlayerUtils;

public abstract class TEVoidMiner extends AbstractMultiBlockModifierTE
    implements IEnergyReceiver, IPowerContainer, ISidedInventory {

    protected int storedEnergyRF = 0;
    protected float lastSyncPowerStored = -1;
    protected EnergyStorage energyStorage;
    protected ItemStackHandler output;
    protected final int[] allSlots;
    protected List<BlockCoord> modifiers;
    protected BlockCoord lens;
    protected ModifierHandler modifierHandler;
    protected List<WeightedStackBase> possibleResults;
    protected Random rand;
    protected DyeColor focusColor;
    protected float focusBoostModifier = 1.0F;

    @SideOnly(Side.CLIENT)
    private float beamProgress = 0.0F;
    @SideOnly(Side.CLIENT)
    private long lastBeamUpdateTick = 0L;

    public TEVoidMiner() {
        this.modifiers = new ArrayList<>();
        this.modifierHandler = new ModifierHandler();
        this.possibleResults = new ArrayList<>();
        this.rand = new Random();
        this.focusColor = DyeColor.WHITE;
        this.energyStorage = new EnergyStorage(1000000);
        this.output = new ItemStackHandler(4);
        this.allSlots = new int[output.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    @Override
    public String getMachineName() {
        return "voidOreMiner";
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
    public void onProcessTick() {
        int energyExtracted = Math.min(getEnergyStored(), this.getEnergyCostPerTick());
        setEnergyStored(getEnergyStored() - energyExtracted);
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
        if (tileEntity instanceof TEVoidOreMinerT1) {
            player.triggerAchievement(ModAchievements.assemble_void_ore_miner_t1);
        }
        if (tileEntity instanceof TEVoidOreMinerT4) {
            player.triggerAchievement(ModAchievements.assemble_void_ore_miner_t4);
        }
        if (tileEntity instanceof TEVoidResMinerT1) {
            player.triggerAchievement(ModAchievements.assemble_void_res_miner_t1);
        }
        if (tileEntity instanceof TEVoidResMinerT4) {
            player.triggerAchievement(ModAchievements.assemble_void_res_miner_t4);
        }
    }

    @Override
    public boolean addToMachine(Block block, int meta, int x, int y, int z) {
        if (block == null) {
            return false;
        }

        BlockCoord coord = new BlockCoord(x, y, z);
        if (modifiers.contains(coord)) {
            return false;
        }

        boolean added = false;
        if (block == ModBlocks.blockLaserLens) {
            lens = new BlockCoord(x, y, z);
            return true;
        }
        if (block == ModBlocks.blockModifierSpeed) {
            added = true;
        } else if (block == ModBlocks.blockModifierAccuracy) {
            added = true;
        }

        if (added) {
            modifiers.add(coord);
        }

        return added;
    }

    @Override
    protected void clearStructureParts() {
        this.modifiers = new ArrayList<>();
        this.modifierHandler = new ModifierHandler();
        this.possibleResults = new ArrayList<>();
        lens = null;
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
        root.setTag("output_inv", this.output.serializeNBT());
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
        root.setInteger(PowerHandlerUtil.STORED_ENERGY_NBT_KEY, storedEnergyRF);
        this.output.deserializeNBT(root.getCompoundTag("output_inv"));
    }

    private boolean isPathToVoidClear() {
        for (int y = yCoord - 1; y >= 0; y--) {
            Block block = worldObj.getBlock(xCoord, y, zCoord);

            if (block.isAir(worldObj, xCoord, y, zCoord)) {
                continue;
            }
            if (block == Blocks.bedrock) {
                continue;
            }
            if (block == Blocks.glass) {
                continue;
            }
            if (block == ModBlocks.blockLaserCore) {
                continue;
            }
            if (block == ModBlocks.blockLaserLens) {
                continue;
            }

            return false;
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public float getBeamProgress() {
        if (!this.isPathToVoidClear()) {
            return 0.0F;
        } else {
            int ticksPassed = (int) (this.worldObj.getTotalWorldTime() - this.lastBeamUpdateTick);
            this.lastBeamUpdateTick = this.worldObj.getTotalWorldTime();

            if (ticksPassed > 1) {
                this.beamProgress -= (float) ticksPassed / 40.0F;
                if (this.beamProgress < 0.0F) {
                    this.beamProgress = 0.0F;
                }
            }

            this.beamProgress += 0.025F;
            if (this.beamProgress > 1.0F) {
                this.beamProgress = 1.0F;
            }

            return this.beamProgress;
        }
    }

    public abstract IFocusableRegistry getRegistry();

    @Override
    public void onProcessComplete() {
        if (this.possibleResults != null && !this.possibleResults.isEmpty()) {
            WeightedStackBase result = (WeightedStackBase) WeightedRandom
                .getRandomItem(this.rand, this.possibleResults);
            if (result == null) {
                return;
            }

            ItemStack resultStack = result.getMainStack();
            if (resultStack == null || resultStack.getItem() == null) {
                return;
            }

            ItemStack cloneStack = resultStack.copy();
            cloneStack.stackSize = 1;
            ItemHandlerHelper.insertItem(this.output, cloneStack, false);
            this.ejectAll(output);
        }
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
        this.focusBoostModifier = this.modifierHandler.getAttributeMultiplier("accuracy");

        if (this.possibleResults.isEmpty() && lens != null) {
            Block block = lens.getBlock(worldObj);

            if (block instanceof BlockLaserLens lensBlock) {
                int meta = lens.getMetadata(worldObj);

                if (meta >= 1) {
                    this.focusColor = lensBlock.getFocusColor(meta);
                    this.possibleResults = this.getRegistry()
                        .getFocusedList(this.focusColor, this.focusBoostModifier);
                } else {
                    this.focusColor = null;
                    this.possibleResults = this.getRegistry()
                        .getUnFocusedList();
                }
            }
        }

        boolean hasFreeSlot = false;
        for (int i = 0; i < output.getSlots(); i++) {
            ItemStack stack = output.getStackInSlot(i);
            if (stack == null || stack.stackSize < stack.getMaxStackSize()) {
                hasFreeSlot = true;
                break;
            }
        }

        if (!hasFreeSlot) {
            this.ejectAll(output);
            return false;
        }

        if (getEnergyStored() < this.getEnergyCostPerTick()) {
            return false;
        }

        return isPathToVoidClear();
    }

    public abstract int getEnergyCostPerDuration();

    public int getEnergyCostPerTick() {
        if (this.modifierHandler.hasAttribute("energycost")) {
            int e = (int) ((float) this.getEnergyCostPerDuration()
                * this.modifierHandler.getAttributeMultiplier("energycost"));
            return e / this.getCurrentProcessDuration();
        } else {
            return this.getEnergyCostPerDuration() / this.getCurrentProcessDuration();
        }
    }

    @Override
    public float getSpeedMultiplier() {
        return this.modifierHandler.hasAttribute("speed") ? this.modifierHandler.getAttributeMultiplier("speed") : 1.0F;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        ItemStack existing = output.getStackInSlot(slot);
        if (existing == null || existing.stackSize < itemstack.stackSize) {
            return false;
        }
        return itemstack.getItem() == existing.getItem();
    }

    @Override
    public int getSizeInventory() {
        return output.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return output.getStackInSlot(slotIn);
    }

    @Override
    public ItemStack decrStackSize(int fromSlot, int amount) {
        ItemStack fromStack = output.getStackInSlot(fromSlot);
        if (fromStack == null) {
            return null;
        }
        if (fromStack.stackSize <= amount) {
            output.setStackInSlot(fromSlot, null);
            return fromStack;
        }
        ItemStack result = fromStack.splitStack(amount);
        output.setStackInSlot(fromSlot, fromStack.stackSize > 0 ? fromStack : null);
        return result;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack contents) {
        if (contents == null) {
            output.setStackInSlot(slot, null);
        } else {
            output.setStackInSlot(slot, contents.copy());
        }

        ItemStack stack = output.getStackInSlot(slot);
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
            output.setStackInSlot(slot, stack);
        }
    }

    @Override
    public String getInventoryName() {
        return getMachineName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return canPlayerAccess(player);
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
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
}
