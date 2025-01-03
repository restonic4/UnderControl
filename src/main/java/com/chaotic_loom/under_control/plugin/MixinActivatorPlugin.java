package com.chaotic_loom.under_control.plugin;

import com.chaotic_loom.under_control.UnderControl;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public abstract class MixinActivatorPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> conditions = new HashMap<>();

    public static void addLoadCondition(String mixinClassName, Supplier<Boolean> condition) {
        if (!conditions.containsKey(mixinClassName)) {
            conditions.put(mixinClassName, condition);
            UnderControl.LOGGER.info(mixinClassName + " added to conditional mixins");
        }
    }

    public static void addLoadConditions(Supplier<Boolean> condition, String... mixinClassNames) {
        for (String mixinClassName : mixinClassNames) {
            if (!conditions.containsKey(mixinClassName)) {
                conditions.put(mixinClassName, condition);
                UnderControl.LOGGER.info(mixinClassName + " added to conditional mixins");
            }
        }
    }

    public abstract void beforeMixinApplication(); // You should override this method to add your mixin restrictions

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        beforeMixinApplication();

        boolean shouldApply = conditions.getOrDefault(mixinClassName, TRUE).get();

        if (!shouldApply) {
            UnderControl.LOGGER.warn("Skipping mixin: " + targetClassName + " -> " + mixinClassName);
        }

        return shouldApply;
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
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

    }
}
