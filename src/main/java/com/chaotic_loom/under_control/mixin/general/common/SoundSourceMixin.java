package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.UnderControl;
import com.chaotic_loom.under_control.events.types.RegistrationEvents;
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

        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            CustomValue custom = container.getMetadata().getCustomValue(UnderControl.MOD_ID);
            if (custom != null && custom.getType() == CustomValue.CvType.OBJECT) {
                CustomValue.CvObject modmenuObj = custom.getAsObject();
                CustomValue soundSourcesValue = modmenuObj.get("sound_sources");

                if (soundSourcesValue != null && soundSourcesValue.getType() == CustomValue.CvType.ARRAY) {
                    CustomValue.CvArray badgesArray = soundSourcesValue.getAsArray();
                    for (int i = 0; i < badgesArray.size(); i++) {
                        String soundSource = badgesArray.get(i).getAsString();

                        registerSource(soundSource);
                        RegistrationEvents.SOUND_SOURCE.invoker().onEvent(soundSource);
                    }
                }
            }
        }

        soundSources.addAll(sourcesToRegister);

        ArrayList<String> internalIds = new ArrayList<>();
        for (SoundSource soundSource : soundSources) {
            internalIds.add(soundSource.name());
        }

        $VALUES = soundSources.toArray(new SoundSource[0]);

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
