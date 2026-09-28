package com.stews_zbk.vivecraftaim.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/** Restricts optional mixin application to the explicitly supported Vivecraft target classes. */
public class ZbkMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return "org.vivecraft.client_vr.VRData".equals(targetClassName)
            || "org.vivecraft.client_vr.gameplay.VRPlayer".equals(targetClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (mixinClassName.startsWith("com.stews_zbk.vivecraftaim.mixin.")) {
            System.out.println("[ZBK Vivecraft Offhand Aim] Applied mixin to " + targetClassName);
        }
    }
}
