package com.chaotic_loom.under_control.mixin.general.common;

import com.chaotic_loom.under_control.events.types.OtherEvents;
import com.llamalad7.mixinextras.sugar.Local;
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
    public static class RandomizableContainerBlockEntityMixin {
        @Inject(
                method = "unpackLootTable",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V"
                )
        )
        private void onUnpackLootTable(Player player, CallbackInfo ci, @Local LootTable lootTable) {
            onLootTableOpenedFirstTime(player, lootTable, false);
        }
    }

    @Mixin(ContainerEntity.class)
    public interface ContainerEntityMixin {
        @Inject(
                method = "unpackChestVehicleLootTable",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V"
                )
        )
        private void unpackChestVehicleLootTable(Player player, CallbackInfo ci, @Local LootTable lootTable) {
            onLootTableOpenedFirstTime(player, lootTable, true);
        }
    }

    @Unique
    private static void onLootTableOpenedFirstTime(Player player, LootTable lootTable, boolean isEntityContainer) {
        OtherEvents.LOOT_CONTAINER_GENERATED_LOOT.invoker().onEvent(player, lootTable, isEntityContainer);
    }
}
