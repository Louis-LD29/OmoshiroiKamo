package ruiseki.omoshiroikamo.plugin.structureLib;

import ruiseki.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure;
import ruiseki.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure;
import ruiseki.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure;

public class StructureCompat {

    public static void init() {
        SolarArrayStructure.registerStructureInfo();
        VoidOreMinerStructure.registerStructureInfo();
        VoidResMinerStructure.registerStructureInfo();
        NanoBotBeaconStructure.registerStructureInfo();
    }
}
