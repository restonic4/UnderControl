package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class LootContainerMixins {
    @Mixin(RandomizableContainerBlockEntity.class)
    public abstract static class RandomizableContainerBlockEntityMixin {
        @Inject(
                method = "unpackLootTable",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V",
                        shift = At.Shift.AFTER
                )
        )
        private void onUnpackLootTable(Player player, CallbackInfo ci, @Local LootTable lootTable) {
            OtherEvents.LOOT_CONTAINER_GENERATED_LOOT.invoker().onEvent(player, lootTable, (Container) (Object) this, false);
        }
    }

    @Mixin(ContainerEntity.class)
    public interface ContainerEntityMixin {
        @Inject(
                method = "unpackChestVehicleLootTable",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V",
                        shift = At.Shift.AFTER
                )
        )
        private void unpackChestVehicleLootTable(Player player, CallbackInfo ci, @Local LootTable lootTable) {
            OtherEvents.LOOT_CONTAINER_GENERATED_LOOT.invoker().onEvent(player, lootTable, (Container) (Object) this, true);
        }
    }
}
