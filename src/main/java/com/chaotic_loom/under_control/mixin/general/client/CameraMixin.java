package com.chaotic_loom.under_control.mixin.general.client;

import com.chaotic_loom.under_control.api.cutscene.CutsceneAPI;
import com.chaotic_loom.under_control.client.rendering.screen_shake.types.ScreenShakeGlobalManager;
import com.chaotic_loom.under_control.util.RandomHelper;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setPosition(double d, double e, double f);

    @Shadow
    protected abstract void setRotation(float f, float g);

    @Shadow protected abstract void move(double d, double e, double f);

    @Shadow public abstract float getXRot();

    @Shadow public abstract float getYRot();

    @Shadow @Final private Quaternionf rotation;

    @Inject(method = "setup", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Camera;detached:Z", shift = At.Shift.AFTER), cancellable = true)
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if (CutsceneAPI.isPlaying()) {
            Vector3f position = CutsceneAPI.getPosition();
            Vector2f rotation = CutsceneAPI.getRotation();

            if (position != null) {
                this.setPosition(position.x, position.y, position.z);
            }

            if (rotation != null) {
                this.setRotation(rotation.x, rotation.y);
            }

            shake();

            ci.cancel();
        }
    }

    @Inject(method = "setup", at = @At("TAIL"))
    public void setupEnd(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        shake();
    }

    @Unique
    private void shake() {
        float screenShakeIntensity = ScreenShakeGlobalManager.computeGlobalShakeOffset();

        this.move(RandomHelper.randomBetween(screenShakeIntensity), RandomHelper.randomBetween(screenShakeIntensity), RandomHelper.randomBetween(screenShakeIntensity));
        this.setRotation(this.getYRot() + RandomHelper.randomBetween(screenShakeIntensity), this.getXRot() + RandomHelper.randomBetween(screenShakeIntensity));
    }
}
