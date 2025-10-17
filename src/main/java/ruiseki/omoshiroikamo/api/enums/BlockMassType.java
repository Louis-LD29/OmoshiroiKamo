package ruiseki.omoshiroikamo.api.enums;

public enum BlockMassType {

    BLOCK(0.125), // 1 block Minecraft = 1/8 m³ (để ra mass ~ 1,000 kg max)
    INGOT(0.015625), // 1/64 block
    NUGGET(0.001953125), // 1/512 block
    PLATE(0.01),
    ROD(0.005),
    PIPE(0.01),
    DENSE_PLATE(0.03),
    MACHINE(0.05),
    HEAT_INOUT(0.02),
    HEAT_INOUT_HEAVY(0.04);

    private final double volumeM3;

    BlockMassType(double volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public double getVolumeM3() {
        return volumeM3;
    }
}
