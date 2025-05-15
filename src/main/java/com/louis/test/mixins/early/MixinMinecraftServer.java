package com.louis.test.mixins.early;

import com.louis.test.Test;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "tick", at = @At("HEAD"))
    private void mymodid$tick(CallbackInfo ci) {
        Test.LOG.info("Hello From Tick Mixin!");
    }

}
