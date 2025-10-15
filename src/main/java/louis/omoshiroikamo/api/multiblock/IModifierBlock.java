package louis.omoshiroikamo.api.multiblock;

import java.util.List;

public interface IModifierBlock {

    String getModifierName();

    List<IModifierAttribute> getAttributes();
}
