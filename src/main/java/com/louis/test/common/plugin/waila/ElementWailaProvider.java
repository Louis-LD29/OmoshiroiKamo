package com.louis.test.common.plugin.waila;

import com.louis.test.api.enums.ElementType;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ElementWailaProvider implements IWailaEntityProvider {

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
                                     IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (!(entity instanceof EntityLivingBase)) return currenttip;

        long worldTime = accessor.getWorld()
            .getTotalWorldTime();

        if (tag.hasKey("ElementMarks")) {
            NBTTagCompound markData = tag.getCompoundTag("ElementMarks");

            for (ElementType type : ElementType.VALUES) {
                if (type == ElementType.NONE) continue;

                String idKey = String.valueOf(type.ordinal());

                if (markData.hasKey(idKey)) {
                    long expire = markData.getLong(idKey);
                    if (expire <= worldTime) continue;

                    long remaining = expire - worldTime;
                    double seconds = remaining / 20.0;

                    String localized = StatCollector.translateToLocal(
                        "element." + type.name()
                            .toLowerCase() + ".name");
                    String display = type.getColorCode() + localized
                        + " §7("
                        + String.format("%.1f", seconds)
                        + "s left)";
                    currenttip.add(display);

                }
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
                                     IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
                                     IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (ent instanceof EntityLivingBase) {
            NBTTagCompound data = ent.getEntityData();
            if (data.hasKey("ElementMarks")) {
                tag.setTag("ElementMarks", data.getCompoundTag("ElementMarks"));
            }
        }
        return tag;
    }

}
