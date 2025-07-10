package com.louis.test.api.interfaces.energy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.louis.test.api.enums.Material;
import com.louis.test.common.item.ModItems;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */

public class MaterialWireType extends WireType {

    private final Material material;
    public static final Map<Material, MaterialWireType> materialWireTypes = new HashMap<>();

    static {
        for (Material mat : Material.values()) {
            materialWireTypes.put(mat, new MaterialWireType(mat));
        }
    }

    public MaterialWireType(Material material) {
        super();
        this.material = material;
    }

    @Override
    public String getUniqueName() {
        return material.name();
    }

    @Override
    public double getLossRatio() {
        return 1.0 / Math.max(1.0, material.getElectricalConductivity() / 1e6);
    }

    @Override
    public int getTransferRate() {
        return material.getMaxPowerTransfer();
    }

    @Override
    public int getColour(ImmersiveNetHandler.Connection connection) {
        return material.getColor();
    }

    @Override
    public double getSlack() {
        double elasticity = 1.0 / Math.sqrt(material.getMaxPressureMPa());
        return 1.001 + elasticity * 0.1;
    }

    @Override
    public IIcon getIcon(ImmersiveNetHandler.Connection connection) {
        return WireType.iconDefaultWire;
    }

    @Override
    public int getMaxLength() {
        return material.getVoltageTier()
            .ordinal() * 8 + 16;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ModItems.itemWireCoil, 1, material.ordinal());
    }

    @Override
    public double getRenderDiameter() {
        return 0.03125 + 0.01 * material.getVoltageTier()
            .ordinal();
    }

    @Override
    public boolean isEnergyWire() {
        return material.getElectricalConductivity() > 1e6;
    }

    public Material getMaterial() {
        return material;
    }
}
