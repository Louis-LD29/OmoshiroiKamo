package ruiseki.omoshiroikamo.common.item.backpack;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.enderio.core.client.render.RenderUtil;
import com.enderio.core.common.util.DyeColor;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.omoshiroikamo.api.energy.PowerDisplayUtil;
import ruiseki.omoshiroikamo.api.enums.ModObject;
import ruiseki.omoshiroikamo.api.item.IBaubleRender;
import ruiseki.omoshiroikamo.common.entity.EntityImmortalItem;
import ruiseki.omoshiroikamo.common.item.ItemBauble;
import ruiseki.omoshiroikamo.common.item.upgrade.EnergyUpgrade;
import ruiseki.omoshiroikamo.common.util.lib.LibMods;
import ruiseki.omoshiroikamo.common.util.lib.LibResources;

public class ItemBackpack extends ItemBauble
    implements IEnergyContainerItem, IGuiHolder<PlayerInventoryGuiData>, IBaubleRender {

    @SideOnly(Side.CLIENT)
    private static IModelCustom model;

    public ItemBackpack() {
        super(ModObject.itemBackPack.unlocalisedName);
        setHasSubtypes(true);
        setMaxStackSize(1);
        setNoRepair();
        disableRightClickEquip();
    }

    public static ItemBackpack create() {
        return new ItemBackpack();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        String base = super.getUnlocalizedName(stack);
        String type;
        if (meta == 1) {
            type = "Copper";
        } else if (meta == 2) {
            type = "Iron";
        } else if (meta == 3) {
            type = "Gold";
        } else if (meta == 4) {
            type = "Diamond";
        } else if (meta == 5) {
            type = LibMods.EtFuturum.isLoaded() ? "Netherite" : "Obsidian";
        } else {
            type = "Starter";
        }
        return base + "." + type;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote && !player.isSneaking()) {
            GuiFactories.playerInventory()
                .openFromMainHand(player);
        }
        return super.onItemRightClick(itemStackIn, worldIn, player);
    }

    public int getBackpackRow(int meta) {
        switch (meta) {
            case 1:
                return 4;
            case 2:
                return 6;
            case 3:
                return 9;
            case 4:
                return 9;
            case 5:
                return 10;
            default:
                return 3;
        }
    }

    public int getBackpackCol(int meta) {
        switch (meta) {
            case 4:
                return 12;
            case 5:
                return 12;
            default:
                return 9;
        }
    }

    public int getUpgradeRow(int meta) {
        switch (meta) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 7;
            default:
                return 1;
        }
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new BackpackGui(data.getPlayer(), data, syncManager, settings, this);
    }

    // Energy
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return EnergyUpgrade.receiveEnergy(container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return EnergyUpgrade.extractEnergy(container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return EnergyUpgrade.getEnergyStored(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return EnergyUpgrade.getMaxEnergyStored(container);
    }

    @Override
    public void addDetailedEntries(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        EnergyUpgrade up = EnergyUpgrade.loadFromItem(itemstack);
        if (up != null) {
            list.add(PowerDisplayUtil.formatStoredPower(up.getEnergy(), up.getCapacity()));
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack stack) {
        return new EntityImmortalItem(world, location, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, IBaubleRender.RenderType type) {
        if (stack == null || type != IBaubleRender.RenderType.BODY) {
            return;
        }

        if (model == null) {
            model = AdvancedModelLoader
                .loadModel(new ResourceLocation(LibResources.PREFIX_MODEL + "backpack_base.obj"));
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(0F, 0.75F, 0.3F);
        GL11.glScalef(0.85F, 0.85F, 0.85F);
        GL11.glRotatef(180f, 1f, 0f, 0f);

        GL11.glColor3f(0.353f, 0.243f, 0.106f);
        RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/backpack_border.png"));
        model.renderOnly("trim1", "trim2", "trim3", "trim4", "trim5", "padding1");

        int color = DyeColor.BROWN.getColor();
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag != null && tag.hasKey("BackpackColor")) {
                color = tag.getInteger("BackpackColor");
            }
        }

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        float brightnessFactor = 1.18f;
        r = Math.min(1.0f, r * brightnessFactor);
        g = Math.min(1.0f, g * brightnessFactor);
        b = Math.min(1.0f, b * brightnessFactor);

        GL11.glColor3f(r, g, b);
        RenderUtil.bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/backpack_cloth.png"));
        model.renderOnly(
            "inner1",
            "inner2",
            "outer1",
            "outer2",
            "left_trim1",
            "right_trim1",
            "bottom_trim1",
            "body",
            "pouch1",
            "pouch2",
            "top1",
            "top2",
            "top3",
            "bottom1",
            "bottom2",
            "bottom3",
            "lip1");

        GL11.glColor3f(1f, 1f, 1f);

        String material;
        switch (stack.getItemDamage()) {
            case 1:
                material = "copper";
                break;
            case 2:
                material = "iron";
                break;
            case 3:
                material = "gold";
                break;
            case 4:
                material = "diamond";
                break;
            case 5:
                material = "netherite";
                break;
            default:
                material = "leather";
                break;
        }
        RenderUtil
            .bindTexture(new ResourceLocation(LibResources.PREFIX_MOD + "textures/items/" + material + "_clips.png"));
        model.renderOnly(
            "top4",
            "right1",
            "right2",
            "right_clip1",
            "right_clip2",
            "left1",
            "left2",
            "left_clip1",
            "left_clip2",
            "clip1",
            "clip2",
            "clip3",
            "clip4",
            "opening1",
            "opening2",
            "opening3",
            "opening4",
            "opening5");

        GL11.glPopMatrix();
    }

}
