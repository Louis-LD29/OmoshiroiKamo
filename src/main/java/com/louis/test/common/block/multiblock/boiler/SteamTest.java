package com.louis.test.common.block.multiblock.boiler;

public class SteamTest {

    public static void main(String[] args) {
        SteamCalculator boiler = new SteamCalculator();

        boiler.setSteamVolumeMB(0);
        boiler.setWaterVolumeMB(4000); // 1000 mB = 1L nước
        boiler.setTankCapacityMB(8000); // Tổng dung tích bình
        boiler.setHeatSourceTempC(300); // Nhiệt độ ban đầu

        int step = 0;
        while (boiler.getWaterVolumeMB() > 0 && boiler.getHeatSourceTempC() > boiler.getTboil()) {
            double beforeTemp = boiler.getHeatSourceTempC();
            double generated = boiler.calculateSteamStep();
            double afterTemp = boiler.getHeatSourceTempC();
            double deltaTempDrop = beforeTemp - afterTemp;

            boiler.printState(++step, generated, deltaTempDrop);

            // Dừng sớm nếu không còn sinh hơi
            if (generated <= 0.01) break;
        }
    }
}
