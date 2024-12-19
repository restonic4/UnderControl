package com.restonic4.under_control.mixin;

import com.restonic4.under_control.events.EventResult;
import com.restonic4.under_control.events.types.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void popResource(Level level, Supplier<ItemEntity> supplier, ItemStack itemStack, CallbackInfo ci) {
        if (BlockEvents.DROPS_ITEMS.invoker().onDropItems(level, supplier, itemStack) == EventResult.CANCELED) {
            ci.cancel();
        }
    }

    @Inject(method = "popExperience", at = @At("HEAD"), cancellable = true)
    protected void popExperience(ServerLevel serverLevel, BlockPos blockPos, int size, CallbackInfo ci) {
        if (BlockEvents.DROPS_EXPERIENCE.invoker().onDropExperience(serverLevel, blockPos, size) == EventResult.CANCELED) {
            ci.cancel();
        }
    }
}
