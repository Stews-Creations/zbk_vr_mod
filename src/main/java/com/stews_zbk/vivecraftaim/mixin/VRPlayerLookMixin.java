package com.stews_zbk.vivecraftaim.mixin;

import com.stews_zbk.vivecraftaim.OffhandAimConfig;
import com.stews_zbk.vivecraftaim.ZbkVivecraftOffhandAimClient;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.phys.Vec3;
import org.vivecraft.client_vr.VRData;
import org.vivecraft.client_vr.VRData.VRDevicePose;

/** Adapts player look to the configured controller while retaining the disabled-mode fallback. */
@Mixin(targets = "org.vivecraft.client_vr.gameplay.VRPlayer", remap = false)
public abstract class VRPlayerLookMixin {
    @Unique
    private static boolean zbk$loggedLookOverride;
    @Unique
    private static boolean zbk$loggedCrosshairAlignment;

    @Shadow(remap = false)
    private Vec3 lookAtPos;
    @Shadow(remap = false)
    public Vec3 crossVec;

    @Inject(
        method = "doPermanentLookOverride",
        at = @At("TAIL"),
        remap = false,
        require = 1)
    private void zbk$forceOffhandPlayerLook(LocalPlayer player, VRData vrData, CallbackInfo ci) {
        if (!OffhandAimConfig.enabled() || player == null || vrData == null) {
            return;
        }

        if (this.lookAtPos != null || this.crossVec != null) {
            if (!zbk$loggedCrosshairAlignment) {
                zbk$loggedCrosshairAlignment = true;
                ZbkVivecraftOffhandAimClient.LOGGER.info(
                    "Vivecraft player look override preserving crosshair-aligned target");
            }
            return;
        }

        VRDevicePose controllerPose = vrData.getController(OffhandAimConfig.controllerIndex());
        player.setYRot(controllerPose.getYaw());
        player.setXRot(-controllerPose.getPitch());
        player.setYHeadRot(player.getYRot());

        if (!zbk$loggedLookOverride) {
            zbk$loggedLookOverride = true;
            ZbkVivecraftOffhandAimClient.LOGGER.info(
                "Vivecraft player look override active; yaw/pitch now follows controller index {}",
                OffhandAimConfig.controllerIndex());
        }
    }
}
