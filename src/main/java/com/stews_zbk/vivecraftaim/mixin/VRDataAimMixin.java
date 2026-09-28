package com.stews_zbk.vivecraftaim.mixin;

import com.stews_zbk.vivecraftaim.OffhandAimConfig;
import com.stews_zbk.vivecraftaim.ZbkVivecraftOffhandAimClient;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vivecraft.client_vr.VRData.VRDevicePose;

/** Routes Vivecraft aim through the configured controller; the original method remains active when disabled. */
@Mixin(targets = "org.vivecraft.client_vr.VRData", remap = false)
public abstract class VRDataAimMixin {
    @Unique
    private static boolean zbk$loggedAimOverride;

    @Shadow(remap = false)
    public abstract VRDevicePose getController(int controllerIndex);

    @Inject(
        method = "getAim",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1)
    private void zbk$useOffhandControllerAim(CallbackInfoReturnable<VRDevicePose> cir) {
        if (!OffhandAimConfig.enabled()) {
            return;
        }

        if (!zbk$loggedAimOverride) {
            zbk$loggedAimOverride = true;
            ZbkVivecraftOffhandAimClient.LOGGER.info(
                "Vivecraft getAim() override active; returning controller index {}",
                OffhandAimConfig.controllerIndex());
        }

        cir.setReturnValue(this.getController(OffhandAimConfig.controllerIndex()));
    }
}
