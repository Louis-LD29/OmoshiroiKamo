package louis.omoshiroikamo.plugin.structureLib;

import louis.omoshiroikamo.common.block.multiblock.nanoBotBeacon.NanoBotBeaconStructure;
import louis.omoshiroikamo.common.block.multiblock.solarArray.SolarArrayStructure;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.oreMiner.VoidOreMinerStructure;
import louis.omoshiroikamo.common.block.multiblock.voidMiner.resMiner.VoidResMinerStructure;

public class StructureCompat {

    public static void init() {
        SolarArrayStructure.registerStructureInfo();
        VoidOreMinerStructure.registerStructureInfo();
        VoidResMinerStructure.registerStructureInfo();
        NanoBotBeaconStructure.registerStructureInfo();
    }
}
