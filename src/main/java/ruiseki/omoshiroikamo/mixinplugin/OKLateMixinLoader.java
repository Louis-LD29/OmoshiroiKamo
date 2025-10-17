package ruiseki.omoshiroikamo.mixinplugin;

import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import com.gtnewhorizon.gtnhmixins.builders.IMixins;

import ruiseki.omoshiroikamo.common.util.lib.LibMisc;

@LateMixin
@SuppressWarnings("unused")
public class OKLateMixinLoader implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins." + LibMisc.MOD_ID + ".late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return IMixins.getLateMixins(Mixins.class, loadedMods);
    }
}
