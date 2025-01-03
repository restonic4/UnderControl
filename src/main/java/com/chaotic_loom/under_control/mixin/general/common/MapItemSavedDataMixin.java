package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.EventResult;
import com.chaotic_loom.under_control.events.types.ServerPlayerExtraEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataMixin {
    @Shadow protected abstract void removeDecoration(String string);

    @WrapOperation(
            method = "tickCarriedBy",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;trackingPosition:Z",
                    ordinal = 0
            )
    )
    private boolean hideFromMap(MapItemSavedData instance, Operation<Boolean> original, @Local MapItemSavedData.HoldingPlayer holdingPlayer) {
        EventResult eventResult = ServerPlayerExtraEvents.ADDED_TO_MAP.invoker().onAddedToMap(instance, holdingPlayer);

        if (eventResult == EventResult.CANCELED) {
            removeDecoration(holdingPlayer.player.getName().getString());
            return false;
        } else {
            return original.call(instance);
        }
    }
}
