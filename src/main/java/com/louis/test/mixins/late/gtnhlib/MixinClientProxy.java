package com.louis.test.mixins.late.gtnhlib;

import com.louis.test.Test;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizon.gtnhlib.ClientProxy;

@Mixin(ClientProxy.class)
public class MixinClientProxy {

    @Inject(method = "init", at = @At("HEAD"), remap = false)
    private void mymodid$init(CallbackInfo ci) {
        Test.LOG.info("Hello from GTNHLib ClientProxy Mixin!");
    }

}
