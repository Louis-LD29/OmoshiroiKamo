package louis.omoshiroikamo.mixinplugin;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    NEI(new MixinBuilder().addCommonMixins("nei.RecipeInfoMixin")
        .setPhase(Phase.LATE)
        .addRequiredMod(TargetedMod.NEI));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @NotNull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
