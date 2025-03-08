package com.chaotic_loom.under_control.mixin.compatibility.modmenu;

import com.chaotic_loom.under_control.UnderControl;
import com.terraformersmc.modmenu.util.mod.Mod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mod.Badge.class)
public class BadgeMixin {
    @Invoker("<init>")
    private static Mod.Badge newType(String internalName, int internalId, String translationKey, int outlineColor, int fillColor, String key) {
        throw new AssertionError("[" + UnderControl.MOD_ID + "]: Mixin injection failed!");
    }

    @Shadow(remap = false)
    @Final
    @Mutable
    private static Mod.Badge[] $VALUES;

    @Unique
    private static int currentOrdinal = 0;

    @Unique
    private static List<Mod.Badge> badgesToRegister;

    @Inject(
            method = "<clinit>",
            remap = false,
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTSTATIC,
                    target = "Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;$VALUES:[Lcom/terraformersmc/modmenu/util/mod/Mod$Badge;",
                    shift = At.Shift.AFTER
            )
    )
    private static void addCustomBadges(CallbackInfo ci) {
        var badges = new ArrayList<>(Arrays.asList($VALUES));
        var last = badges.get(badges.size() - 1);
        currentOrdinal = last.ordinal() + 1;
        badgesToRegister = new ArrayList<>();

        registerBadge("special_library", "Library", 0xff107454, 0xff093929);
        registerBadge("chaotic_loom", "Chaotic Loom", 0xFF9B59B6, 0xFF4B0082);

        badges.addAll(badgesToRegister);

        ArrayList<String> internalIds = new ArrayList<>();
        for (Mod.Badge badge : badges) {
            internalIds.add(badge.name());
        }

        $VALUES = badges.toArray(new Mod.Badge[0]);
    }

    @Unique
    private static Mod.Badge registerBadge(String id, String translationKey, int outlineColor, int fillColor) {
        var badge = newType(id.toUpperCase(), currentOrdinal, translationKey, outlineColor, fillColor, id.toLowerCase());
        currentOrdinal += 1;

        badgesToRegister.add(badge);

        return badge;
    }
}