package com.stews_zbk.vivecraftaim.mixin;

import com.stews_zbk.vivecraftaim.OffhandAimConfig;
import com.stews_zbk.vivecraftaim.ZbkVivecraftOffhandAimClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.vivecraft.client_vr.VRData;
import org.vivecraft.client_vr.VRData.VRDevicePose;
import org.vivecraft.client_vr.provider.MCVR;
import org.vivecraft.client_vr.render.helpers.RenderHelper;

/** Keeps menu pointing and HUD placement in the client integration, independently of server weapon state. */
@Mixin(targets = "org.vivecraft.client_vr.gameplay.screenhandlers.GuiHandler", remap = false)
public abstract class GuiHandlerAimMixin {
    @Unique
    private static boolean zbk$loggedMenuAimOverride;
    @Unique
    private static boolean zbk$loggedHudWristOverride;

    @Redirect(
        method = "processGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/vivecraft/client_vr/provider/MCVR;isControllerTracking(I)Z"),
        remap = false,
        require = 1)
    private static boolean zbk$useOffhandMenuTracking(MCVR vr, int controllerIndex) {
        if (!OffhandAimConfig.enabled()) {
            return vr.isControllerTracking(controllerIndex);
        }

        return vr.isControllerTracking(OffhandAimConfig.controllerIndex());
    }

    @Redirect(
        method = "processGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/vivecraft/client_vr/VRData;getController(I)Lorg/vivecraft/client_vr/VRData$VRDevicePose;"),
        remap = false,
        require = 1)
    private static VRDevicePose zbk$useOffhandMenuCursor(VRData vrData, int controllerIndex) {
        if (!OffhandAimConfig.enabled()) {
            return vrData.getController(controllerIndex);
        }

        if (!zbk$loggedMenuAimOverride) {
            zbk$loggedMenuAimOverride = true;
            ZbkVivecraftOffhandAimClient.LOGGER.info(
                "Vivecraft menu cursor override active; GUI cursor follows controller index {}",
                OffhandAimConfig.controllerIndex());
        }

        return vrData.getController(OffhandAimConfig.controllerIndex());
    }

    @Redirect(
        method = "extractGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/vivecraft/client_vr/provider/MCVR;getAimRotation(I)Lorg/joml/Matrix4fc;",
            ordinal = 1),
        remap = false,
        require = 1)
    private static Matrix4fc zbk$useConfiguredHudWristRotation(MCVR vr, int controllerIndex) {
        if (!OffhandAimConfig.enabled()) {
            return vr.getAimRotation(controllerIndex);
        }

        zbk$logHudWristOverride();
        return vr.getAimRotation(OffhandAimConfig.hudControllerIndex());
    }

    @Redirect(
        method = "extractGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/vivecraft/client_vr/render/helpers/RenderHelper;getControllerRenderPos(I)Lnet/minecraft/world/phys/Vec3;"),
        remap = false,
        require = 1)
    private static Vec3 zbk$useConfiguredHudWristPosition(int controllerIndex) {
        if (!OffhandAimConfig.enabled()) {
            return RenderHelper.getControllerRenderPos(controllerIndex);
        }

        zbk$logHudWristOverride();
        return RenderHelper.getControllerRenderPos(OffhandAimConfig.hudControllerIndex());
    }

    @Redirect(
        method = "extractGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;rotateZ(F)Lorg/joml/Matrix4f;"),
        remap = false,
        require = 1)
    private static Matrix4f zbk$useConfiguredHudWristRoll(Matrix4f matrix, float angle) {
        return matrix.rotateZ(zbk$hudWristAngle(angle));
    }

    @Redirect(
        method = "extractGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Matrix4f;rotateY(F)Lorg/joml/Matrix4f;",
            ordinal = 1),
        remap = false,
        require = 1)
    private static Matrix4f zbk$useConfiguredHudWristYaw(Matrix4f matrix, float angle) {
        return matrix.rotateY(zbk$hudWristAngle(angle));
    }

    @Redirect(
        method = "extractGui",
        at = @At(
            value = "INVOKE",
            target = "Lorg/joml/Vector3f;set(FFF)Lorg/joml/Vector3f;"),
        remap = false,
        require = 1)
    private static Vector3f zbk$useConfiguredHudWristOffset(Vector3f vector, float x, float y, float z) {
        if (!OffhandAimConfig.enabled() || OffhandAimConfig.hudControllerIndex() == 1) {
            return vector.set(x, y, z);
        }

        return vector.set(-x, y, z);
    }

    @Unique
    private static void zbk$logHudWristOverride() {
        if (!zbk$loggedHudWristOverride) {
            zbk$loggedHudWristOverride = true;
            ZbkVivecraftOffhandAimClient.LOGGER.info(
                "Vivecraft wrist HUD override active; HUD follows controller index {}",
                OffhandAimConfig.hudControllerIndex());
        }
    }

    @Unique
    private static float zbk$hudWristAngle(float angle) {
        if (!OffhandAimConfig.enabled() || OffhandAimConfig.hudControllerIndex() == 1) {
            return angle;
        }

        return -angle;
    }
}
