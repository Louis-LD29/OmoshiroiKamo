package ruiseki.omoshiroikamo.common.recipe.voidMiner;

public class VoidResMinerRegistry extends FocusableRegistry {

    private static VoidResMinerRegistry instance;

    public static VoidResMinerRegistry getInstance() {
        if (instance == null) {
            instance = new VoidResMinerRegistry();
        }

        return instance;
    }

    protected VoidResMinerRegistry() {}
}
