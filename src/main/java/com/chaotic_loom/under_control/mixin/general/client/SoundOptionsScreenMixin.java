package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.util.HiddenSoundSourcesCache;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin {
    @Redirect(
            method = "getAllSoundOptionsExceptMaster",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
            )
    )
    private Stream<SoundSource> getAllSoundOptionsExceptMaster(Stream<SoundSource> instance, Predicate<? super SoundSource> predicate) {
        Predicate<? super SoundSource> customPredicate = (soundSource) -> soundSource != SoundSource.MASTER && !HiddenSoundSourcesCache.isHidden(soundSource);
        return instance.filter(customPredicate);
    }
}
