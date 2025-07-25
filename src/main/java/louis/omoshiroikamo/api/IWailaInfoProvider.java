package louis.omoshiroikamo.api;

public interface IWailaInfoProvider {

    default boolean hasItemStorage() {
        return false;
    }

    default boolean hasFluidStorage() {
        return false;
    }

    default boolean hasEnergyStorage() {
        return false;
    }

    default boolean hasActiveStatus() {
        return false;
    }

    default boolean hasProcessStatus() {
        return false;
    }
}
