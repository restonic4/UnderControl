package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.events.types.RegistrationEvents;
import com.chaotic_loom.under_control.util.HiddenSoundSourcesCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.sounds.SoundSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(SoundSource.class)
public class SoundSourceMixin {
    @Invoker("<init>")
    private static SoundSource newType(String internalName, int internalId, String id) {
        throw new AssertionError("[" + UnderControl.MOD_ID + "]: Mixin injection failed!");
    }

    @Shadow()
    @Final
    @Mutable
    private static SoundSource[] $VALUES;

    @Unique
    private static int currentOrdinal = 0;

    @Unique
    private static List<SoundSource> sourcesToRegister;

    @Inject(
            method = "<clinit>",
            remap = false,
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTSTATIC,
                    target = "Lnet/minecraft/sounds/SoundSource;$VALUES:[Lnet/minecraft/sounds/SoundSource;",
                    shift = At.Shift.AFTER
            )
    )
    private static void addCustomSources(CallbackInfo ci) {
        var soundSources = new ArrayList<>(Arrays.asList($VALUES));
        var last = soundSources.get(soundSources.size() - 1);
        currentOrdinal = last.ordinal() + 1;
        sourcesToRegister = new ArrayList<>();

        System.out.println("-----------------------------------");
        System.out.println("SOUND SOURCES");
        System.out.println("-----------------------------------");

        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            CustomValue custom = container.getMetadata().getCustomValue(UnderControl.MOD_ID);

            if (custom != null && custom.getType() == CustomValue.CvType.OBJECT) {
                CustomValue.CvObject obj = custom.getAsObject();

                CustomValue soundSourcesValue = obj.get("sound_sources");

                if (soundSourcesValue != null && soundSourcesValue.getType() == CustomValue.CvType.ARRAY) {
                    CustomValue.CvArray sourcesArray = soundSourcesValue.getAsArray();
                    for (int i = 0; i < sourcesArray.size(); i++) {
                        String soundSource = sourcesArray.get(i).getAsString();

                        System.out.println(soundSource);

                        registerSource(soundSource);
                        RegistrationEvents.SOUND_SOURCE.invoker().onEvent(soundSource);
                    }
                }

                CustomValue hiddenSoundSourcesValue = obj.get("hidden_sound_sources");

                if (hiddenSoundSourcesValue != null && hiddenSoundSourcesValue.getType() == CustomValue.CvType.ARRAY) {
                    CustomValue.CvArray sourcesArray = hiddenSoundSourcesValue.getAsArray();
                    for (int i = 0; i < sourcesArray.size(); i++) {
                        String soundSource = sourcesArray.get(i).getAsString();

                        System.out.println(soundSource);

                        SoundSource newSoundSource = registerSource(soundSource);
                        HiddenSoundSourcesCache.registerSoundSource(newSoundSource);
                        RegistrationEvents.SOUND_SOURCE.invoker().onEvent(soundSource);
                    }
                }
            }
        }

        System.out.println("-----------------------------------");

        soundSources.addAll(sourcesToRegister);

        ArrayList<String> internalIds = new ArrayList<>();
        for (SoundSource soundSource : soundSources) {
            internalIds.add(soundSource.name());
        }

        $VALUES = soundSources.toArray(new SoundSource[0]);
    }

    @Inject(
            method = "<clinit>",
            remap = false,
            at = @At("RETURN")
    )
    private static void onSoundSourceInitCompleted(CallbackInfo ci) {
        RegistrationEvents.SOUND_SOURCE_COMPLETED.invoker().onEvent();
    }

    @Unique
    private static SoundSource registerSource(String id) {
        var source = newType(id.toUpperCase(), currentOrdinal, id.toLowerCase());
        currentOrdinal += 1;

        sourcesToRegister.add(source);

        System.out.println("SoundSource " + id + " added!");

        return source;
    }
}
