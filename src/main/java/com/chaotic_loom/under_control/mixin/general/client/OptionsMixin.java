package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.util.HiddenSoundSourcesCache;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

@Mixin(Options.class)
public abstract class OptionsMixin {

    @Shadow protected abstract OptionInstance<Double> createSoundSliderOptionInstance(String string, SoundSource soundSource);

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/Util;make(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;",
                    ordinal = 0
            )
    )
    private <T> T redirectMakeSoundSourceVolumes(T obj, Consumer<T> consumer) {
        EnumMap<SoundSource, OptionInstance<Double>> enumMap = new EnumMap<>(SoundSource.class);

        for (SoundSource soundSource : SoundSource.values()) {
            if (!HiddenSoundSourcesCache.isHidden(soundSource)) {
                enumMap.put(soundSource, this.createSoundSliderOptionInstance("soundCategory." + soundSource.getName(), soundSource));
            }
        }

        return (T) enumMap;
    }

    @WrapOperation(
            method = "processOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Options$FieldAccess;process(Ljava/lang/String;Lnet/minecraft/client/OptionInstance;)V",
                    ordinal = 67
            )
    )
    private <T> void processOptions(Options.FieldAccess instance, String s, OptionInstance<T> tOptionInstance, Operation<Void> original, @Local SoundSource soundSource) {
        if (!HiddenSoundSourcesCache.isHidden(soundSource)) {
            original.call(instance, s, tOptionInstance);
        }
    }
}
