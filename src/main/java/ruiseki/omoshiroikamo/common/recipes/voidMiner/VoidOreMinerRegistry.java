package ruiseki.omoshiroikamo.common.recipes.voidMiner;

public class VoidOreMinerRegistry extends FocusableRegistry {

    private static VoidOreMinerRegistry instance;

    public static VoidOreMinerRegistry getInstance() {
        if (instance == null) {
            instance = new VoidOreMinerRegistry();
        }

        return instance;
    }

    protected VoidOreMinerRegistry() {}
}
