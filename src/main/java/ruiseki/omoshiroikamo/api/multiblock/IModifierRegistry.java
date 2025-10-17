package ruiseki.omoshiroikamo.api.multiblock;

public interface IModifierRegistry {

    boolean registerModifier(IModifierBlock var1);

    boolean modifierExists(IModifierBlock var1);

    IModifierBlock getModifier(String var1);
}
